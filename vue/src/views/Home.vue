<template>
  <div style="color: #666">
    <div style="margin: 10px 0">
      <el-select  style="width: 200px" v-model="college"  class="ml-5" v-if="isAdministrator" placeholder="请选择学院" >
        <el-option v-for="item in colleges" :key="item.name" :label="item.name" :value="item.value">
          {{ item.name }}
        </el-option>
      </el-select>
      <el-input style="width: 200px" placeholder="请输入名称"  class="ml-5" suffix-icon="el-icon-search" v-model="title"></el-input>
      <el-button class="ml-5" type="primary" @click="load" size="small">搜索</el-button>
      <el-button type="warning" @click="reset" size="small">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <div style="padding: 10px 0; border-bottom: 1px dashed #ccc" v-for="item in tableData" :key="item.id">
        <div class="pd-10" style="font-size: 20px; color: #3F5EFB; cursor: pointer" @click="goToNotificationDetail(item.id)">{{ item.title }}</div>
        <div style="font-size: 14px; margin-top: 10px">
          <i class="el-icon-user-solid"></i> <span>{{ userNames[item.user] }}</span>
          <i class="el-icon-time" style="margin-left: 10px"></i> <span>{{ item.time }}</span>
        </div>
      </div>
    </div>

    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, prev, pager, next"
          :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>

export default {
  name: "Home",
  data() {
    return {
      tableData: [],
      title: '',
      pageNum: 1,
      pageSize: 10,
      total: 0,
      college:"",
      colleges: [],
      isAdministrator:false,
      userRole:"",
      userCollege:"",
      userNames: {}, // 添加 userNames 对象用于存储用户名
    }
  },

  created() {
    this.load()
    this.request.get("/dict/college").then(res => {
      this.colleges = res.data
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
    }

  },
  methods: {
    async load() {
      try {
        const res = await this.request.get("/notification/page", {
          params: {
            title: this.title,
            college: this.college,
            pageNum: this.pageNum,
            pageSize: this.pageSize,
          }
        });

        this.tableData = res.data.records;
        this.total = res.data.total;
        // 确保数据加载后再获取用户名
        await this.getUserNames();
      } catch (error) {
        console.error('加载数据失败', error);
      }
    },

    reset() {
      if (this.isAdministrator) {
        this.college = ""
      }
      this.title = ""
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
    goToNotificationDetail(id) {
      console.log(id)
      // 例如，在点击标题时导航到通知详情
      this.$router.push(`/notificationDetail/${id}`);
    },

    async getUserNames() {
      for (const item of this.tableData) {
        this.$set(this.userNames, item.user, await this.getUserName(item.user));
      }
    },

    async getUserName(userNo) {
      try {
        const response = await this.request.get(`/teacher/selectByTeacherNo/${userNo}`);
        if (response.code === '200') {
          return response.data
        } else {
          return userNo
        }
      } catch (error) {
        console.error('获取用户名失败', error)
        return userNo
      }
    }
  }
}
</script>

<style scoped>

</style>
