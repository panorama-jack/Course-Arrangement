<template>
  <div>
    <div style="margin: 10px 0">
      <el-select style="width: 200px" v-model="term" class="ml-5" placeholder="请选择学期">
        <el-option v-for="item in terms" :key="item.name" :label="item.name" :value="item.value">
          {{ item.name }}
        </el-option>
      </el-select>

      <el-select style="width: 200px" v-model="college" class="ml-5" placeholder="请选择学院" v-if="isAdministrator" @change="handleSelectChange">
        <el-option v-for="item in colleges" :key="item.name" :label="item.name" :value="item.value">
          {{ item.name }}
        </el-option>
      </el-select>

      <el-select style="width: 200px" v-model="grade" class="ml-5" placeholder="请选择年级"  v-if="isAdministrator||isAdmin" @change="handleSelectChange">
        <el-option v-for="item in grades" :key="item.name" :label="item.name" :value="item.value" >
          {{ item.name }}
        </el-option>
      </el-select>

      <el-select style="width: 200px" v-model="classNo" class="ml-5" placeholder="请选择班级" v-if="isAdministrator||isAdmin">
        <el-option v-for="item in classInfos" :key="item.className" :label="item.className" :value="item.classNo">
          {{ item.className }}
        </el-option>
      </el-select>

      <el-button class="ml-5" type="primary" @click="querySchedule">查询课表</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
      <el-button type="info" v-if="isAdministrator||isAdmin" @click="editSchedule">微调课表</el-button>
    </div>

    <div class="table-wrapper">
      <div class="tabel-container">
        <table>
          <!--表头-->
          <thead>
          <tr>
            <th>时间</th>
            <th
                v-for="(weekNum, weekIndex) in courseTableData.courses.length"
                :key="weekIndex">
              <!-- 第一行渲染-->
              {{ "周" + digitalToChinese(weekIndex + 1, "week") }}
            </th>
          </tr>
          </thead>
          <!-- 表体 -->
          <tbody>
          <!--五行-->
          <tr
              v-for="(lesson, lessonIndex) in courseTableData.lessons"
              :key="lessonIndex">
            <!--第一列-->
            <td>
              <p>{{ "第" + digitalToChinese(lessonIndex + 1) + "节" }}</p> <!--显示当前时间段是第几节课，通过digitalToChinese方法将课时数字转换为中文表示。-->
              <p class="period">{{ lesson }}</p>
            </td>
            <!--五列-->
            <td
                v-for="(course, courseIndex) in courseTableData.courses"
                :key="courseIndex">
              {{ courseTableData.courses[courseIndex][lessonIndex] || "-" }}
            </td>
            <!-- v-for="(course, courseIndex) in courseTableData.courses": 使用v-for指令循环渲染五列，每一列代表一天的课程情况。
            :key="courseIndex": 为每列设置唯一的键值，以便Vue能够正确地更新和跟踪每列的状态。
            {{ courseTableData.courses[courseIndex][lessonIndex] || "-" }}: 显示具体的课程信息。如果某个时间段没有课程，则显示占位符“-”。
            整个表格的结构是基于Vue.js的数据驱动，courseTableData 对象中包含了时间段信息(lessons)和具体的课程安排(courses)。该模板通过Vue的循环指令和数据绑定，动态地渲染出完整的课程表格。在表格中，每一行代表一个时间段，每一列代表一天，以及在第一列中显示当前时间段是第几节课的信息。-->
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <el-dialog title="课表微调" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small">
        <el-form-item label="课程名称">
          <el-select clearable v-model="form.id" placeholder="请选择" style="width: 100%"  @change="handleFormSelectChange">
            <el-option v-for="item in courseData" :key="item.id" :label="item.courseName" :value="item.id">
              {{  `${item.courseName}  ${item.courseTime}`}}
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="上课时间">
          <el-select clearable v-model="form.courseTime" placeholder="请选择" style="width: 100%">
            <el-option v-for="item in courseTimeList" :value="item" :key="item">
              {{ item }}
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="上课教室">
          <el-select v-model="form.classroomNo"  placeholder="请选择" style="width: 100%">
          <el-option v-for="item in classroomList" :key="item.classroomNo" :label="item.classroomName" :value="item.classroomNo">
            {{ item.classroomName }}
          </el-option>
          </el-select>
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
  name: "Schedule",
  data() {
    return {
      term:"",
      terms:[],
      college:"",
      colleges:[],
      grade:"",
      grades:[],
      classNo:"",
      classInfos:[],

      isTeacher:false,
      isStudent:false,
      isAdministrator:false,
      isAdmin:false,
      userRole:"",
      userCollege:"",
      userNo:"",

      courseTableData: {
        lessons: [
          "08.00-9.40",
          "10.00-11.50",
          "14.00-15.40",
          "16.00-17.50",
          "19.00-21.00",
        ],
        courses: [
          [],
          [],
          [],
          [],
          [],
        ],
      },

      dialogFormVisible: false,
      form: {},
      courseTimeList:[],
      classroomList:[],

      courseData:[]


    }
  },
  created() {
    //获取字典数据  term  college grade
    this.request.get("/dict/term").then(res => {
      this.terms = res.data
    })
    this.request.get("/dict/college").then(res => {
      this.colleges = res.data
    })
    this.request.get("/dict/grade").then(res => {
      this.grades = res.data
    })
  },

  async mounted() {
    let userInfo;
    const userString = localStorage.getItem("user");

    if (userString) {
      userInfo = JSON.parse(userString); // 解析本地存储的用户信息
      this.token = userInfo.token
      this.userNo=userInfo.userNo
      try {
        const res = await this.request.get("/user/getUserRoleAndCollege/" + userInfo.userNo);
        this.userCollege = res.data.college;
        this.userRole = res.data.role;

        if (this.userRole === "ROLE_ADMINISTRATOR") {
          this.isAdministrator = true
        }else if(this.userRole === "ROLE_ADMIN"){
          this.isAdmin = true
          this.college = this.userCollege;
        } else if(this.userRole === "ROLE_TEACHER"){
           this.isTeacher=true
        }else{
          this.isStudent = true
          const res = await this.request.get("/student/getClassNoByStudentNo/" + this.userNo);
          this.classNo=res.data
        }
      } catch (error) {
        console.error('请求失败', error);
      }
    }
  },

  methods: {
    //重置查询框
    reset() {
      this.term = ""
      if(this.isAdministrator){
        this. college= ""
      }
      if(this.isAdministrator||this.isAdmin){  //系统管理员和管理员有年级和班级的下拉框
        this. grade= ""
        this. classNo= ""
      }

      //清空表格数据
      this.courseTableData.courses.map((item, index) => {
        this.courseTableData.courses[index].splice(
            0,
            this.courseTableData.courses[index].length
        );
      });
    },

    querySchedule() {
      //这段代码的作用是清空 courseTableData.courses 数组中每个元素（每行）的内容，通过将每个行的数组使用 splice 方法清空，将其长度设置为 0。这样做的目的可能是在重新填充新的数据之前，先清空已有的数据，确保 courseTableData.courses 是一个空数组。
      this.courseTableData.courses.map((item, index) => {
        this.courseTableData.courses[index].splice(
            0,
            this.courseTableData.courses[index].length
        );
      });

       // 判断是否选择了 term 和 classNo
      if (this.userRole === "ROLE_ADMINISTRATOR" || this.userRole === "ROLE_ADMIN") {
        // 如果是管理员或系统管理员，需要判断两个条件
        if (!this.term || !this.classNo) {
          this.$message.error("请先选择学期和班级");
          return;
        }
      } else {
        // 如果是学生或老师，只需要判断学期
        if (!this.term) {
          this.$message.error("请先选择学期");
          return;
        }
      }

      // 老师的课表请求地址和其他角色不一样
      const getRequestUrl = (isTeacher) => {
        return isTeacher ? "http://localhost:8081/schedule/teacher" : "http://localhost:8081/schedule";
      };

      // 获取请求地址
      const requestUrl = getRequestUrl(this.isTeacher);

      // 根据用户类型动态构造请求参数   参数也不一样
      const getRequestParams = (isTeacher) => {
        return isTeacher ? { term: this.term, teacherNo: this.userNo } : { term: this.term, classNo: this.classNo };
      };

    // 获取请求参数
      const requestParams = getRequestParams(this.isTeacher);
      this.request.get(requestUrl, {
      params: requestParams})
          .then((res) => {
            this.courseData = res.data;
            let level = 0;
            let times = 0;
            for (let index = 0; index < this.courseData.length; index++) {
              //时间段计数器
              times = times + 1;
              //第index个数据
              const item = this.courseData[index];
              if (parseInt(item.courseTime) !== times) {
                // 如果课程时间不匹配当前时间，填充空白
                this.courseTableData.courses[level].push("");
                index = index - 1;  // 回退一步，重新处理当前数据
              } else {
                // 将课程信息添加到对应的时间段
                this.courseTableData.courses[level].push(
                    item.courseName  +" --"+(this.isTeacher ? item.className : item.teacherName) +"--"+  item.classroomName
                );
              }
              if (times % 5 == 0) {
                // 当处理了5个时间段后，切换到下一列
                level = level + 1;
              }
            }
            // 显示成功消息
            this.$message({ message: "查询成功", type: "success" });
          })
          .catch((error) => {
            // 处理Ajax请求失败的情况
            this.$message.error("查询失败",error);
          });
    },

    /**
     * 数字转中文
     * @param {Number} num 需要转换的数字
     * @param {String} identifier 标识符
     * @returns {String} 转换后的中文
     *
     * 考虑了一个标识符（identifier）用于指定特定的转换规则
     * 如果标识符是 "week" 并且数字是 0 或 7，则返回 "日"。
     否则，根据数字在 character 数组中找到相应的中文字符进行返回。
     */
    digitalToChinese(num, identifier) {
      // 定义中文字符数组
      const character = [
        "零",
        "一",
        "二",
        "三",
        "四",
        "五",
        // "六",
        // "七",
        // "八",
      ];

      // 根据标识符和数字进行特定的转换
      return identifier === "week" && (num === 0 || num === 7)
          ? "日"
          : character[num];
    },

    //用户为系统管理员或者管理员时选择年级，学院下拉框，获取班级信息
    handleSelectChange() {
      // 处理每个下拉框的变化
      this.performOperation();
    },
    performOperation() {
      // 若选择了年级和学院信息，查询班级信息
      if (this.grade && this.college) {
        this.request.get("/classInfo/gradeAndCollege", {
          params: {
            grade: this.grade,
            college: this.college
          }
        }).then(res => {
          this.classInfos = res.data
        })
      }
    },


    save() {
      this.request.post("/schedule", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          //修改成功之后重新查询课表信息
          this.querySchedule()
        } else {
          this.$message.error("保存失败")
        }
      })

    },

    handleFormSelectChange() {
      this.request.get("/schedule/courseTime/"+this.form.id).then(res => {
        this.courseTimeList = res.data
      })
      this.request.get("/schedule/classroom/"+this.form.id).then(res => {
        this.classroomList = res.data
      })
    },

    editSchedule(){
        if (this.courseData.length === 0) {
          this.$message.error('请先查询课程信息');
          return;
        }
        this.dialogFormVisible = true;
      }
  }
}
</script>

