<template>
  <div class="user-management-container">
    <el-card class="filter-card">
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <el-button type="primary" @click="handleAddUser">
            <el-icon><plus /></el-icon>
            添加用户
          </el-button>
        </div>
      </template>

      <el-form :inline="true" :model="filterForm" class="demo-form-inline">
        <el-form-item label="账户">
          <el-input v-model="filterForm.username" placeholder="请输入账户"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="filterForm.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="filterForm.gender" placeholder="请选择性别">
            <el-option label="男" value="male"></el-option>
            <el-option label="女" value="female"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleFilter">
            <el-icon><search /></el-icon>
            搜索
          </el-button>
          <el-button @click="resetFilter">
            <el-icon><refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="user-table-card">
      <el-table
          :data="userList"
          stripe
          border
          highlight-current-row
          @row-click="handleRowClick"
          style="width: 100%">
        <el-table-column type="index" label="序号" width="60"></el-table-column>
        <el-table-column prop="username" label="账户" width="120"></el-table-column>
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.gender === 'male' ? 'primary' : 'success'">
              {{ scope.row.gender === 'male' ? '男' : '女' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱"></el-table-column>
        <el-table-column prop="phone" label="电话" width="130"></el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button size="small" type="warning" @click="handleEditUser(scope.row)">
              <el-icon><edit /></el-icon>
              编辑
            </el-button>
            <el-button size="small" type="danger" @click="handleDeleteUser(scope.row)">
              <el-icon><delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

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

    <!-- 添加/编辑用户对话框 -->
    <el-dialog :visible.sync="dialogVisible" title="用户信息">
      <el-form :model="userForm" :rules="userFormRules" ref="userFormRef" label-width="100px">
        <el-form-item label="账户" prop="username">
          <el-input v-model="userForm.username"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="userForm.password" type="password"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="userForm.gender">
            <el-radio label="male">男</el-radio>
            <el-radio label="female">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="userForm.phone"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveUser">保存</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 确认删除对话框 -->
    <el-dialog :visible.sync="deleteDialogVisible" title="确认删除">
      <div slot="content">您确定要删除该用户吗？</div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="deleteDialogVisible = false">取消</el-button>
          <el-button type="danger" @click="confirmDeleteUser">确认删除</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'UserManagement',
  data() {
    return {
      userList: [],
      filterForm: {
        username: '',
        email: '',
        gender: ''
      },
      userForm: {
        id: null,
        username: '',
        password: '',
        gender: 'male',
        email: '',
        phone: ''
      },
      userFormRules: {
        username: [
          { required: true, message: '请输入账户', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入电话', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ]
      },
      currentPage: 1,
      pageSize: 10,
      total: 0,
      dialogVisible: false,
      deleteDialogVisible: false,
      currentUserId: null
    }
  },
  mounted() {
    this.loadUserList();
  },
  methods: {
    // 加载用户列表
    loadUserList() {
      // 模拟API请求
      setTimeout(() => {
        // 实际项目中应替换为真实API调用
        // axios.get('/api/users', { params: { ...this.filterForm, page: this.currentPage, size: this.pageSize } })
        //   .then(response => {
        //     this.userList = response.data.content;
        //     this.total = response.data.totalElements;
        //   })
        //   .catch(error => {
        //     this.$message.error('加载用户列表失败');
        //     console.error(error);
        //   });

        // 模拟数据
        this.userList = [
          { id: 1, username: 'admin', gender: 'male', email: 'admin@example.com', phone: '13800138000' },
          { id: 2, username: 'user1', gender: 'female', email: 'user1@example.com', phone: '13900139000' },
          { id: 3, username: 'user2', gender: 'male', email: 'user2@example.com', phone: '13700137000' },
          { id: 4, username: 'user3', gender: 'female', email: 'user3@example.com', phone: '13600136000' },
          { id: 5, username: 'user4', gender: 'male', email: 'user4@example.com', phone: '13500135000' }
        ];
        this.total = 20;
      }, 300);
    },

    // 处理搜索
    handleFilter() {
      this.currentPage = 1;
      this.loadUserList();
    },

    // 重置搜索条件
    resetFilter() {
      this.filterForm = {
        username: '',
        email: '',
        gender: ''
      };
      this.currentPage = 1;
      this.loadUserList();
    },

    // 处理分页大小变化
    handleSizeChange(size) {
      this.pageSize = size;
      this.loadUserList();
    },

    // 处理页码变化
    handleCurrentChange(page) {
      this.currentPage = page;
      this.loadUserList();
    },

    // 处理行点击
    handleRowClick(row) {
      // 可选：行点击事件处理
    },

    // 处理添加用户
    handleAddUser() {
      this.userForm = {
        id: null,
        username: '',
        password: '',
        gender: 'male',
        email: '',
        phone: ''
      };
      this.dialogVisible = true;
    },

    // 处理编辑用户
    handleEditUser(row) {
      this.userForm = { ...row };
      this.dialogVisible = true;
    },

    // 保存用户
    saveUser() {
      this.$refs.userFormRef.validate(valid => {
        if (valid) {
          // 模拟API请求
          setTimeout(() => {
            // 实际项目中应替换为真实API调用
            // if (this.userForm.id) {
            //   // 更新用户
            //   axios.put(`/api/users/${this.userForm.id}`, this.userForm)
            //     .then(() => {
            //       this.$message.success('用户更新成功');
            //       this.dialogVisible = false;
            //       this.loadUserList();
            //     })
            //     .catch(error => {
            //       this.$message.error('用户更新失败');
            //       console.error(error);
            //     });
            // } else {
            //   // 创建用户
            //   axios.post('/api/users', this.userForm)
            //     .then(() => {
            //       this.$message.success('用户创建成功');
            //       this.dialogVisible = false;
            //       this.loadUserList();
            //     })
            //     .catch(error => {
            //       this.$message.error('用户创建失败');
            //       console.error(error);
            //     });
            // }

            this.$message.success(this.userForm.id ? '用户更新成功' : '用户创建成功');
            this.dialogVisible = false;
            this.loadUserList();
          }, 300);
        } else {
          return false;
        }
      });
    },

    // 处理删除用户
    handleDeleteUser(row) {
      this.currentUserId = row.id;
      this.deleteDialogVisible = true;
    },

    // 确认删除用户
    confirmDeleteUser() {
      // 模拟API请求
      setTimeout(() => {
        // 实际项目中应替换为真实API调用
        // axios.delete(`/api/users/${this.currentUserId}`)
        //   .then(() => {
        //     this.$message.success('用户删除成功');
        //     this.deleteDialogVisible = false;
        //     this.loadUserList();
        //   })
        //   .catch(error => {
        //     this.$message.error('用户删除失败');
        //     console.error(error);
        //   });

        this.$message.success('用户删除成功');
        this.deleteDialogVisible = false;
        this.loadUserList();
      }, 300);
    }
  }
}
</script>

<style scoped>
.user-management-container {
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

.user-table-card {
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
</style>