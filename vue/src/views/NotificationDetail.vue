<template>
  <div style="color: #666">
    <div style="margin: 20px 0; ">
      <div class="pd-10" style="font-size: 20px; color: #3F5EFB; cursor: pointer">{{ notification.title }}</div>
      <div style="font-size: 14px; margin-top: 10px">
        <i class="el-icon-user-solid"></i> <span>{{ userName }}</span>
        <i class="el-icon-time" style="margin-left: 10px"></i> <span>{{ notification.time }}</span>
      </div>
    </div>
    <div style="margin: 20px 0">
      <mavon-editor
          class="md"
          :value="notification.content"
          :subfield="false"
          :defaultOpen="'preview'"
          :toolbarsFlag="false"
          :editable="false"
          :scrollStyle="true"
          :ishljs="true"/>
    </div>
  </div>
</template>

<script>
export default {
  name: "Notification",
  data() {
    return {
      notification: {},
      userName: "",
      dialogFormVisible: false
    };
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      // 通过 `vm` 访问组件实例
      vm.loadNotification(to.params.id);
    });
  },
  beforeRouteUpdate(to, from, next) {
    // 当路由更新时加载通知数据
    this.loadNotification(to.params.id);
    next();
  },

  methods: {
    async loadNotification(id) {
      try {
        const res = await this.request.get("/notification/" + id);
        this.notification = res.data;

        // 获取用户名
        if (this.notification.user) {
          const userNameResponse = await this.getUserName(this.notification.user);
          this.userName = userNameResponse.code === '200' ? userNameResponse.data : this.notification.user;
        }
      } catch (error) {
        console.error('加载通知数据失败', error);
      }
    },

    async getUserName(userNo) {
      try {
        const response = await this.request.get(`/teacher/selectByTeacherNo/${userNo}`);
        return response;
      } catch (error) {
        console.error('获取用户名失败', error);
        return { code: '500', data: userNo };  //返回一个默认值或者错误信息
      }
    }
  }
};
</script>

<style scoped>

</style>
