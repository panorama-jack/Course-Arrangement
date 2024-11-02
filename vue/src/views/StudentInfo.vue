<template >
  <div>
    <div style="margin: 10px 0">
      <el-select style="width: 200px" v-model="term" class="ml-5" placeholder="请选择学期" @change="handleSelectChange">
        <el-option v-for="item in terms" :key="item.name" :label="item.name" :value="item.value">
          {{ item.name }}
        </el-option>
      </el-select>

      <el-select   style="width: 200px" v-model="classNo" class="ml-5" placeholder="请选择班级">
        <el-option v-for="item in classInfos" :key="item.className" :label="item.className" :value="item.classNo">
          {{ item.className }}
        </el-option>
      </el-select>

      <el-input clearable style="width: 200px" placeholder="请输入学号" suffix-icon="el-icon-message" class="ml-5" v-model="studentNo"></el-input>
      <el-input clearable style="width: 200px" placeholder="请输入姓名" suffix-icon="el-icon-message" class="ml-5" v-model="studentName"></el-input>

      <el-button class="ml-5" type="primary" @click="load">查询</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
       <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'" >
      <el-table-column prop="grade" label="年级" width="80"></el-table-column>
      <el-table-column prop="college" label="学院" width="150"></el-table-column>
      <el-table-column prop="className" label="班级" width="200"></el-table-column>
      <el-table-column prop="studentNo" label="学号" ></el-table-column>
      <el-table-column prop="studentName" label="姓名"></el-table-column>
      <el-table-column prop="telephone" label="电话" ></el-table-column>
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
      pageSize: 80,
      term:"",
      terms:[],
      classNo:"",
      classInfos:[],

      studentNo: "",
      studentName: "",
      token:"",
      teacherNo:""
    }
  },

  created() {
    this.load()
    //获取字典数据  term  college grade
    this.request.get("/dict/term").then(res => {
      this.terms = res.data
    })

    let userInfo;
    const userString = localStorage.getItem("user");
    if (userString) {
      userInfo = JSON.parse(userString); // 解析本地存储的用户信息
      this.token = userInfo.token;
      this.teacherNo=userInfo.userNo
    }
  },

  methods: {
    load() {
      this.request.get("/student/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          term:this.term,
          classNo: this.classNo,
          studentNo: this.studentNo,
          studentName: this.studentName,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },

    //下拉框重置
    reset() {
      this.term = ""
      this.classNo = ""
      this.studentNo=""
      this.studentName=""
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
          term:this.term,
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
            const fileName = decodeURIComponent(matchName)
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
    handleSelectChange() {
        this.request.get("/schedule/term", {
          params: {
            term: this.term,
            teacherNo: this.teacherNo
          }
        }).then(res => {
          this.classInfos = res.data
        })
      }
    }
}
</script>

<style>
.headerBg {
  background: #eee!important;
}
</style>
