<template>
  <div>
    <div style="margin: 10px 0">
      <el-select style="width: 200px" v-model="term" class="ml-5" placeholder="请选择学期">
        <el-option v-for="item in terms" :key="item.name" :label="item.name" :value="item.value">
          {{ item.name }}
        </el-option>
      </el-select>
      
      <el-select style="width: 200px" v-model="grade" class="ml-5" placeholder="请选择年级"  @change="handleSelectChange">
        <el-option v-for="item in grades" :key="item.name" :label="item.name" :value="item.value">
          {{ item.name }}
        </el-option>
      </el-select>

      <el-select style="width: 200px" v-model="college" class="ml-5"  v-if="isAdministrator" placeholder="请选择学院"  @change="handleSelectChange">
        <el-option v-for="item in colleges" :key="item.name" :label="item.name" :value="item.value">
          {{ item.name }}
        </el-option>
      </el-select>

      <el-select style="width: 200px" v-model="classNo" class="ml-5" placeholder="请选择班级">
        <el-option v-for="item in selectClassInfos" :key="item.className" :label="item.className" :value="item.classNo">
          {{ item.className }}
        </el-option>
      </el-select>

      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
      <el-button class="ml-5" type="primary" v-if="isAdministrator" @click="arrangeCourse()">排课<i class="el-icon-thumb el-icon--right"></i></el-button>
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

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="term" label="学期" width="100"></el-table-column>

      <el-table-column prop="collegeNo" label="学院" width="120">
        <template slot-scope="scope">
          {{ getLabel(colleges, scope.row.collegeNo, 'value', 'name')}}
        </template>
      </el-table-column>

      <el-table-column prop="gradeNo" label="年级">
        <template slot-scope="scope">
          {{ getLabel(grades, scope.row.gradeNo, 'value', 'name')}}
        </template>
      </el-table-column>

      <el-table-column prop="classNo" label="班级"  width="180">
        <template slot-scope="scope">
          {{ getLabel(classInfos, scope.row.classNo, 'classNo', 'className')}}
        </template>
      </el-table-column>

      <el-table-column prop="courseNo" label="课程"   width="180">
        <template slot-scope="scope">
          {{ getLabel(courseInfos, scope.row.courseNo, 'courseNo', 'courseName')}}
        </template>
      </el-table-column>

      <el-table-column prop="weekTime" label="周学时" width="60"></el-table-column>
      <el-table-column prop="weeks" label="周数"  width="50"></el-table-column>

      <el-table-column prop="isFix" label="时间固定">
        <template slot-scope="scope">
          {{ getFixLabel(scope.row.isFix) }}
        </template>
      </el-table-column>

      <el-table-column prop="courseTime" label="上课时间"></el-table-column>
      <el-table-column label="操作"  width="180" align="center">
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

    <el-dialog title="课程计划" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="80px" size="small">

        <el-form-item label="学期">
          <el-select clearable v-model="form.term" placeholder="请选择" style="width: 100%" >
            <el-option v-for="item in terms" :key="item.name" :label="item.name" :value="item.value">
              {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="学院" v-if="isAdministrator">
          <el-select clearable v-model="form.collegeNo" placeholder="请选择" style="width: 100%"  @change="handleSelectChange">
            <el-option v-for="item in colleges" :key="item.name" :label="item.name" :value="item.value" >
              {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="年级">
          <el-select clearable v-model="form.gradeNo" placeholder="请选择" style="width: 100%" @change="handleSelectChange">
            <el-option v-for="item in grades" :key="item.name" :label="item.name" :value="item.value">
              {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="班级">
          <el-select clearable v-model="form.classNo" placeholder="请选择" style="width: 100%">
            <el-option v-for="item in formClassInfos" :key="item.className" :label="item.className" :value="item.classNo">
              {{ item.className}}
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="课程">
          <el-select clearable v-model="form.courseNo"  filterable placeholder="请选择" style="width: 100%">
            <el-option v-for="item in courseInfos" :key="item.courseName" :label="item.courseName" :value="item.courseNo">
              {{ `${item.courseName}  ${item.teacherName}` }}
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="周学时">
          <el-input v-model="form.weekTime" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="周数">
          <el-input v-model="form.weeks" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="时间固定">
          <label>是<input type="radio" v-model="form.isFix" value=1></label>
          <label>否<input type="radio" v-model="form.isFix" value=0></label>
        </el-form-item>

        <el-form-item label="上课时间">
          <el-input v-model="form.courseTime" autocomplete="off" :disabled="form.isFix == 0"></el-input>
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
  name: "CoursePlan",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      term:"",
      terms:[],
      college:"",
      colleges:[],
      grade:"",
      grades:[],
      classNo:"",
      classInfo:"",
      classInfos:[],
      selectClassInfos:[],
      formClassInfos:[],
      courseInfos:[],
      form: { },
      dialogFormVisible: false,
      multipleSelection: [],

      isAdministrator:false,
      userRole:"",
      userCollege:""
    }
  },
  created() {
    this.load()
    this.request.get("/dict/term").then(res => {
      this.terms = res.data
    })
    this.request.get("/dict/college").then(res => {
      this.colleges = res.data
    })
    this.request.get("/dict/grade").then(res => {
      this.grades = res.data
    })
    this.request.get("/courseInfo/course").then(res => {
      this.courseInfos = res.data
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
      this.request.get("/classInfo/college/"+this.college).then(res => {
        this.classInfos = res.data
      })
    }else{
      this.request.get("/classInfo").then(res => {
        this.classInfos = res.data
      })
    }
  },

  methods: {
    load() {
      this.request.get("/coursePlan/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          term: this.term,
          grade: this.grade,
          college: this.college,
          classNo: this.classNo,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },

    save() {
      this.request.post("/coursePlan/", this.form).then(res => {
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
      this.formClassInfos=[]
      this.dialogFormVisible = true
      this.form = { isFix: 0 }//  默认为0不选中
      if(!this.isAdministrator){
        this.form.collegeNo = this.userCollege;
      }
    },
    handleEdit(row) {
      this.formClassInfos=[]
      this.form = row
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/coursePlan/" + id).then(res => {
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
      this.request.post("/coursePlan/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },

    // 点击开始提交学期到系统后台排课
    arrangeCourse() {
      if (this.term == "") {
        alert("请选择学期");
      } else {
        this.request.get("/coursePlan/arrange/" + this.term)
            .then(res => {
              if (res.code === '200') {
                this.$message({message: res.msg, type: 'success'})
                this.$router.push('/Schedule')
              } else {
                this.$message.error(res.msg)
              }
            })
            .catch(error => {
              this.$message.error('排课失败')
            })
      }
    },

    reset() {
      if(this.isAdministrator){
        this.college=""
      }
      this.term = ""
      this.grade = ""
      this.classNo = ""
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
    getFixLabel(isFix) {
      return isFix == 1 ? "是" : "否";   //因为 == 会进行类型转换，而 === 不会
    },
    handleSelectChange() {
      // 处理每个下拉框的变化
      this.performOperation();
    },

    performOperation() {
      // 若选择了年级和学院信息，查询班级信息      表单中的下拉框
      if (this.form.gradeNo && this.form.collegeNo) {
        this.request.get("/classInfo/gradeAndCollege", {
          params: {
            grade:this.form.gradeNo,
            college: this.form.collegeNo
          }
        }).then(res => {
          this.formClassInfos = res.data
        })
      }

      //查询按钮的下拉框数据
      if (this.grade && this.college) {
        this.request.get("/classInfo/gradeAndCollege",{
          params: {
            grade: this.grade,
            college: this.college,
          }
        }).then(res => {
          this.selectClassInfos = res.data
        })
      }
    }
  }
}

</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>
