<template>
  <div class="paper-management">
    <!-- 顶部操作栏 -->
    <el-card class="header-card">
      <template #header>
        <div class="card-header">
          <span>试卷管理</span>
          <el-button type="primary" @click="handleAddPaper">
            <el-icon><Plus /></el-icon>新增试卷
          </el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :inline="true" :model="filterForm" class="search-form">
        <el-form-item label="试卷标题">
          <el-input v-model="filterForm.title" placeholder="请输入试卷标题关键词"></el-input>
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

    <!-- 试卷列表 -->
    <el-card class="paper-table-card" >
      <el-table
          :data="paperList"
          stripe
          border
          highlight-current-row
          @row-click="handleRowClick"
          style="width: 100%">
        <el-table-column type="index" label="序号" width="60"></el-table-column>
        <el-table-column prop="title" label="试卷标题"></el-table-column>
        <el-table-column prop="totalScore" label="总分" width="100"></el-table-column>
        <el-table-column prop="passScore" label="及格分" width="100"></el-table-column>
        <el-table-column prop="questions.length" label="试题数量" width="100"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button size="small" type="warning" @click="handleEditPaper(scope.row)">
              <el-icon><Edit /></el-icon>编辑
            </el-button>
            <el-button size="small" type="danger" @click="handleDeletePaper(scope.row)">
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

    <!-- 添加/编辑试卷对话框 -->
    <el-dialog :visible.sync="dialogVisible" title="试卷信息">
      <el-form :model="paperForm" :rules="paperFormRules" ref="paperFormRef" label-width="100px">
        <el-form-item label="试卷标题" prop="title">
          <el-input v-model="paperForm.title"></el-input>
        </el-form-item>
        <el-form-item label="总分" prop="totalScore">
          <el-input-number v-model="paperForm.totalScore" :min="0" :max="1000" step="1"></el-input-number>
        </el-form-item>
        <el-form-item label="及格分" prop="passScore">
          <el-input-number v-model="paperForm.passScore" :min="0" :max="1000" step="1"></el-input-number>
        </el-form-item>

        <!-- 试题选择区域 -->
        <el-form-item label="选择试题">
          <el-button type="primary" size="small" @click="openQuestionSelector">
            <el-icon><Plus /></el-icon>选择试题
          </el-button>

          <el-table
              :data="paperForm.questions || []"
              stripe
              style="width: 100%; margin-top: 10px"
              size="small">
            <el-table-column type="index" label="序号" width="50"></el-table-column>
            <el-table-column prop="content" label="试题内容"></el-table-column>
            <el-table-column prop="score" label="分值" width="80"></el-table-column>
            <el-table-column label="操作" width="80">
              <template #default="scope">
                <el-button size="mini" type="danger" @click="removeQuestion(scope.$index)">
                  <el-icon><Delete /></el-icon>
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="savePaper">保存</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 试题选择对话框 -->
    <el-dialog :visible.sync="questionSelectorVisible" title="选择试题">
      <el-form :inline="true" :model="questionFilterForm" class="search-form">
        <el-form-item label="题目内容">
          <el-input v-model="questionFilterForm.content" placeholder="请输入题目关键词"></el-input>
        </el-form-item>
        <el-form-item label="题目类型">
          <el-select v-model="questionFilterForm.type" placeholder="请选择题目类型">
            <el-option label="单选题" value="1"></el-option>
            <el-option label="多选题" value="2"></el-option>
            <el-option label="判断题" value="3"></el-option>
            <el-option label="填空题" value="4"></el-option>
            <el-option label="简答题" value="5"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="filterQuestions">
            <el-icon><Search /></el-icon>搜索
          </el-button>
          <el-button @click="resetQuestionFilter">
            <el-icon><Refresh /></el-icon>重置
          </el-button>
        </el-form-item>
      </el-form>

      <el-table
          ref="questionTable"
          :data="questionList"
          stripe
          border
          @row-click="handleQuestionRowClick"
          style="width: 100%">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column type="index" label="序号" width="60"></el-table-column>
        <el-table-column prop="content" label="题目内容"></el-table-column>
        <el-table-column prop="type" label="题目类型" width="100">
          <template #default="scope">
            <el-tag :type="getTypeTag(scope.row.type)">{{ getTypeName(scope.row.type) }}</el-tag>
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="questionSelectorVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmSelectQuestions">确认选择</el-button>
      </div>
    </el-dialog>

    <!-- 确认删除对话框 -->
    <el-dialog :visible.sync="deleteDialogVisible" title="确认删除">
      <div slot="content">您确定要删除该试卷吗？</div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="deleteDialogVisible = false">取消</el-button>
          <el-button type="danger" @click="confirmDeletePaper">确认删除</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'PaperManagement',
  data() {
    return {
      // 筛选表单
      filterForm: {
        title: ''
      },

      // 试卷列表数据
      paperList: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,

      // 添加/编辑表单
      paperForm: {
        id: null,
        title: '',
        totalScore: 100,
        passScore: 60,
        questions: []
      },

      // 表单验证规则
      paperFormRules: {
        title: [
          { required: true, message: '请输入试卷标题', trigger: 'blur' }
        ],
        totalScore: [
          { required: true, message: '请输入总分', trigger: 'blur' },
          { type: 'number', message: '总分必须为数字值', trigger: 'blur' }
        ],
        passScore: [
          { required: true, message: '请输入及格分', trigger: 'blur' },
          { type: 'number', message: '及格分必须为数字值', trigger: 'blur' },
          { validator: (rule, value, callback) => {
              if (value > this.paperForm.totalScore) {
                callback(new Error('及格分不能大于总分'));
              } else {
                callback();
              }
            }, trigger: 'blur' }
        ],
        questions: [
          { validator: (rule, value, callback) => {
              if (!value || value.length === 0) {
                callback(new Error('试卷至少需要包含一道试题'));
              } else {
                callback();
              }
            }, trigger: 'change' }
        ]
      },

      // 试题选择相关
      questionSelectorVisible: false,
      questionFilterForm: {
        content: '',
        type: ''
      },
      questionList: [],
      selectedQuestions: [],

      // 对话框状态
      dialogVisible: false,
      deleteDialogVisible: false,

      // 当前操作的试卷ID
      currentPaperId: null
    }
  },
  mounted() {
    // 页面加载时获取试卷列表
    this.loadPaperList();
  },
  methods: {
    // 获取试卷列表
    loadPaperList() {
      // 调用API获取数据
      setTimeout(() => {
        this.$http.get("/api/GetExamPapers", {params: {...this.filterForm, page: this.currentPage, size: this.pageSize}})
            .then(response=>{
              this.$set(this, 'paperList', response.data.data.list);
              this.$set(this, 'total', response.data.data.total);
              // this.$message.success("获取试卷列表成功");
            }).catch(error=>{
              this.$message.error("加载试卷列表失败");
        })
      }, 300);
    },

    // 获取试题列表
    loadQuestionList() {
      // API返回数据
      setTimeout(() => {
        this.$http.get('/api/GetQuestions', { params: { ...this.questionFilterForm, page: this.currentPage, size: this.pageSize } })
            .then(response => {
              this.questionList = response.data.data.list;
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
      this.loadPaperList();
    },

    // 重置搜索条件
    resetFilter() {
      this.filterForm = {
        title: ''
      };
      this.currentPage = 1;
      this.loadPaperList();
    },

    // 处理分页大小变化
    handleSizeChange(size) {
      this.pageSize = size;
      this.loadPaperList();
    },

    // 处理页码变化
    handleCurrentChange(page) {
      this.currentPage = page;
      this.loadPaperList();
    },

    // 处理行点击
    handleRowClick(row) {
      // 可选：行点击事件处理
    },

    // 添加试卷
    handleAddPaper() {
      // 重置表单
      this.paperForm = {
        id: null,
        title: '',
        totalScore: 100,
        passScore: 60,
        questions: []
      };
      this.dialogVisible = true;
    },

    // 编辑试卷
    handleEditPaper(row) {
      // 复制当前行数据到表单
      this.paperForm = { ...row };
      this.dialogVisible = true;
    },

    // 保存试卷
    savePaper() {
      this.$refs.paperFormRef.validate(valid => {
        if (valid) {
          // 计算总分
          this.paperForm.totalScore = this.paperForm.questions.reduce((sum, question) => {
            return sum + (question.score || 0);
          }, 0);

          // 确保及格分不超过总分
          if (this.paperForm.passScore > this.paperForm.totalScore) {
            this.paperForm.passScore = this.paperForm.totalScore;
          }

          // API请求
          setTimeout(() => {
            if (this.paperForm.id) {
              // 更新试卷
              this.$http.put('/api/UpdateExamPaper', this.paperForm)
                  .then(response => {
                    this.$message.success('试卷更新成功');
                  })
                  .catch(error => {
                    this.$message.error('更新试卷失败');
                    console.error(error);
                  });
            } else {
              // 添加试卷
              this.$http.post('/api/AddExamPaper', this.paperForm)
                  .then(response => {
                    this.$message.success('试卷添加成功');
                  })
                  .catch(error => {
                    this.$message.error('添加试卷失败');
                    console.error(error);
                  });
            }
            this.dialogVisible = false;
            this.loadPaperList();
          }, 300);
        } else {
          return false;
        }
      });
    },

    // 删除试卷
    handleDeletePaper(row) {
      this.currentPaperId = row.id;
      this.deleteDialogVisible = true;
    },

    // 确认删除
    confirmDeletePaper() {
      // API请求
      setTimeout(() => {
        this.$http.delete(`/api/deleteExamPaper/${this.currentPaperId}`)
            .then(response=> {
              this.$message.success('试卷删除成功');
            })
            .catch(error=>{
              this.$message.error('试卷删除失败');
            })
        this.deleteDialogVisible = false;
        this.loadPaperList();
      }, 300);
    },

    // 打开试题选择器
    openQuestionSelector() {
      this.questionSelectorVisible = true;
      this.loadQuestionList();

      // 过滤掉已选试题
      if (this.paperForm.questions && this.paperForm.questions.length > 0) {
        const selectedIds = this.paperForm.questions.map(q => q.id);
        this.questionList = this.questionList.filter(q => !selectedIds.includes(q.id));
      }
    },

    // 筛选试题
    filterQuestions() {
      this.loadQuestionList();
    },

    // 重置试题筛选条件
    resetQuestionFilter() {
      this.questionFilterForm = {
        content: '',
        type: ''
      };
      this.loadQuestionList();
    },

    // 处理试题行点击
    handleQuestionRowClick(row) {
      this.$refs.questionTable.toggleRowSelection(row);
    },

    // 确认选择试题
    confirmSelectQuestions() {
      const selectedRows = this.$refs.questionTable.selection;
      if (selectedRows && selectedRows.length > 0) {
        // 添加到试卷试题列表
        this.paperForm.questions = [...this.paperForm.questions, ...selectedRows];

        // 自动计算总分
        this.paperForm.totalScore = this.paperForm.questions.reduce((sum, question) => {
          return sum + (question.score || 0);
        }, 0);

        // 如果及格分大于总分，调整及格分
        if (this.paperForm.passScore > this.paperForm.totalScore) {
          this.paperForm.passScore = this.paperForm.totalScore;
        }

        this.$message.success(`已添加 ${selectedRows.length} 道试题`);
      }
      this.questionSelectorVisible = false;
    },

    // 移除试题
    removeQuestion(index) {
      // 从列表中移除
      const removedQuestion = this.paperForm.questions.splice(index, 1)[0];

      // 更新总分
      this.paperForm.totalScore -= removedQuestion.score || 0;

      // 如果及格分大于总分，调整及格分
      if (this.paperForm.passScore > this.paperForm.totalScore) {
        this.paperForm.passScore = this.paperForm.totalScore;
      }
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
.paper-management {
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

.paper-table-card {
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

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>