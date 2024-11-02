<template>
  <div style="margin: 10px 0">
    <div style="margin: 10px 0">
      <el-select style="width: 200px" v-model="role" placeholder="请选择角色">
        <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.value">
          {{ item.name }}
        </el-option>
      </el-select>

      <el-input style="width: 200px" placeholder="请输入用户账号" suffix-icon="el-icon-search" class="ml-5" v-model="userNo"></el-input>

      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
  </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'">
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column  prop="userNo" label=账号  width="150"></el-table-column>
      <el-table-column  prop="username" label="用户名"  width="100"></el-table-column>

      <el-table-column label="用户头像" width="100" align="center">
        <template slot-scope="scope">
          <img :src="scope.row.avatarUrl" alt="Avatar" style="width: 50px; height: 50px; border-radius: 50%;">
        </template>
      </el-table-column>

      <el-table-column prop="role" label="用户角色" width="220" align="center">
        <template slot-scope="scope">
            <el-dropdown trigger="click" style="font-size: 0.85em;">
              <el-button type="primary" icon="el-icon-user" class="c-btn">
                {{ getLabel(roles, scope.row.role, 'value', 'name') || '选择角色' }} <i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
									<span v-for="role in roles" :key="role.value" @click="changeUserRole(scope.row.id,role.value)">
										<el-dropdown-item>{{role.name}}</el-dropdown-item>
									</span>
              </el-dropdown-menu>
            </el-dropdown>

        </template>
      </el-table-column>

      <el-table-column label="重置密码" width="150" align="center">
        <template slot-scope="scope">
          <el-button type="danger" @click="resetPassword(scope.row.id)">重置密码</el-button>
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
  </div>
</template>

<script>
export default {
  name: "User",
  data() {
    return {
      tableData: [],
      role: "",
      roles: [
        {
          name: "超级管理员",
          value: "ROLE_ADMINISTRATOR"
        },
        {
          name: "管理员",
          value: "ROLE_ADMIN"
        },
        {
          name: "教师",
          value: "ROLE_TEACHER"
        },
        {
          name: "学生",
          value: "ROLE_STUDENT"
        }
      ],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      userNo: "",
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/user/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          role: this.role,
          userNo: this.userNo,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    reset() {
      this.role = ""
      this.userNo = ""
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
    changeUserRole(id, role) {      // 在这里添加处理用户选择角色的逻辑
      this.request.get("/user/editUserRole",{
        params: {
          id: id,
          role:role,
        }
      }).then(res => {
        if (res.code === '200') {
          this.$message.success("用户角色修改成功")
          this.load()
        } else {
          this.$message.error("用户角色修改失败")
        }
      })
    },
    resetPassword(id){
      this.request.get("/user/resetPassword/"+id).then(res => {
        if (res.code === '200') {
          this.$message.success("用户密码重置成功")
          this.load()
        } else {
          this.$message.error("用户密码重置失败")
        }
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
