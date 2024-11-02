<template>
  <div>
    <div style="margin: 10px 0">
      <el-select style="width: 200px" v-model="teachingBuilding" class="ml-5" placeholder="请选择教学楼">
        <el-option v-for="item in teachingBuildings" :key="item.name" :label="item.name" :value="item.value">
          {{ item.name }}
        </el-option>
      </el-select>
      <el-select style="width: 200px" v-model="classroomType" class="ml-5" placeholder="请选择教室类型">
        <el-option v-for="item in classroomTypes" :key="item.name" :label="item.name" :value="item.value">
          {{ item.name }}
        </el-option>
      </el-select>

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
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="teachingBuildingNo" label="教学楼" width="140">
        <template slot-scope="scope">
          {{ getLabel(teachingBuildings, scope.row.teachingBuildingNo, 'value', 'name') }}
        </template>
      </el-table-column>

      <el-table-column prop="type" label="教室类型">
        <template slot-scope="scope">
          {{ getLabel(classroomTypes, scope.row.classroomType, 'value', 'name') }}
        </template>
      </el-table-column>
      <el-table-column prop="classroomNo" label="教室编号" width="120"></el-table-column>
      <el-table-column prop="classroomName" label="教室名称"></el-table-column>
      <el-table-column prop="capacity" label="教室容量"></el-table-column>

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

    <el-dialog title="教室信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small">
        <el-form-item label="教学楼">
          <el-select clearable v-model="form.teachingBuildingNo" placeholder="请选择" style="width: 100%"
                     @change="handleSelectChange">
            <el-option v-for="item in teachingBuildings" :key="item.name" :label="item.name" :value="item.value">
              {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="教室类型">
          <el-select clearable v-model="form.classroomType" placeholder="请选择" style="width: 100%"
                     @change="handleSelectChange">
            <el-option v-for="item in classroomTypes" :key="item.name" :label="item.name" :value="item.value">
              {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="教室编号">
          <el-input v-model="form.classroomNo" disabled autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="教室名称">
          <el-input v-model="form.classroomName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="教室容量">
          <el-input v-model="form.capacity" autocomplete="off"></el-input>
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
  name: "Classroom",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      teachingBuilding: "",
      teachingBuildings: [],
      classroomType: "",
      classroomTypes: [],
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
    }
  },
  created() {
    this.load()
    this.request.get("/dict/teachingBuilding").then(res => {
      this.teachingBuildings = res.data
    })
    this.request.get("/dict/classroom").then(res => {
      this.classroomTypes = res.data
    })
  },

  methods: {
    load() {
      this.request.get("/classroom/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          teachingBuilding: this.teachingBuilding,
          classroomType: this.classroomType
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    save() {
      this.request.post("/classroom", this.form).then(res => {
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
    },
    handleEdit(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/classroom/" + id).then(res => {
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
      this.request.post("/classroom/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      this.teachingBuilding = ""
      this.classroomType = ""
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

    handleSelectChange() {
      // 处理每个下拉框的变化
      this.performOperation();
    },
    performOperation() {

      // 只有在四个下拉框都选择了值时才执行操作
      if (this.form.teachingBuildingNo && this.form.classroomType) {
          this.generateClassroomNo();
      } else {
        console.log('修改信息');
      }
    },

    generateClassroomNo() {
      // 封装成对象
      const requestData = {
        teachingBuildingNo: this.form.teachingBuildingNo,
        classroomType: this.form.classroomType
      };
      // 发送请求到后台，将 requestData 传递给后台
      this.request.post("/classroom/generateClassroomNo", requestData).then(res => {
        // 处理响应
        if (res.code === '200') {
          this.$message.success(res.msg)
          console.log(res.data)
          //赋值到输入框
          this.$set(this.form, 'classroomNo', res.data)
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
