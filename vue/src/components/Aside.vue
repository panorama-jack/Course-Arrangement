<template>
  <el-menu :default-openeds="['1', '3']" style="min-height: 100%; overflow-x: hidden"
           background-color="rgb(45, 45, 45)"
           text-color="#fff"
           active-text-color="#ffd04b"
           :collapse-transition="false"
           :collapse="isCollapse"
           router>
    <div style="height: 60px; line-height: 60px; text-align: center">

      <img src="../assets/logo.png" alt="" style="width: 20px; position: relative; top: 5px; right: 5px">
      <b style="color: white" v-show="logoTextShow">{{ menuTitle }}</b>
    </div>
    <div v-for="item in menus" :key="item.id">
      <div v-if="item.path">
        <el-menu-item :index="item.path">
          <i :class="item.icon"></i>
          <span slot="title">{{ item.name }}</span>
        </el-menu-item>
      </div>
      <div v-else>
        <el-submenu :index="item.id + ''">
          <template slot="title">
            <i :class="item.icon"></i>
            <span slot="title">{{ item.name }}</span>
          </template>
          <div v-for="subItem in item.children" :key="subItem.id">
            <el-menu-item :index="subItem.path">
              <i :class="subItem.icon"></i>
              <span slot="title">{{ subItem.name }}</span>
            </el-menu-item>
          </div>
        </el-submenu>
      </div>
    </div>
  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  props: {
    isCollapse: Boolean,
    logoTextShow: Boolean
  },
  data() {
    return {
      menus: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).menus : [],
    }
  },

  computed: {
    // 根据用户类型动态设置菜单标题
    menuTitle() {
      let userInfo;
      const userString = localStorage.getItem("user");
      if (userString) {
        userInfo = JSON.parse(userString);
        // 解析本地存储的用户信息
        return ["ROLE_ADMINISTRATOR", "ROLE_ADMIN"].includes(userInfo.role) ? "排课系统" : "课表查询系统";
      } else {
        // 处理无法获取用户信息的情况
        return "尚未登录";
      }
    }
  }
}

</script>

<style scoped>
/*解决收缩菜单文字不消失问题*/
.el-menu--collapse span {
  visibility: hidden;
}
</style>
