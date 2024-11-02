<template>
  <div class="wrapper">
    <div class="login-container">
      <div class="login-header">登 录</div>
      <el-form :model="user" :rules="rules" ref="userForm" class="login-form">
        <el-form-item prop="userNo">
          <el-input size="medium" class="input-field" prefix-icon="el-icon-user" v-model="user.userNo"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" class="input-field" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item class="login-button">
          <el-button type="primary" size="medium" @click="login">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import {setRoutes} from "@/router";
export default {
  name: "Login",
  data() {
    return {
      user: {
        userNo:"",
        password:""
      },
      rules: {
        userNo: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 5, max: 14, message: '长度在 5到 14 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 1, max: 20, message: '长度在 6到 20 个字符', trigger: 'blur' }
        ],
      },
      loading: false, // 添加 loading 状态
    }
  },
  methods: {
    login() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          this.loading = true; // 开启 loading 状态

          this.request.post("/user/login", this.user).then(res => {
            if (res.code === '200') {
              localStorage.setItem("user", JSON.stringify(res.data))  // 存储用户信息到浏览器
              // 动态设置当前用户的路由
              setRoutes()
              this.$router.push("/")
              this.$message.success("登录成功")
            } else {
              this.$message.error(res.msg)
            }
          }).finally(() => {
            this.loading = false; // 关闭 loading 状态
          });
        }
      });
    }
  }
}
</script>


<style>
.wrapper {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-image: url('../../../upload/image.svg'); /* Replace with your image URL */
  background-size: cover;
  overflow: hidden;
}

.login-container {
  background-color: rgba(255, 255, 255, 0.9);
  width: 350px;
  height: 300px;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.2);
}

.login-header {
  margin: 20px 0;
  text-align: center;
  font-size: 24px;
  font-weight: bold;
}

.login-form {
  margin-top: 20px;
}

.input-field {
  margin: 10px 0;
}

.login-button {
  margin: 10px 0;
  text-align: right;
}
</style>