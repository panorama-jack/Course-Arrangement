<template>
  <div>
    <el-row :span="10" style="margin-bottom: 30px">
      <el-col :span="6">
        <el-card style="color:#409EFF">
            <div ><i class="el-icon-user"></i>学生总数</div>
          <div style="padding: 10px 0;text-align: center;font-weight: bold">{{ value[0]}}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card  style="color:#67C23A">
          <div><i class="el-icon-user-solid"></i>教师总数</div>
          <div style="padding: 10px 0;text-align: center;font-weight: bold">{{ value[1]}}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card  style="color:#303133">
          <div><i class="el-icon-s-promotion"></i>班级总数</div>
          <div style="padding: 10px 0;text-align: center;font-weight: bold">{{ value[2]}}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color:#909399">
          <div ><i class="el-icon-s-order"></i>课程总数</div>
          <div style="padding: 10px 0;text-align: center;font-weight: bold">{{ value[3]}}</div>
        </el-card>
      </el-col>

    </el-row>

    <el-row>
      <el-col :span="10">
          <div ref="pieChart" style="height:600px; margin-top: 40px"></div>
      </el-col>

      <el-col :span="14">
        <div style="text-align: center; margin-bottom: 10px">
        <el-select clearable style="width: 200px" v-model="term" class="ml-5" placeholder="请选择学期"  @change="handleSelectChange">
          <el-option  v-for="item in terms" :key="item.name" :label="item.name" :value="item.value">
            {{ item.name }}
          </el-option>
        </el-select>
          <el-select clearable style="width: 200px" v-model="grade" class="ml-5" placeholder="请选择年级"  @change="handleSelectChange">
            <el-option v-for="item in grades" :key="item.name" :label="item.name" :value="item.value" >
              {{ item.name }}
            </el-option>
          </el-select>
        </div>
        <div ref="barChart" style="height:600px;"></div>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import * as echarts from 'echarts';
export default {
  name: "SystemData",
  data() {
    return {
      value: [],
      pieChart: null,
      barChart: null,
      term:"",
      terms:[],
      grade:"",
      grades:[]
    }
  },
  mounted() {
    this.request.get("/dict/term").then(res => {
      this.terms = res.data
    })
    this.request.get("/dict/grade").then(res => {
      this.grades = res.data
    })
    this.initEChart();
    this.loadPieData();
    this.loadBarData();
  },

  methods: {
    initEChart() {
      this.pieChart = echarts.init(this.$refs.pieChart);
      this.barChart = echarts.init(this.$refs.barChart);
    },

    loadPieData() {
      this.request.get("/echarts/count").then(res => {
        this.value = res.data.value;
      })
      this.request.get('/echarts/queryTypeCount')
          .then(response => {
            this.renderPieChart(response.data);
          })
          .catch(error => {
            console.error('请求失败', error);
          });
    },

    async loadBarData() {
      try {
        const res = await this.request.get('/echarts/queryCoursePlanProcess', {
          params: {
            term: this.term,
            gradeNo: this.grade,
          },
        });
        console.log(res.data);
        this.renderBarChart(res.data);
      } catch (error) {
        console.error('请求失败', error);
      }
    },
    handleSelectChange(){
      this.loadBarData()
    },
    renderPieChart(data) {
      const option = {
        title: {
          text: '系统字典数据',
          subtext: '不同 Type 的数量',
          left: 'center',
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)',
        },
        series: [
          {
            name: '类型数量',
            type: 'pie',
            radius: '65%',
            data: data.map(item => ({
              name: item.type,
              value: item.type_count,
            })),
            roseType: 'radius',
            label: {
              show: true,
            },
          },
        ],

      };
      this.pieChart.setOption(option);
    },

    //堆叠图
    renderBarChart(data) {
      // 按学院分组数据
      const groupedData = {};
      data.forEach(item => {
        const collegeName = item.collegeName;
        const className = item.className;
        const status = item.appearanceStatus === '出现' ? '已安排课程计划' : '未安排课程计划';
        //按学院放入是否安排好课程计划的列表
        if (!groupedData[collegeName]) {
          groupedData[collegeName] = {
            '已安排课程计划': [],
            '未安排课程计划': [],
          };
        }
        groupedData[collegeName][status].push(className);
      });

      // 构造堆叠图数据
      const legendData = ['已安排课程计划', '未安排课程计划'];
      const xAxisData = Object.keys(groupedData);    //x轴数据
      const seriesData = legendData.map(status => ({
        name: status,
        type: 'bar',
        stack: '总量',
        data: xAxisData.map(college => {
          const total = groupedData[college]['已安排课程计划'].length + groupedData[college]['未安排课程计划'].length;
          const count = groupedData[college][status].length;
          const percentage = total === 0 ? 0 : (count / total) * 100;    //计算百分比
          return percentage.toFixed(2);
        }),
        barWidth : 50,//柱图宽度
      }));

      // 堆叠图配置
      const option = {
        title: {
          text: '各学院班级课程计划安排情况',
          subtext: '按学院分类',
          left: 'center',
        },
        tooltip: {   //鼠标悬浮时显示数据
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
          },
          formatter: function(params) {
            const college = params[0].name;
            return params.map(param => {
              const status = param.seriesName;
              const count = param.value;
              const classList = groupedData[college][status].join('<br>');
              return `${status}: ${count}%<br>${classList}`;
            }).join('<br>');
          },
        },
        //按钮
        legend: {
          data: legendData,
          top: 50
        },
        //x轴
        xAxis: {
          type: 'category',
          axisTick: { show: false },
          axisLabel: {
            interval: 0, // 始终显示所有标签
            rotate: 45, // 标签旋转角度，根据需要调整
          },
          data: xAxisData,
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: '{value}%', // 设置纵坐标刻度的百分比格式
          },
        },
        series: seriesData,
        //x轴滚动条
        dataZoom: {
          show: true, // 为true 滚动条出现
          realtime: true, // 实时更新
          type:'slider', // 有type这个属性，滚动条在最下面，也可以不行，写y：36，这表示距离顶端36px，一般就是在图上面。
          height: 12, // 表示滚动条的高度，也就是粗细
          start: 0, // 表示默认展示0%～80%这一段。
          end: 80,
        },

        // 设置 grid 属性来调整图表大小
        grid: {
          top: '20%', // 调整左边距
          left: '10%', // 调整左边距
          right: '10%', // 调整右边距
          bottom: '10%', // 调整底边距
          containLabel: true,
        },
    };
      // 渲染堆叠图
      this.barChart.setOption(option);
    },
  },
};

</script>
<style scoped>

</style>