<style scoped>
/* 表格容器样式 */
.table-wrapper {
  margin-top: 20px;    /*设置容器与上方元素的上外边距为20像素。*/
  overflow: auto;     /*当表格内容溢出容器时，显示滚动条。*/
  max-height: 400px; /* 设置表格容器的最大高度为400像素*/
}

/* 表格样式 */
.tabel-container table {
  width: 100%;      /*使表格占据其容器的100%宽度。*/
  border-collapse: collapse;    /*合并表格边框，使其看起来更紧凑。*/
}

/* 表头样式 */
.tabel-container th {
  background-color: #67a1ff;
  color: #fff;
  text-align: center;
  padding: 10px;    /*设置内边距为10像素。*/
  height: 40px; /* 设置表头单元格的固定高度*/
}

/* 表体样式 */
.tabel-container td {
  border: 1px solid #ccc;  /*设置边框为1像素实心灰色。*/
  padding: 10px;  /*设置内边距为10像素。*/
  text-align: center;  /*使文字水平居中。*/
  vertical-align: middle;  /*使文字垂直居中。*/
  width: 150px; /*为表格单元格设置固定宽度。*/
  height: 40px; /* 设置表格单元格的固定高度*/
  overflow: hidden;  /*隐藏超出单元格宽度的内容。*/
  text-overflow: ellipsis;  /*超出部分显示省略号*/
}

/* 第一列样式 */
.tabel-container td:first-child {
  font-weight: bold;   /*设置文字粗体。*/
  width: 100px;     /* 调整第一列的宽度 */
}

/* 课时信息样式 */
.tabel-container .lesson-info {
  margin-bottom: 5px;   /*设置底部外边距为5像素。*/
}

/* 课程信息样式 */
.tabel-container .period {
  font-size: 12px;   /*设置文字大小为12像素。*/
  color: #677998;    /* 设置文字颜色为深蓝灰色。*/
}

/* 占位符样式,用于显示空单元格 */
.tabel-container td:empty:before {
  content: "-";
  color: #ccc;
  display: flex; /* 使用 Flex 布局 */
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
}
</style>

