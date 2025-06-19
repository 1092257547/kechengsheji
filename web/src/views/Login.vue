<template>
  <div class="login-container">
    <div class="login-box">
      <div class="logo">
        <h1>心灵守护</h1>
        <p>心理健康管理系统</p>
      </div>

      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
        <el-form-item prop="username">
          <el-input
              prefix-icon="el-icon-user"
              placeholder="用户名"
              v-model="loginForm.username"
          ></el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input
              prefix-icon="el-icon-lock"
              type="password"
              placeholder="密码"
              v-model="loginForm.password"
          ></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" class="login-btn" @click="handleLogin">
            登录
          </el-button>
        </el-form-item>
      </el-form>

      <div class="footer">
        <span>还没有账号?</span>
        <router-link to="/register">立即注册</router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      loginRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
        ]
      }
    };
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.$http.post('/api/login', this.loginForm)
              .then(response => {
                if (response && response.data.code === 200) {
                  //提取返回的用户信息
                  const userData = response.data.data;
                  // 存储用户信息、token 和角色信息
                  localStorage.setItem('user', JSON.stringify(userData.username));
                  const roles = userData.rid === 1 ? ['user'] : ['admin'];
                  localStorage.setItem('roles', JSON.stringify(roles));
                  // 根据角色信息跳转到不同的页面
                  if (roles.includes('admin')) {
                    this.$router.push('/admin/dashboard');
                  } else {
                    this.$router.push('/user/dashboard');
                  }
                } else {
                  console.error('登录成功但响应格式异常:', response);
                  this.$message.error('登录成功，但服务器响应异常');
                }
              })
              .catch(error => {
                this.$message.error('登录失败，请稍后重试');
              });
        }
      });
    }
  }
};
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.login-box {
  width: 400px;
  padding: 40px;
  background-color: rgba(255, 255, 255, 0.9);
  border-radius: 10px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
}

.logo {
  text-align: center;
  margin-bottom: 30px;
}

.logo h1 {
  font-size: 28px;
  color: #333;
  margin-bottom: 10px;
}

.logo p {
  font-size: 14px;
  color: #666;
}

.login-form {
  margin-top: 20px;
}

.login-btn {
  width: 100%;
  height: 40px;
  font-size: 16px;
  background-color: #4facfe;
  border-color: #4facfe;
}

.login-btn:hover {
  background-color: #3a96e6;
  border-color: #3a96e6;
}

.footer {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #666;
}

.footer a {
  color: #4facfe;
  text-decoration: none;
  margin-left: 5px;
}

.footer a:hover {
  text-decoration: underline;
}
</style>
