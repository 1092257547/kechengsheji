<template>
  <el-container style="min-height: 100vh;">
    <!-- 侧边栏区域 -->
    <el-aside width="200px" style="background-color: #2f4050;">
      <div class="admin-logo" @click="goToAdminHome">
        <img src="@/assets/logo.png" alt="系统logo" class="logo-img">
        <span class="logo-text">心灵守护</span>
      </div>
      <el-menu
          :default-active="activeMenu"
          class="admin-menu"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
          background-color="transparent"
          router
      >
        <el-menu-item index="/admin/dashboard">
          <el-icon><home /></el-icon>
          <span>管理首页</span>
        </el-menu-item>
        <el-submenu index="user">
          <template #title>
            <el-icon><user /></el-icon>
            <span>用户管理</span>
          </template>
          <el-menu-item index="/admin/user-management">用户列表</el-menu-item>
        </el-submenu>
        <el-submenu index="exam">
          <template #title>
            <el-icon><document /></el-icon>
            <span>试卷管理</span>
          </template>
          <el-menu-item index="/admin/exam-management">试卷列表</el-menu-item>
        </el-submenu>
        <el-submenu index="answer">
          <template #title>
            <el-icon><message /></el-icon>
            <span>答卷管理</span>
          </template>
          <el-menu-item index="/admin/answer-management">答卷列表</el-menu-item>
        </el-submenu>
      </el-menu>
    </el-aside>
    <!-- 右侧主容器 -->
    <el-container>
      <!-- 头部区域 -->
      <el-header style="background-color: #fff; border-bottom: 1px solid #e6e6e6; display: flex; justify-content: space-between; align-items: center; padding: 0 20px;">
        <div class="header-title">管理员后台</div>
        <div class="user-info">
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              <el-avatar :size="32" icon="el-icon-user" />
              <span class="username">{{ username }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <!-- 主要内容区域，用于渲染子路由组件 -->
      <el-main style="padding: 20px; background-color: #f3f3f4;">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: "AdminLayout",
  data() {
    return {
      activeMenu: "/admin/dashboard", // 默认激活的菜单
      username: "管理员", // 可从登录态获取真实用户名，这里先模拟
    };
  },
  methods: {
    goToAdminHome() {
      // 点击logo跳转管理首页
      this.$router.push("/admin/dashboard");
    },
    handleCommand(command) {
      if (command === "profile") {
        // 跳转到个人中心，若有单独页面可替换路径
        this.$message.info("前往个人中心（可扩展对应页面）");
      } else if (command === "logout") {
        // 退出登录逻辑，清除管理员登录态、跳转登录页等
        localStorage.removeItem("adminToken");
        this.$router.push("/login");
        this.$message.success("退出登录成功");
      }
    },
  },
  mounted() {
    // 从登录态等地方获取真实用户名，示例直接写死，实际可优化
    const userInfo = localStorage.getItem("adminUserInfo");
    if (userInfo) {
      this.username = JSON.parse(userInfo).username;
    }
  },
};
</script>

<style scoped>
.el-container {
  height: 100%;
}
.admin-logo {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px 0;
  cursor: pointer;
}
.logo-img {
  width: 40px;
  height: 40px;
  margin-right: 10px;
}
.logo-text {
  font-size: 18px;
  color: #fff;
  font-weight: bold;
}
.admin-menu {
  border-right: none;
}
.header-title {
  font-size: 16px;
  color: #333;
  font-weight: bold;
}
.user-info {
  display: flex;
  align-items: center;
}
.username {
  margin-right: 10px;
  color: #333;
}
</style>