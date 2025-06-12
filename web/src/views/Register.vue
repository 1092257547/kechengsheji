<template>
  <div class="register-container">
    <div class="register-box">
      <div class="logo">
        <h1>心灵守护</h1>
        <p>创建您的心理健康账户</p>
      </div>

      <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="register-form">
        <el-form-item prop="username">
          <el-input
              prefix-icon="el-icon-user"
              placeholder="用户名"
              v-model="registerForm.username"
          ></el-input>
        </el-form-item>

        <el-form-item prop="email">
          <el-input
              prefix-icon="el-icon-message"
              placeholder="邮箱"
              v-model="registerForm.email"
          ></el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input
              prefix-icon="el-icon-lock"
              type="password"
              placeholder="密码"
              v-model="registerForm.password"
          ></el-input>
        </el-form-item>

        <el-form-item prop="confirmPassword">
          <el-input
              prefix-icon="el-icon-lock"
              type="password"
              placeholder="确认密码"
              v-model="registerForm.confirmPassword"
          ></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" class="register-btn" @click="handleRegister">
            注册
          </el-button>
        </el-form-item>
      </el-form>

      <div class="footer">
        <span>已有账号?</span>
        <router-link to="/login">立即登录</router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      registerForm: {
        username: '',
        email: '',
        password: '',
        confirmPassword: ''
      },
      registerRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名长度在3到20个字符之间', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          { validator: this.validateConfirmPassword, trigger: 'blur' }
        ]
      }
    };
  },
  methods: {
    validateConfirmPassword(rule, value, callback) {
      if (value !== this.registerForm.password) {
        callback(new Error('两次输入的密码不一致'));
      } else {
        callback();
      }
    },
    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          // 移除确认密码字段，不发送到后端
          const userData = { ...this.registerForm };
          delete userData.confirmPassword;

          this.$http.post('/api/register', userData)
              .then(response => {
                this.$message.success('注册成功，请登录');
                this.$router.push('/login');
              })
              .catch(error => {
                this.$message.error(error.response.data || '注册失败，请稍后重试');
              });
        }
      });
    }
  }
};
</script>

<style scoped>
.register-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #00f2fe 0%, #4facfe 100%);
}

.register-box {
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

.register-form {
  margin-top: 20px;
}

.register-btn {
  width: 100%;
  height: 40px;
  font-size: 16px;
  background-color: #00f2fe;
  border-color: #00f2fe;
}

.register-btn:hover {
  background-color: #00d9e6;
  border-color: #00d9e6;
}

.footer {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #666;
}

.footer a {
  color: #00f2fe;
  text-decoration: none;
  margin-left: 5px;
}

.footer a:hover {
  text-decoration: underline;
}
</style>
