<template>
  <div>
    <div style="margin: 10px 0">
      <el-select style="width: 200px" v-model="college" class="ml-5"   v-if="isAdministrator"  placeholder="请选择开课学院">
        <el-option v-for="item in colleges" :key="item.name" :label="item.name" :value="item.value">
          {{ item.name }}
        </el-option>
      </el-select>

      <el-select style="width: 200px" v-model="courseAttribute" class="ml-5" placeholder="请选择课程属性">
        <el-option v-for="item in courseAttributes" :key="item.name" :label="item.name" :value="item.value">
          {{ item.name }}
        </el-option>
      </el-select>

      <el-input style="width: 200px" placeholder="请输入课程名称" suffix-icon="el-icon-search" class="ml-5"
                v-model="courseName"></el-input>
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
      <el-upload action="http://localhost:8081/courseInfo/import" :show-file-list="false" accept="xlsx"
                 :on-success="handleExcelImportSuccess" :headers="uploadHeaders" style="display: inline-block">
        <el-button type="info" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="info" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column label="开课学院">
        <template slot-scope="scope">
          {{ getLabel(colleges, scope.row.collegeNo, 'value', 'name') }}
        </template>
      </el-table-column>

      <el-table-column prop="courseAttribute" label="课程属性">
        <template slot-scope="scope">
          {{ getLabel(courseAttributes, scope.row.courseAttribute, 'value', 'name') }}
        </template>
      </el-table-column>

      <el-table-column prop="courseNo" label="课程编号" width="120"></el-table-column>
      <el-table-column prop="courseName" label="课程名称"></el-table-column>

      <el-table-column prop="teacherNo" label="上课教师">
        <template slot-scope="scope">
          {{ getLabel(teachers, scope.row.teacherNo, 'teacherNo', 'teacherName') }}
        </template>
      </el-table-column>

      <el-table-column prop="classroomType" label="教室类型">
        <template slot-scope="scope">
          {{ getLabel(classrooms, scope.row.classroomType, 'value', 'name') }}
        </template>

      </el-table-column>
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

    <el-dialog title="课程信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small">

        <el-form-item label="开课学院"  v-if="isAdministrator">
          <el-select clearable v-model="form.collegeNo" placeholder="请选择" style="width: 100%"
                     @change="handleCollegeSelectChange">
            <el-option v-for="item in colleges" :key="item.name" :label="item.name" :value="item.value">
              {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="课程属性">
          <el-select clearable v-model="form.courseAttribute" placeholder="请选择" style="width: 100%"
                     @change="handleCourseAttributeSelectChange">
            <el-option v-for="item in courseAttributes" :key="item.name" :label="item.name" :value="item.value">
              {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="课程编号">
          <el-input v-model="form.courseNo" disabled autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="课程名称">
          <el-input v-model="form.courseName" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="上课教师">
          <el-select clearable v-model="form.teacherNo" placeholder="请选择" style="width: 100%">
            <el-option v-for="item in formTeachers" :key="item.teacherName" :label="item.teacherName"
                       :value="item.teacherNo">
              {{ item.teacherName }}
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="教室类型">
          <el-select clearable v-model="form.classroomType" placeholder="请选择" style="width: 100%">
            <el-option v-for="item in classrooms" :key="item.name" :label="item.name" :value="item.value">
              {{ item.name }}
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
import axios from "axios";

export default {
  name: "CourseInfo",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      college: "",
      colleges: [],
      courseAttribute: "",
      courseAttributes: [],
      teachers: [],
      formTeachers: [],
      classrooms: [],
      courseName: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      isAdministrator:false,
      userRole:"",
      userCollege:"",
      token:"",
      uploadHeaders:{"token":JSON.parse(localStorage.getItem("user")).token}
    }
  },
  created() {
    this.load()
    this.request.get("/dict/college").then(res => {
      this.colleges = res.data
    })
    this.request.get("/dict/courseAttribute").then(res => {
      this.courseAttributes = res.data
    })
    this.request.get("/dict/classroom").then(res => {
      this.classrooms = res.data
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
      this.request.get("/courseInfo/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          college: this.college,
          courseAttribute: this.courseAttribute,
          courseName: this.courseName,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },

    //数据导出
    exp() {
      const exportUrl = `http://localhost:8081/courseInfo/export`;
      // 使用 axios 发起 GET 请求
      axios({
        url: exportUrl,
        method: 'GET',
        responseType: 'blob',  // 设置响应类型为 blob，以便处理文件
        params: {
          college: this.college,
          courseAttribute: this.courseAttribute,
          courseName: this.courseName,
        },
        headers: {
          'token': this.token,  // 携带 token
        },
      }).then(response => {
            const contentDisposition = response.headers['content-disposition'];
            const matchName = contentDisposition.split(';')[1].split('filename=')[1].trim();
            const fileName = decodeURIComponent(matchName);
            const downloadUrl = URL.createObjectURL(new Blob([response.data]));

            // 创建一个下载链接
            const link = document.createElement('a');
            link.href = downloadUrl;
            link.setAttribute('download', fileName);
            document.body.appendChild(link);

            // 触发点击链接的操作
            link.click();
            // 清理
            document.body.removeChild(link);
          })
          .catch(error => {
            // 处理错误
            console.error('导出请求失败:', error);
          });
    },

    handleExcelImportSuccess(response) {
      // 在这里处理上传成功后的逻辑
      if (response.code === '200') {
        // 在页面上显示成功导入的数据条数，
        this.$message.success(response.msg);
      } else {
        // 处理上传失败的情况
        this.$message.error(response.msg);
      }
      this.load()
    },

    save() {
      this.request.post("/courseInfo", this.form).then(res => {
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
      this.request.delete("/courseInfo/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/courseInfo/del/batch", ids).then(res => {
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
      this.courseAttribute = ""
      this.courseName = ""
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
    async getTeacherList() {
      this.request.get("/teacher/selectByCollege/" + this.form.collegeNo).then(res => {
        this.formTeachers = res.data
      })
    },

    handleCollegeSelectChange() {
      // 处理每个下拉框的变化
      this.performOperation();
      this.getTeacherList();
    },

    handleCourseAttributeSelectChange() {
      // 处理每个下拉框的变化
      this.performOperation();
    },

    performOperation() {
      if (this.form.collegeNo && this.form.courseAttribute) {
          this.generateCourseNo();
      } else {
        console.log('下拉框还未全选！');
      }
    },
    generateCourseNo() {
      const requestData = {
        college: this.form.collegeNo,
        courseAttribute: this.form.courseAttribute,
      };
      // 发送请求到后台，将 requestData 传递给后台
      this.request.post("/courseInfo/generateCourseNo", requestData).then(res => {
        // 处理响应
        if (res.code === '200') {
          this.$message.success(res.msg)
          console.log(res.data)
          this.$set(this.form, 'courseNo', res.data)
        }
      });
    },
  }
}
</script>


<style>
.headerBg {
  background: #eee !important;
}
</style>
