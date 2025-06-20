<template>
  <div class="question-management">
    <!-- 搜索区域 -->
    <el-card class="filter-card">
      <template #header>
        <div class="card-header">
          <span>试题管理</span>
          <el-button type="primary" @click="handleAddQuestion">
            <el-icon><Plus /></el-icon>添加试题
          </el-button>
        </div>
      </template>

      <el-form :inline="true" :model="filterForm" class="demo-form-inline">
        <el-form-item label="题目内容">
          <el-input v-model="filterForm.content" placeholder="请输入题目关键词"></el-input>
        </el-form-item>
        <el-form-item label="题目类型">
          <el-select v-model="filterForm.type" placeholder="请选择题目类型">
            <el-option label="单选题" value="1"></el-option>
            <el-option label="多选题" value="2"></el-option>
            <el-option label="判断题" value="3"></el-option>
            <el-option label="填空题" value="4"></el-option>
            <el-option label="简答题" value="5"></el-option>
          </el-select>
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

    <!-- 试题列表 -->
    <el-card class="question-table-card">
      <el-table
          :data="questionList"
          stripe
          border
          highlight-current-row
          @row-click="handleRowClick"
          style="width: 100%">
        <el-table-column type="index" label="序号" width="60"></el-table-column>
        <el-table-column prop="content" label="题目内容"></el-table-column>
        <el-table-column prop="type" label="题目类型" width="100">
          <template #default="scope">
            <el-tag :type="getTypeTag(scope.row.type)">{{ getTypeName(scope.row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="选项/答案" width="180">
          <template #default="scope">
            <el-button type="text" size="small" @click="handleViewDetail(scope.row)">查看详情</el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button size="small" type="warning" @click="handleEditQuestion(scope.row)">
              <el-icon><Edit /></el-icon>编辑
            </el-button>
            <el-button size="small" type="danger" @click="handleDeleteQuestion(scope.row)">
              <el-icon><Delete /></el-icon>删除
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
          :page-sizes="[10, 20, 30, 50]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </el-card>

    <!-- 添加/编辑试题对话框 -->
    <el-dialog :visible.sync="dialogVisible" title="试题信息">
      <el-form :model="questionForm" :rules="questionFormRules" ref="questionFormRef" label-width="100px">
        <el-form-item label="题目类型" prop="type">
          <el-radio-group v-model="questionForm.type">
            <el-radio label="1">单选题</el-radio>
            <el-radio label="2">多选题</el-radio>
            <el-radio label="3">判断题</el-radio>
            <el-radio label="4">填空题</el-radio>
            <el-radio label="5">简答题</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="题目内容" prop="content">
          <el-input type="textarea" :rows="4" v-model="questionForm.content"></el-input>
        </el-form-item>

        <!-- 选择题选项 -->
        <el-form-item label="选项" v-if="isChoiceQuestion">
          <el-form :inline="true" :model="optionForm" class="option-form">
            <el-input v-model="optionForm.key" placeholder="选项标识(A/B/C/D...)" style="width: 80px"></el-input>
            <el-input v-model="optionForm.value" placeholder="选项内容" style="width: 300px"></el-input>
            <el-button type="primary" @click="addOption">添加选项</el-button>
          </el-form>
          <el-table :data="questionForm.options" stripe style="width: 100%; margin-top: 10px">
            <el-table-column prop="key" label="选项标识" width="100"></el-table-column>
            <el-table-column prop="value" label="选项内容"></el-table-column>
            <el-table-column label="操作" width="80">
              <template #default="scope">
                <el-button size="mini" type="danger" @click="removeOption(scope.$index)">
                  <el-icon><Delete /></el-icon>
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>

        <el-form-item label="答案" prop="answer">
          <el-input type="textarea" :rows="2" v-model="questionForm.answer"></el-input>
        </el-form-item>
        <el-form-item label="答案解析" prop="explanation">
          <el-input type="textarea" :rows="3" v-model="questionForm.explanation"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveQuestion">保存</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 确认删除对话框 -->
    <el-dialog :visible.sync="deleteDialogVisible" title="确认删除">
      <div slot="content">您确定要删除该试题吗？</div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="deleteDialogVisible = false">取消</el-button>
          <el-button type="danger" @click="confirmDeleteQuestion">确认删除</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog :visible.sync="detailDialogVisible" title="试题详情">
      <el-form :model="questionDetail" label-width="100px">
        <el-form-item label="题目类型">
          <span>{{ getTypeName(questionDetail.type) }}</span>
        </el-form-item>
        <el-form-item label="题目内容">
          <pre>{{ questionDetail.content }}</pre>
        </el-form-item>
        <el-form-item label="选项" v-if="isChoiceQuestionDetail">
          <div v-for="(option, index) in questionDetail.options" :key="index">
            {{ option.key }}. {{ option.value }}
          </div>
        </el-form-item>
        <el-form-item label="答案">
          <pre>{{ questionDetail.answer }}</pre>
        </el-form-item>
        <el-form-item label="答案解析">
          <pre>{{ questionDetail.explanation }}</pre>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="detailDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'QuestionManagement',
  data() {
    return {
      // 筛选表单
      filterForm: {
        content: '',
        type: ''
      },

      // 试题列表数据
      questionList: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,

      // 添加/编辑表单
      questionForm: {
        id: null,
        type: '1',
        content: '',
        options: [],
        answer: '',
        explanation: ''
      },

      // 选项表单
      optionForm: {
        key: '',
        value: ''
      },

      // 表单验证规则
      questionFormRules: {
        type: [
          { required: true, message: '请选择题目类型', trigger: 'change' }
        ],
        content: [
          { required: true, message: '请输入题目内容', trigger: 'blur' }
        ],
        answer: [
          { required: true, message: '请输入答案', trigger: 'blur' }
        ]
      },

      // 对话框状态
      dialogVisible: false,
      deleteDialogVisible: false,
      detailDialogVisible: false,

      // 当前操作的试题ID
      currentQuestionId: null,

      // 试题详情
      questionDetail: {}
    }
  },
  computed: {
    // 判断是否为选择题（单选题、多选题）
    isChoiceQuestion() {
      return this.questionForm.type === '1' || this.questionForm.type === '2';
    },

    // 查看详情时判断是否为选择题
    isChoiceQuestionDetail() {
      return this.questionDetail.type === 1 || this.questionDetail.type === 2;
    }
  },
  mounted() {
    // 页面加载时获取试题列表
    this.loadQuestionList();
  },
  methods: {
    // 获取试题列表
    loadQuestionList() {
      // 调用API获取数据
      setTimeout(() => {
        this.$http.get('/api/GetQuestions', { params: { ...this.filterForm, page: this.currentPage, size: this.pageSize } })
            .then(response => {
              this.questionList = response.data.data.list;
              this.total = response.data.data.total;
            })
            .catch(error => {
              this.$message.error('加载试题列表失败');
              console.error(error);
            });
      }, 300);
    },

    // 处理搜索
    handleFilter() {
      this.currentPage = 1;
      this.loadQuestionList();
    },

    // 重置搜索条件
    resetFilter() {
      this.filterForm = {
        content: '',
        type: ''
      };
      this.currentPage = 1;
      this.loadQuestionList();
    },

    // 处理分页大小变化
    handleSizeChange(size) {
      this.pageSize = size;
      this.loadQuestionList();
    },

    // 处理页码变化
    handleCurrentChange(page) {
      this.currentPage = page;
      this.loadQuestionList();
    },

    // 处理行点击
    handleRowClick(row) {
      // 行点击事件处理
    },

    // 添加试题
    handleAddQuestion() {
      // 重置表单
      this.questionForm = {
        id: null,
        type: '1',
        content: '',
        options: [],
        answer: '',
        explanation: ''
      };
      this.dialogVisible = true;
    },

    // 编辑试题
    handleEditQuestion(row) {
      // 复制当前行数据到表单
      this.questionForm = { ...row };
      this.questionForm.type = String(row.type); // 确保类型为字符串
      this.dialogVisible = true;
    },

    // 保存试题
    saveQuestion() {
      this.$refs.questionFormRef.validate(valid => {
        if (valid) {
          // 模拟API请求
          setTimeout(() => {
            if (this.questionForm.id) {
              // 更新试题
              this.$http.put(`/api/updateQuestion`, this.questionForm)
                  .then(()=>{
                    this.$message.success('试题更新成功');
                  })
                  .catch(error=>{
                    this.$message.error('试题更新失败');
                    console.error(error);
                  })
            } else {
              this.$http.post('/api/AddQuestion', this.questionForm)
                .then(() => {
                  this.$message.success('试题添加成功');
                })
                .catch(error => {
                  this.$message.error('试题添加失败');
                  console.error(error);
                });
              }
            this.dialogVisible = false;
            this.loadQuestionList();
          }, 300);
        } else {
          return false;
        }
      });
    },

    // 删除试题
    handleDeleteQuestion(row) {
      this.currentQuestionId = row.id;
      this.deleteDialogVisible = true;
    },

    // 确认删除
    confirmDeleteQuestion() {
      // 模拟API请求
      setTimeout(() => {
        this.$http.delete(`/api/deleteQuestion/${this.currentQuestionId}`)
          .then(() => {
            this.$message.success('试题删除成功');
          })
          .catch(error => {
            this.$message.error('试题删除失败');
            console.error(error);
          });
        this.deleteDialogVisible = false;
        this.loadQuestionList();
      }, 300);
    },

    // 查看详情
    handleViewDetail(row) {
      this.questionDetail = { ...row };
      this.detailDialogVisible = true;
    },

    // 添加选项
    addOption() {
      if (!this.optionForm.key || !this.optionForm.value) {
        this.$message.warning('请输入选项标识和内容');
        return;
      }

      // 检查选项标识是否已存在
      const exists = this.questionForm.options.some(option => option.key === this.optionForm.key);
      if (exists) {
        this.$message.warning('选项标识已存在');
        return;
      }

      // 添加选项
      this.questionForm.options.push({
        key: this.optionForm.key,
        value: this.optionForm.value
      });

      // 清空表单
      this.optionForm = {
        key: '',
        value: ''
      };
    },

    // 移除选项
    removeOption(index) {
      this.questionForm.options.splice(index, 1);
    },

    // 获取题目类型名称
    getTypeName(type) {
      const typeMap = {
        1: '单选题',
        2: '多选题',
        3: '判断题',
        4: '填空题',
        5: '简答题'
      };
      return typeMap[type] || '未知类型';
    },

    // 获取题目类型标签样式
    getTypeTag(type) {
      const tagMap = {
        1: 'primary',
        2: 'success',
        3: 'info',
        4: 'warning',
        5: 'danger'
      };
      return tagMap[type] || 'default';
    }
  }
}
</script>

<style scoped>
.question-management {
  padding: 20px;
}

.filter-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.question-table-card {
  min-height: 400px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.el-table {
  margin-top: 10px;
}

.el-form-item {
  margin-bottom: 10px;
}

.el-dialog__body {
  padding-top: 10px;
}

.option-form {
  display: flex;
  align-items: center;
  gap: 10px;
}
</style>