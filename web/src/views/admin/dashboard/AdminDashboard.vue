<template>
  <div class="admin-dashboard">
    <!-- 仅保留内容区域，不再包含侧边栏和头部 -->
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
          <div class="stat-title">今日新增</div>
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
      userCount: 1024,
      examCount: 24,
      answerCount: 3580,
      todayCount: 24,
      recentAnswers: [
        { username: 'user1', examTitle: '抑郁自评量表', score: 75, submitTime: '2025-06-19 14:30' },
        { username: 'user2', examTitle: '焦虑自评量表', score: 60, submitTime: '2025-06-19 10:15' },
        { username: 'user3', examTitle: '生活压力量表', score: 85, submitTime: '2025-06-18 16:20' },
        { username: 'user4', examTitle: '抑郁自评量表', score: 65, submitTime: '2025-06-18 09:45' }
      ]
    }
  },
  mounted() {
    // 初始化图表
    this.initChart();
  },
  methods: {
    initChart() {
      // ECharts初始化代码（略，与之前相同）
    }
  }
}
</script>