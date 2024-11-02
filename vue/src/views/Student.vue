<template >
  <div>
    <div style="margin: 10px 0">
    <el-select  style="width: 200px" v-model="grade"  placeholder="请选择年级" @change="handleGradeChange">
      <el-option v-for="item in grades" :key="item.name" :label="item.name" :value="item.value">
        {{ item.name }}
      </el-option>
    </el-select>

      <el-select  style="width: 200px" v-model="college"  class="ml-5" v-if="isAdministrator" placeholder="请选择学院" @change="handleCollegeChange">
        <el-option v-for="item in colleges" :key="item.name" :label="item.name" :value="item.value">
          {{ item.name }}
        </el-option>
      </el-select>

      <el-select   style="width: 200px" v-model="classNo" class="ml-5" placeholder="请选择班级">
        <el-option v-for="item in selectClassInfos" :key="item.className" :label="item.className" :value="item.classNo">
          {{ item.className }}
        </el-option>
      </el-select>
      <el-input clearable style="width: 200px" placeholder="请输入学号" suffix-icon="el-icon-search" class="ml-5" v-model="studentNo"></el-input>
      <el-input clearable style="width: 200px" placeholder="请输入姓名" suffix-icon="el-icon-s-custom" class="ml-5" v-model="studentName"></el-input>
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

      <el-upload action="http://localhost:8081/student/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess"   :headers="uploadHeaders" style="display: inline-block">
        <el-button type="info" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>
      </el-upload>

      <el-button type="info" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column prop="grade" label="年级" width="80"></el-table-column>
      <el-table-column prop="college" label="学院" width="150"></el-table-column>
      <el-table-column prop="className" label="班级" width="200"></el-table-column>
      <el-table-column prop="studentNo" label="学号" ></el-table-column>
      <el-table-column prop="studentName" label="姓名"></el-table-column>
      <el-table-column prop="telephone" label="电话" ></el-table-column>
      <el-table-column label="操作"  width="200" align="center">
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

    <el-dialog title="学生信息" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="80px" size="small">
        <el-form-item label="年级">
          <el-select clearable v-model="form.gradeNo" placeholder="请选择" style="width: 100%" @change="handleSelectChange">
            <el-option v-for="item in grades" :key="item.name" :label="item.name" :value="item.value">
              {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="学院" v-if="isAdministrator">
          <el-select clearable v-model="form.collegeNo" placeholder="请选择" style="width: 100%"  @change="handleSelectChange">
            <el-option v-for="item in colleges" :key="item.name" :label="item.name" :value="item.value">
              {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="班级">
          <el-select clearable v-model="form.classNo" placeholder="请选择" style="width: 100%" @change="handleClassSelectChange">
            <el-option v-for="item in formClassInfos" :key="item.className" :label="item.className" :value="item.classNo">
              {{ item.className }}
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="学号">
          <el-input v-model="form.studentNo"  disabled autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="form.studentName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话号码">
          <el-input v-model="form.telephone" autocomplete="off"></el-input>
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
  name: "Student",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      studentNo: "",
      studentName: "",
      college:"",
      colleges: [],
      grade:"",
      grades:[],
      classNo:"",
      //编辑新增框班级信息
      formClassInfos:[],
      //查询框班级信息
      selectClassInfos:[],
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      isNewRecord: true, // 默认是新增记录   生成学号标志位

      isAdministrator:false,
      userRole:"",
      userCollege:"",
      token:"",
      uploadHeaders:{"token":JSON.parse(localStorage.getItem("user")).token}
    }
  },

  created() {
    this.load()
    //请求字典数据
    this.request.get("/dict/college").then(res => {
      this.colleges = res.data
    })
    // 请求年级数据的数据
    this.request.get("/dict/grade").then(res => {
      this.grades = res.data
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
      this.college = this.userCollege; //
    }
  },

  methods: {
    load() {
      this.request.get("/student/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          college:this.college,
          grade:this.grade,
          classNo: this.classNo,
          studentNo: this.studentNo,
          studentName: this.studentName,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    //数据导入
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
    }
    ,
    //修改或者保存
    save() {
      this.request.post("/student", this.form).then(res => {
        if (res.code==='200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    //新增
    handleAdd() {
      //清理脏数据
      this.formClassInfos=[]

      this.isNewRecord = true
      this.dialogFormVisible = true
      this.form = {}

      if(!this.isAdministrator){
        this.form.collegeNo = this.userCollege;
      }
    },

    //编辑
    handleEdit(row) {
      //清理脏数据
      this.formClassInfos=[]
      // 将后台返回的数据映射到前端表单
     this.request.get("/student/" + row.id).then(res=>{
       if (res.code==='200') {
         this.form =res.data
       }
     })
      this.isNewRecord = false; // 标记为编辑记录
      this.dialogFormVisible = true
    },

    //删除
    del(id) {
      this.request.delete("/student/" + id).then(res => {
        if (res.code==='200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },

    //多选操作
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },

    //批量删除
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/student/del/batch", ids).then(res => {
        if (res.code==='200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    //下拉框重置
    reset() {
    if(this.isAdministrator){
      this.college=""
    }
      this.grade = ""
      this.classNo=""
      this.studentNo=""
      this.studentName= ""
      this.load()
    },

    exp() {
      const exportUrl = `http://localhost:8081/student/export`;
      // 使用 axios 发起 GET 请求
      axios({
        url: exportUrl,
        method: 'GET',
        responseType: 'blob',  // 设置响应类型为 blob，以便处理文件
        params: {
          college: this.college,
          grade: this.grade,
          classNo: this.classNo,
          studentNo: this.studentNo,
          studentName: this.studentName,
        },
        headers: {
          'token': this.token,  // 携带 token
        },
      })
          .then(response => {
            const contentDisposition = response.headers['content-disposition'];
            const matchName = contentDisposition.split(';')[1].split('filename=')[1].trim();
            const fileName=decodeURIComponent(matchName)
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

    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },

    //新增编辑下拉框处理逻辑
    handleSelectChange() {
      // 若学院，年级变化，班级清空
      this.form.classNo=""
      if(this.isNewRecord){
        //若为新增，学号清空
        this.form.studentNo=""
      }

      this.performOperation();
  },
    //单独处理班级下拉框变化
    handleClassSelectChange(){
      this.performOperation();
    },

    performOperation() {
      // 若选择了年级和学院信息，查询班级信息
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

      // 只有在三个下拉框都选择了值时才执行操作
      if (this.form.gradeNo && this.form.collegeNo && this.form.classNo) {
        // 如果当前是新增模式，执行生成学号操作

        if (this.isNewRecord) {
          this.generateStudentNo();
        }
      } else {
        console.log('下拉框还未全选！');
      }
    },
    //生成学号
      generateStudentNo() {
        // 封装成对象
        const requestData = {
          gradeNo: this.form.gradeNo,
          college: this.form.collegeNo,
          classNo: this.form.classNo,
        };
        // 发送请求到后台，将 requestData 传递给后台
        this.request.post("/student/generateStudentNo", requestData).then(res => {
          // 处理响应
          if (res.code==='200') {
            this.$message.success(res.msg)
            console.log(res.data)
            //this.form.studentNo=res.data
            //赋值到输入框
            this.$set(this.form, 'studentNo', res.data)
          }
        });
      },


    //查询下拉框选择逻辑
    handleGradeChange() {
      // 在这里检查学院是否也选中，如果是，则执行相应逻辑
      if (this.college) {
        this.handleBothSelected();
      }else{
        // 处理选中年级的逻辑
        this.request.get("/classInfo/grade/"+this.grade).then(res => {
          this.selectClassInfos = res.data
        })
      }
      this.classNo=""
    },
    handleCollegeChange() {
      // 处理选中学院的逻辑
      // 在这里检查年级是否也选中，如果是，则执行相应逻辑
      if (this.grade) {
        this.handleBothSelected();
      }else{
        this.request.get("/classInfo/college/"+this.college).then(res => {
          this.selectClassInfos = res.data
        })
      }
      this.classNo=""

    },
    handleBothSelected() {
      // 处理两者都选中的逻辑
      this.request.get("/classInfo/gradeAndCollege", {
        params: {
          grade: this.grade,
          college: this.college,
        }
      }).then(res => {
        this.selectClassInfos = res.data
      })
      this.classNo=""
    }
  }
}
</script>

<style>
.headerBg {
  background: #eee!important;
}
</style>
