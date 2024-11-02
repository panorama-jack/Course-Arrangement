<template>
  <div style="margin: 10px 0">
    <div style="margin: 10px 0">
      <el-select style="width: 200px" v-model="college" v-if="isAdministrator" placeholder="请选择学院">
        <el-option v-for="item in colleges" :key="item.name" :label="item.name" :value="item.value">
          {{ item.name }}
        </el-option>
      </el-select>
      <el-input style="width: 200px" placeholder="请输入班级名称" suffix-icon="el-icon-search" class="ml-5" v-model="className"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='取消'
          icon="el-icon-info"
          icon-color="red"
          title="您确定批量删除这些数据吗？"
          @confirm="delBatch">
        <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column label=学院 width="140">
        <template slot-scope="scope">
          {{ getLabel(colleges, scope.row.collegeNo, 'value', 'name') }}
        </template>
      </el-table-column>
      <el-table-column prop="gradeNo" label="年级">
        <template slot-scope="scope">
          {{ getLabel(grades, scope.row.gradeNo, 'value', 'name') }}
        </template>
      </el-table-column>

      <el-table-column prop="gradeNo" label="专业">
        <template slot-scope="scope">
          {{ getLabel(majors, scope.row.majorNo, 'value', 'name') }}
        </template>
      </el-table-column>

      <el-table-column prop="classNo" label="班级编号"></el-table-column>
      <el-table-column prop="className" label="班级名称" width="200"></el-table-column>
      <el-table-column prop="teacherNo" label="班主任"  width="80">
        <template slot-scope="scope">
          {{ getLabel(teachers, scope.row.teacherNo, 'teacherNo', 'teacherName') }}
        </template>
      </el-table-column>
      <el-table-column prop="studentNumber" label="班级人数"  width="80"></el-table-column>
      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='取消'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)">
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 20, 50]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="班级信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small">

        <el-form-item label="年级">
          <el-select clearable v-model="form.gradeNo" placeholder="请选择" style="width: 100%"
                     @change="handleSelectChange">
            <el-option v-for="item in grades" :key="item.name" :label="item.name" :value="item.value">
              {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="学院" v-if="isAdministrator">
          <el-select clearable v-model="form.collegeNo" placeholder="请选择" style="width: 100%"
                     @change="handleCollegeSelectChange">
            <el-option v-for="item in colleges" :key="item.name" :label="item.name" :value="item.value">
              {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="专业">
          <el-select clearable v-model="form.majorNo" placeholder="请选择"  filterable style="width: 100%"
                     @change="handleSelectChange">
            <el-option v-for="item in majors" :key="item.name" :label="item.name" :value="item.value">
              {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="班级编号">
          <el-input v-model="form.classNo" disabled autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="班级名称">
          <el-input v-model="form.className" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="班主任">
          <el-select clearable v-model="form.teacherNo" placeholder="请选择" style="width: 100%">
            <el-option v-for="item in formTeachers" :key="item.teacherName" :label="item.teacherName"
                       :value="item.teacherNo">
              {{ item.teacherName }}
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "ClassInfo",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      className: "",
      college: "",
      colleges: [],
      grades: [],
      teachers: [],
      formTeachers: [],
      majors: [],
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      isAdministrator:false,
      userRole:"",
      userCollege:"",
    }
  },
  created() {
    this.load(),
        this.request.get("/dict/college").then(res => {
          this.colleges = res.data
        }),
        this.request.get("/dict/grade").then(res => {
          this.grades = res.data
        }),
        this.request.get("/dict/major").then(res => {
          this.majors = res.data
        })
  },

  async mounted() {
    let userInfo;
    const userString = localStorage.getItem("user");

    if (userString) {
      userInfo = JSON.parse(userString); // 解析本地存储的用户信息
      this.token = userInfo.token;

      try {
        const res = await this.request.get("/user/getUserRoleAndCollege/" + userInfo.userNo);
        this.userCollege = res.data.college;
        this.userRole = res.data.role;
        // 在获取到数据后再进行处理
        if (this.userRole === "ROLE_ADMINISTRATOR") {
          this.isAdministrator = true;
        }
      } catch (error) {
        console.error('请求失败', error);
      }
    }
    // 如果不是管理员，设置默认学院编号
    if (!this.isAdministrator) {
      this.college = this.userCollege;
      this.request.get("/teacher/selectByCollege/" + this.college).then(res => {
        this.teachers = res.data
      })
    }else {
      this.request.get("/teacher").then(res => {
        this.teachers = res.data
      })
    }
  },

  methods: {
    load() {
      this.request.get("/classInfo/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          college: this.college,
          className: this.className,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    save() {
      this.request.post("/classInfo", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },

    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}

      // 如果不是管理员，设置默认学院编号
      if (!this.isAdministrator) {
        this.form.collegeNo = this.userCollege;
        this.getTeacherList()
      }
    },

    handleEdit(row) {
      this.form = row
      this.dialogFormVisible = true
    },

    del(id) {
      this.request.delete("/classInfo/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },

    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/classInfo/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      if(this.isAdministrator){
      this.college=""
    }
      this.className = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    getLabel(list, id, value, label) {
      if (id !== '' && Array.isArray(list) && list.length !== 0) {
        return !list.find(item => item[value] === id) ? id : list.find(item => item[value] === id)[label]
      } else {
        return id
      }
    },

    getTeacherList() {
      this.request.get("/teacher/selectByCollege/" + this.form.collegeNo).then(res => {
        this.formTeachers = res.data
      })
    },

    handleCollegeSelectChange() {
      this.performOperation();
      this.getTeacherList();
    },
    handleSelectChange() {
      // 处理每个下拉框的变化
      this.performOperation();
    },

    performOperation() {
      if (this.form.gradeNo && this.form.collegeNo && this.form.majorNo) {
          this.generateClassNo();
      } else {
        console.log('下拉框还未全选！');
      }
    },
    generateClassNo() {
      // 封装成对象
      const requestData = {
        gradeNo: this.form.gradeNo,
        college: this.form.collegeNo,
        major: this.form.majorNo,
      };
      // 发送请求到后台，将 requestData 传递给后台
      this.request.post("/classInfo/generateClassNo", requestData).then(res => {
        // 处理响应
        if (res.code === '200') {
          this.$message.success(res.msg)
          console.log(res.data)
          //赋值到输入框
          this.$set(this.form, 'classNo', res.data)
        }
      });
    }
  }
}
</script>

<style>
.headerBg {
  background: #eee !important;
}
</style>
