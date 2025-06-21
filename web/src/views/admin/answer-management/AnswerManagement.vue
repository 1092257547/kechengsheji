<template>
  <div class="exam-result-management">
    <!-- 顶部操作栏 -->
    <el-card class="header-card">
      <template #header>
        <div class="card-header">
          <span>答卷数据管理</span>
          <el-button type="primary" @click="handleExport">
            <el-icon><Download /></el-icon>导出答卷
          </el-button>
        </div>
      </template>

      <!-- 搜索筛选区域 -->
      <el-form :inline="true" :model="filterForm" class="search-form">
        <el-form-item label="试卷名称">
          <el-input v-model="filterForm.paperTitle" placeholder="请输入试卷名称"></el-input>
        </el-form-item>
        <el-form-item label="考生姓名">
          <el-input v-model="filterForm.studentName" placeholder="请输入考生姓名"></el-input>
        </el-form-item>
        <el-form-item label="得分范围">
          <el-select v-model="filterForm.scoreRange" placeholder="请选择得分范围">
            <el-option label="全部" value="all"></el-option>
            <el-option label="0-60分" value="0-60"></el-option>
            <el-option label="60-80分" value="60-80"></el-option>
            <el-option label="80-100分" value="80-100"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="提交时间">
          <el-date-picker
              v-model="filterForm.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 280px"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleFilter">
            <el-icon><Search /></el-icon>搜索
          </el-button>
          <el-button @click="resetFilter">
            <el-icon><Refresh /></el-icon>重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6">
        <el-card class="statistics-card" shadow="hover">
          <div class="statistic-item">
            <div class="statistic-title">总答卷数</div>
            <div class="statistic-value">{{ totalCount }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card" shadow="hover">
          <div class="statistic-item">
            <div class="statistic-title">平均分</div>
            <div class="statistic-value">{{ averageScore.toFixed(2) }}分</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card" shadow="hover">
          <div class="statistic-item">
            <div class="statistic-title">最高分</div>
            <div class="statistic-value">{{ maxScore }}分</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card" shadow="hover">
          <div class="statistic-item">
            <div class="statistic-title">及格率</div>
            <div class="statistic-value">{{ passRate }}%</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 答卷列表 -->
    <el-card class="result-table-card">
      <el-table
          :data="resultList"
          stripe
          border
          highlight-current-row
          @row-click="handleViewDetail"
          style="width: 100%">
        <el-table-column type="index" label="序号" width="60"></el-table-column>
        <el-table-column prop="paperTitle" label="试卷名称" min-width="180"></el-table-column>
        <el-table-column prop="studentName" label="考生姓名" width="120"></el-table-column>
        <el-table-column prop="score" label="得分" width="100">
          <template #default="scope">
            <el-badge :content="scope.row.score" :type="getScoreType(scope.row.score)"></el-badge>
          </template>
        </el-table-column>
        <el-table-column prop="passStatus" label="是否及格" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.passStatus ? 'success' : 'danger'">
              {{ scope.row.passStatus ? '及格' : '不及格' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="useTime" label="用时" width="120">
          <template #default="scope">
            {{ formatTime(scope.row.useTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="submitTime" label="提交时间" width="180"></el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button size="small" type="primary" @click="handleViewDetail(scope.row)">
              <el-icon><View /></el-icon>查看
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页控件 -->
      <el-pagination
          class="pagination"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </el-card>

    <!-- 答卷详情对话框 -->
    <el-dialog :visible.sync="detailDialogVisible" title="答卷详情" width="80%">
      <div v-loading="detailLoading">
        <el-descriptions title="答卷基本信息" :column="3" border>
          <el-descriptions-item label="试卷名称">{{ detailResult.paperTitle }}</el-descriptions-item>
          <el-descriptions-item label="考生姓名">{{ detailResult.studentName }}</el-descriptions-item>
          <el-descriptions-item label="得分">{{ detailResult.score }}分</el-descriptions-item>
          <el-descriptions-item label="是否及格">{{ detailResult.passStatus ? '及格' : '不及格' }}</el-descriptions-item>
          <el-descriptions-item label="用时">{{ formatTime(detailResult.useTime) }}</el-descriptions-item>
          <el-descriptions-item label="提交时间">{{ detailResult.submitTime }}</el-descriptions-item>
        </el-descriptions>

        <el-divider />

        <div v-if="detailResult.questions && detailResult.questions.length > 0">
          <h3 style="margin-bottom: 15px">试题回答详情</h3>
          <div v-for="(question, index) in detailResult.questions" :key="index" class="question-item">
            <el-card class="question-card">
              <div class="question-header">
                <span class="question-number">第{{ index + 1 }}题</span>
                <span class="question-type">{{ getQuestionType(question.type) }}</span>
                <span class="question-score">{{ question.score }}分</span>
              </div>
              <div class="question-content">
                <p>{{ question.content }}</p>
                <div v-if="question.options && question.options.length > 0" class="question-options">
                  <div v-for="(option, optIndex) in question.options" :key="optIndex" class="option-item">
                    <span :class="['option-key', question.userAnswer === option.key ? 'user-selected' : '']">{{ option.key }}.</span>
                    <span :class="question.correctAnswer === option.key ? 'correct-option' : question.userAnswer === option.key ? 'user-option' : ''">
                      {{ option.value }}
                    </span>
                    <span v-if="question.userAnswer === option.key && question.correctAnswer !== option.key" class="wrong-tag">×</span>
                    <span v-if="question.correctAnswer === option.key && question.userAnswer === option.key" class="correct-tag">√</span>
                  </div>
                </div>
                <div v-if="question.type === 5" class="question-text-answer">
                  <p class="answer-title">考生答案：</p>
                  <p class="user-answer">{{ question.userAnswer || '未作答' }}</p>
                  <p class="answer-title">正确答案：</p>
                  <p class="correct-answer">{{ question.correctAnswer || '无标准答案' }}</p>
                </div>
                <div class="question-explanation" v-if="question.explanation">
                  <p class="answer-title">解析：</p>
                  <p>{{ question.explanation }}</p>
                </div>
                <div class="question-result" :class="question.isCorrect ? 'correct-result' : 'wrong-result'">
                  <span>{{ question.isCorrect ? '回答正确' : '回答错误' }}</span>
                </div>
              </div>
            </el-card>
          </div>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="detailDialogVisible = false">关闭</el-button>
          <el-button type="primary" @click="handlePrintDetail">打印详情</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'AnswerManagement',
  data() {
    return {
      // 筛选条件
      filterForm: {
        paperTitle: '',
        studentName: '',
        scoreRange: 'all',
        dateRange: []
      },

      // 答卷列表数据
      resultList: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      totalCount: 0,
      averageScore: 0,
      maxScore: 0,
      passRate: 0,

      // 详情数据
      detailResult: {},
      detailDialogVisible: false,
      detailLoading: false,

      // 加载状态
      isLoading: false
    }
  },
  mounted() {
    this.loadExamResults();
    this.calculateStatistics();
  },
  methods: {
    // 加载答卷数据
    loadExamResults() {
      this.isLoading = true;

      // 模拟API请求
      setTimeout(() => {
        // 模拟后端返回数据
        this.resultList = [
          {
            id: 1,
            paperTitle: "抑郁症自评量表",
            studentName: "张三",
            score: 85,
            passStatus: true,
            useTime: 1200,
            submitTime: "2025-06-20 14:30:00",
            questions: this.mockQuestions(85)
          },
          {
            id: 2,
            paperTitle: "焦虑症测试问卷",
            studentName: "李四",
            score: 72,
            passStatus: true,
            useTime: 900,
            submitTime: "2025-06-20 15:45:00",
            questions: this.mockQuestions(72)
          },
          {
            id: 3,
            paperTitle: "心理健康状况评估",
            studentName: "王五",
            score: 65,
            passStatus: true,
            useTime: 1500,
            submitTime: "2025-06-21 09:15:00",
            questions: this.mockQuestions(65)
          },
          {
            id: 4,
            paperTitle: "抑郁症自评量表",
            studentName: "赵六",
            score: 48,
            passStatus: false,
            useTime: 1000,
            submitTime: "2025-06-21 10:30:00",
            questions: this.mockQuestions(48)
          },
          {
            id: 5,
            paperTitle: "焦虑症测试问卷",
            studentName: "钱七",
            score: 88,
            passStatus: true,
            useTime: 800,
            submitTime: "2025-06-21 14:20:00",
            questions: this.mockQuestions(88)
          }
        ];

        this.total = 20; // 模拟总记录数
        this.isLoading = false;
        this.calculateStatistics();
      }, 500);
    },

    // 模拟试题数据
    mockQuestions(score) {
      const questions = [
        {
          id: 1,
          type: 1,
          content: "我感到情绪沮丧，郁闷",
          options: [
            { key: "A", value: "很少" },
            { key: "B", value: "有时" },
            { key: "C", value: "经常" },
            { key: "D", value: "总是" }
          ],
          correctAnswer: "C",
          userAnswer: score >= 80 ? "C" : score >= 60 ? "B" : "A",
          score: 20,
          explanation: "情绪沮丧是抑郁症的常见症状，频率越高可能程度越严重",
          isCorrect: score >= 80 ? true : score >= 60 ? Math.random() > 0.3 : false
        },
        {
          id: 2,
          type: 3,
          content: "我夜间睡眠不好",
          options: [
            { key: "A", value: "正确" },
            { key: "B", value: "错误" }
          ],
          correctAnswer: "A",
          userAnswer: score >= 80 ? "A" : score >= 60 ? "A" : "B",
          score: 15,
          explanation: "睡眠障碍是许多心理问题的常见伴随症状",
          isCorrect: score >= 80 ? true : score >= 60 ? Math.random() > 0.5 : false
        },
        {
          id: 3,
          type: 2,
          content: "我经常感到紧张不安",
          options: [
            { key: "A", value: "没有" },
            { key: "B", value: "偶尔" },
            { key: "C", value: "经常" },
            { key: "D", value: "总是" }
          ],
          correctAnswer: "BC",
          userAnswer: score >= 80 ? "BC" : score >= 60 ? "B" : "A",
          score: 20,
          explanation: "紧张不安是焦虑症的典型表现",
          isCorrect: score >= 80 ? true : score >= 60 ? Math.random() > 0.4 : false
        },
        {
          id: 4,
          type: 4,
          content: "抑郁症常见的症状包括______、______和睡眠障碍。",
          correctAnswer: "情绪低落,兴趣减退",
          userAnswer: score >= 80 ? "情绪低落,兴趣减退" : score >= 60 ? "情绪低落,食欲下降" : "心情好,精力充沛",
          score: 15,
          explanation: "抑郁症的核心症状包括情绪低落、兴趣减退和精力缺乏",
          isCorrect: score >= 80 ? true : score >= 60 ? Math.random() > 0.6 : false
        },
        {
          id: 5,
          type: 5,
          content: "请简述抑郁症的预防措施。",
          correctAnswer: "保持良好的生活习惯、定期运动、保持社交活动、学会压力管理",
          userAnswer: score >= 80 ? "保持良好的生活习惯，定期运动，保持社交活动，学会压力管理" :
              score >= 60 ? "多运动，保持好心情" : "不知道",
          score: 30,
          isCorrect: score >= 80 ? true : score >= 60 ? Math.random() > 0.7 : false
        }
      ];

      // 根据得分调整正确题数
      let correctCount = Math.round(questions.length * (score / 100));
      questions.sort(() => 0.5 - Math.random()).slice(0, correctCount).forEach(q => q.isCorrect = true);
      return questions;
    },

    // 处理搜索
    handleFilter() {
      this.currentPage = 1;
      this.loadExamResults();
    },

    // 重置搜索条件
    resetFilter() {
      this.filterForm = {
        paperTitle: '',
        studentName: '',
        scoreRange: 'all',
        dateRange: []
      };
      this.currentPage = 1;
      this.loadExamResults();
    },

    // 处理分页大小变化
    handleSizeChange(size) {
      this.pageSize = size;
      this.loadExamResults();
    },

    // 处理页码变化
    handleCurrentChange(page) {
      this.currentPage = page;
      this.loadExamResults();
    },

    // 查看详情
    handleViewDetail(row) {
      this.detailLoading = true;
      this.detailResult = row;

      // 模拟详情数据加载
      setTimeout(() => {
        this.detailDialogVisible = true;
        this.detailLoading = false;
      }, 300);
    },

    // 打印详情
    handlePrintDetail() {
      this.$message.success('打印功能已触发');
      // 实际项目中可使用window.print()或专门的打印库
    },

    // 导出答卷
    handleExport() {
      this.$message.success('导出功能已触发');
      // 实际项目中可调用导出API或生成Excel
    },

    // 计算统计数据
    calculateStatistics() {
      if (this.resultList.length === 0) {
        this.totalCount = 0;
        this.averageScore = 0;
        this.maxScore = 0;
        this.passRate = 0;
        return;
      }

      this.totalCount = this.total; // 总记录数
      const scores = this.resultList.map(item => item.score);
      const passCount = this.resultList.filter(item => item.passStatus).length;

      this.averageScore = scores.reduce((sum, score) => sum + score, 0) / scores.length;
      this.maxScore = Math.max(...scores);
      this.passRate = Math.round((passCount / this.resultList.length) * 100);
    },

    // 获取分数标签类型
    getScoreType(score) {
      if (score >= 80) return 'success';
      if (score >= 60) return 'primary';
      return 'danger';
    },

    // 获取试题类型名称
    getQuestionType(type) {
      const typeMap = {
        1: '单选题',
        2: '多选题',
        3: '判断题',
        4: '填空题',
        5: '简答题'
      };
      return typeMap[type] || '未知类型';
    },

    // 格式化用时
    formatTime(seconds) {
      const minutes = Math.floor(seconds / 60);
      const secs = seconds % 60;
      return `${minutes}分${secs}秒`;
    }
  }
}
</script>

<style scoped>
.exam-result-management {
  padding: 20px;
}

.header-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-top: 10px;
}

.statistics-row {
  margin-bottom: 20px;
}

.statistics-card {
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.statistic-item {
  text-align: center;
}

.statistic-title {
  font-size: 14px;
  color: #606266;
  margin-bottom: 5px;
}

.statistic-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}

.result-table-card {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.question-item {
  margin-bottom: 15px;
}

.question-card {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: all 0.3s;
}

.question-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.question-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 15px;
  border-bottom: 1px solid #ebeef5;
}

.question-number {
  font-weight: bold;
  font-size: 16px;
  color: #303133;
}

.question-type {
  font-size: 14px;
  color: #606266;
}

.question-score {
  font-weight: bold;
  color: #409EFF;
}

.question-content {
  padding: 15px;
}

.question-options {
  margin: 15px 0;
}

.option-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 10px;
  padding-left: 20px;
  position: relative;
}

.option-key {
  position: absolute;
  left: 0;
  width: 20px;
  text-align: center;
  color: #606266;
}

.correct-option {
  color: #67c23a;
}

.user-option {
  color: #409EFF;
}

.wrong-tag, .correct-tag {
  position: absolute;
  right: 10px;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
}

.wrong-tag {
  background-color: #f56c6c;
  color: white;
}

.correct-tag {
  background-color: #67c23a;
  color: white;
}

.question-text-answer {
  margin: 15px 0;
}

.answer-title {
  font-weight: bold;
  margin-bottom: 5px;
  color: #303133;
}

.user-answer {
  padding: 8px 12px;
  background-color: #f5f7fa;
  border-radius: 4px;
  display: inline-block;
}

.correct-answer {
  padding: 8px 12px;
  background-color: #ecf5ff;
  border-left: 3px solid #409EFF;
  display: inline-block;
}

.question-explanation {
  margin-top: 15px;
  padding: 10px 15px;
  background-color: #f9f9f9;
  border-left: 3px solid #909399;
  border-radius: 0 4px 4px 0;
}

.question-result {
  margin-top: 15px;
  padding: 8px 12px;
  border-radius: 4px;
  font-weight: bold;
  text-align: center;
}

.correct-result {
  background-color: #e6f7ed;
  color: #67c23a;
}

.wrong-result {
  background-color: #fef0f0;
  color: #f56c6c;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>