<template>
  <div class="exam-history">
    <el-card class="filter-card">
      <el-row :gutter="20">
        <el-col :sm="24" :md="8">
          <el-input
              v-model="filterKeyword"
              placeholder="输入试卷名称或结果搜索"
              prefix-icon="el-icon-search"
              class="filter-input"
          >
            <template #append>
              <el-button @click="handleSearch">搜索</el-button>
            </template>
          </el-input>
        </el-col>
        <el-col :sm="24" :md="8">
          <el-select v-model="filterExam" placeholder="选择试卷" class="filter-select">
            <el-option label="全部" value="" />
            <el-option v-for="exam in exams" :key="exam.id" :label="exam.title" :value="exam.id" />
          </el-select>
        </el-col>
        <el-col :sm="24" :md="8">
          <el-select v-model="filterResult" placeholder="选择结果" class="filter-select">
            <el-option label="全部" value="" />
            <el-option label="良好" value="good" />
            <el-option label="一般" value="normal" />
            <el-option label="需关注" value="attention" />
          </el-select>
        </el-col>
      </el-row>
    </el-card>

    <el-card v-loading="loading" class="history-list-card">
      <template #header>
        <div class="card-header">
          <span>答题记录</span>
          <span class="total-count">共 {{ filteredHistories.length }} 条记录</span>
        </div>
      </template>

      <el-table
          :data="filteredHistories"
          stripe
          style="width: 100%"
          v-loading="loading"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="examTitle" label="试卷名称" min-width="200" />
        <el-table-column prop="score" label="得分" width="100">
          <template #default="scope">
            <span>{{ scope.row.score }}/100</span>
          </template>
        </el-table-column>
        <el-table-column prop="result" label="结果" width="120">
          <template #default="scope">
            <el-tag :type="getResultType(scope.row.result)">{{ getResultText(scope.row.result) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="答题时间" width="180" />
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button size="small" @click="viewDetail(scope.row.id)">查看详情</el-button>
            <el-button size="small" type="primary" @click="reTakeExam(scope.row.examId)">重新答题</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container" v-if="filteredHistories.length > 0">
        <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="histories.length"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
      </div>

      <div class="empty-state" v-else>
        <el-icon class="empty-icon"><time /></el-icon>
        <p class="empty-text">暂无答题记录</p>
        <el-button type="primary" @click="goToExamCenter">去答题</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'ExamHistory',
  data() {
    return {
      loading: false,
      filterKeyword: '',
      filterExam: '',
      filterResult: '',
      currentPage: 1,
      pageSize: 10,
      exams: [
        { id: 1, title: '抑郁自评量表(SDS)' },
        { id: 2, title: '焦虑自评量表(SAS)' },
        { id: 3, title: '生活压力量表(SPSS)' },
        { id: 4, title: '大五人格测试(NEO-FFI)' }
      ],
      histories: [
        {
          id: 1,
          examId: 1,
          examTitle: '抑郁自评量表(SDS)',
          score: 75,
          result: 'attention',
          createTime: '2025-06-15 14:30:22'
        },
        {
          id: 2,
          examId: 2,
          examTitle: '焦虑自评量表(SAS)',
          score: 60,
          result: 'normal',
          createTime: '2025-06-10 09:15:45'
        },
        {
          id: 3,
          examId: 3,
          examTitle: '生活压力量表(SPSS)',
          score: 85,
          result: 'attention',
          createTime: '2025-06-05 16:20:11'
        },
        {
          id: 4,
          examId: 4,
          examTitle: '大五人格测试(NEO-FFI)',
          score: 90,
          result: 'good',
          createTime: '2025-05-28 10:45:33'
        },
        {
          id: 5,
          examId: 1,
          examTitle: '抑郁自评量表(SDS)',
          score: 65,
          result: 'normal',
          createTime: '2025-05-20 13:30:15'
        }
      ]
    }
  },
  computed: {
    filteredHistories() {
      return this.histories
          .filter(history =>
              history.examTitle.includes(this.filterKeyword) ||
              this.getResultText(history.result).includes(this.filterKeyword)
          )
          .filter(history => !this.filterExam || history.examId == this.filterExam)
          .filter(history => !this.filterResult || history.result == this.filterResult)
          .slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize)
    }
  },
  methods: {
    getResultType(result) {
      const types = {
        'good': 'success',
        'normal': 'primary',
        'attention': 'warning'
      }
      return types[result] || ''
    },
    getResultText(result) {
      const texts = {
        'good': '良好',
        'normal': '一般',
        'attention': '需关注'
      }
      return texts[result] || result
    },
    handleSearch() {
      this.currentPage = 1
    },
    handleSizeChange(val) {
      this.pageSize = val
    },
    handleCurrentChange(val) {
      this.currentPage = val
    },
    viewDetail(historyId) {
      this.$router.push(`/user/exam-history/${historyId}/detail`)
    },
    reTakeExam(examId) {
      this.$router.push(`/user/exam-center/${examId}/start`)
    },
    goToExamCenter() {
      this.$router.push('/user/exam-center')
    }
  }
}
</script>

<style scoped>
.exam-history {
  padding: 20px 0;
}

.filter-card, .history-list-card, .pagination-container, .empty-state {
  /* 样式与试卷中心页面一致，此处省略以避免重复 */
  /* 可参考ExamCenter.vue中的对应样式 */
}
</style>