<template>
  <div class="admin-dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-title">用户总数</div>
          <div class="stat-value">{{ userCount }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-title">试卷总数</div>
          <div class="stat-value">{{ examCount }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-title">答卷总数</div>
          <div class="stat-value">{{ answerCount }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-title">今日新增答卷</div>
          <div class="stat-value">{{ todayCount }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="16">
        <el-card>
          <div slot="header" class="clearfix">
            <span>答卷数据分析</span>
          </div>
          <div id="answerChart" style="width: 100%; height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div slot="header" class="clearfix">
            <span>最近答卷</span>
          </div>
          <el-table :data="recentAnswers" stripe style="width: 100%">
            <el-table-column prop="username" label="用户名"></el-table-column>
            <el-table-column prop="examTitle" label="试卷名称"></el-table-column>
            <el-table-column prop="score" label="得分"></el-table-column>
            <el-table-column prop="submitTime" label="提交时间"></el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>

export default {
  name: 'AdminDashboard',
  data() {
    return {
      userCount: 0,
      examCount: 0,
      answerCount: 0,
      todayCount: 0,
      recentAnswers: []
    }
  },
  mounted() {
    this.fetchData(); // 调用获取数据的方法
    this.initChart();
  },
  methods: {
    // 获取数据的方法
    fetchData() {
      // 假设获取统计数据的接口
      this.$http.get('/api/getStatsData')
          .then(response => {
            const data = response.data;
            this.userCount = data.data.userCount;
            this.examCount = data.data.examCount;
            this.answerCount = data.data.answerCount;
            this.todayCount = data.data.todayAnswerCount;
          })
          .catch(error => {
            console.error('获取统计数据失败:', error);
          });

      // 假设获取最近答卷数据的接口
      this.$http.get('/api/recentAnswers')
          .then(response => {
            this.recentAnswers = response.data;
          })
          .catch(error => {
            console.error('获取最近答卷数据失败:', error);
          });
    },
    initChart() {
      // ECharts初始化代码
      // 这里需要你根据实际从后端获取的图表数据，结合 ECharts 进行初始化和渲染
      // 例如如果后端返回了用于图表的数据，你可以在这里处理
    }
  }
}
</script>