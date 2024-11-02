<template>
  <div>
    <div style="margin: 10px 0">
      <el-select style="width: 200px" v-model="college" v-if="isAdministrator" placeholder="请选择学院">
        <el-option v-for="item in colleges" :key="item.name" :label="item.name" :value="item.value">
          {{ item.name }}
        </el-option>
      </el-select>

      <el-input style="width: 200px" placeholder="请输入工号" suffix-icon="el-icon-search"  class="ml-5" v-model="teacherNo"></el-input>
      <el-input style="width: 200px" placeholder="请输入姓名" suffix-icon="el-icon-s-custom" class="ml-5" v-model="teacherName"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus-outline"></i></el-button>
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
      <el-upload action="http://localhost:8081/teacher/import" :show-file-list="false" accept="xlsx" :headers="uploadHeaders" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="info" class="ml-5">导入<i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="info" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="college" label="学院" width="120"></el-table-column>
      <el-table-column prop="teacherNo" label="工号" width="140"></el-table-column>
      <el-table-column prop="teacherName" label="姓名" width="120"></el-table-column>
      <el-table-column prop="profession" label="职称" width="120"></el-table-column>
      <el-table-column prop="telephone" label="电话"></el-table-column>
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

    <el-dialog title="教师信息" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="80px" size="small">
        <el-form-item label="学院" v-if="isAdministrator">
          <el-select v-model="form.collegeNo" placeholder="请选择" style="width: 100%"  @change="handleSelectChange">
            <el-option v-for="item in colleges" :key="item.name" :label="item.name" :value="item.value">
              {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="工号">
          <el-input v-model="form.teacherNo"  disabled autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="姓名">
          <el-input v-model="form.teacherName" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="职称">
          <el-select clearable v-model="form.profession" placeholder="请选择" style="width: 100%">
            <el-option v-for="item in profession" :key="item.label" :label="item.label" :value="item.value">
              {{ item.label }}
            </el-option>
          </el-select>
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
  name: "Teacher",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      teacherNo: "",
      teacherName: "",
      college:"",
      colleges: [],
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      isNewRecord: true,
      profession: [
        {
          label: "教授",
          value: "1"
        },
        {
          label: "副教授",
          value: "2"
        },
        {
          label: "讲师",
          value: "3"
        },
        {
          label: "助教",
          value: "4"
        }
      ],

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

  },

  //用户角色和学院获取
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
    }
  },

  methods: {
    load() {
      this.request.get("/teacher/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          college: this.college,
          teacherNo: this.teacherNo,
          teacherName: this.teacherName,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    exp() {
      const exportUrl = `http://localhost:8081/teacher/export`;

      // 使用 axios 发起 GET 请求
      axios({
        url: exportUrl,
        method: 'GET',
        responseType: 'blob',  // 设置响应类型为 blob，以便处理文件
        params: {
          college: this.college,
          teacherNo: this.teacherNo,
          teacherName: this.teacherName,
        },
        headers: {
          'token': this.token,  // 携带 token
        },
      })
          .then(response => {
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
      this.request.post("/teacher", this.form).then(res => {
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
      this.isNewRecord = true;
      this.dialogFormVisible = true
      this.form = {}
      if(!this.isAdministrator){
        this.form.collegeNo = this.userCollege;
        this.generateTeacherNo()
      }
    },

    //编辑
    handleEdit(row) {
      // 将后台返回的数据映射到前端表单
      this.request.get("/teacher/" + row.id).then(res=>{
            if (res.code==='200') {
              this.form =res.data
            }
          }
      )
      this.isNewRecord = false; // 标记为编辑记录
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/teacher/" + id).then(res => {
        if (res.code==='200') {
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
      this.request.post("/teacher/del/batch", ids).then(res => {
        if (res.code==='200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      if(this.isAdministrator){   //是系统管理员才可以清空
        this.college=""
      }
      this.teacherNo = ""
      this.teacherName = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    formatEmployment (type) {
      // 根据源数据经过处理 把处理之后的数据返回
      // 定义一个映射结构
      const map = {
        1: '教授',
        2: '副教授',
        3: '讲师',
        4: '助教'
      }
      return map[type]
    },
    //学院下拉框变化
    handleSelectChange() {
      // 处理每个下拉框的变化
      // 如果当前是新增模式，执行生成工号操作
      if (this.isNewRecord) {
        this.generateTeacherNo();
      }
     else {
      console.log('修改信息');
    }
    },

    generateTeacherNo() {
      // 封装成对象
      const requestData = {
        college: this.form.collegeNo
      };

      // 发送请求到后台，将 requestData 传递给后台
      this.request.post("/teacher/generateTeacherNo", requestData).then(res => {
        // 处理响应
        if (res.code==='200') {
          this.$message.success(res.msg)
          console.log(res.data)
          this.$set(this.form, 'teacherNo', res.data)
        }
      });
    },
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>
