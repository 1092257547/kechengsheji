<template>
  <el-container style="height: 100vh;">
    <!-- 侧边栏 -->
    <el-aside width="200px" style="background-color: #304156; color: white;">
      <div class="logo" @click="goHome">心灵守护</div>
      <el-menu
          :default-active="activeMenu"
          class="el-menu-vertical-demo custom-menu"
          router
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
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
          <!-- 二级菜单，添加 custom-sub-menu-item 类名用于样式控制 -->
          <el-menu-item class="custom-sub-menu-item" index="/admin/user-management">用户列表</el-menu-item>
        </el-submenu>
        <el-submenu index="exam">
          <template #title>
            <el-icon><document /></el-icon>
            <span>试卷管理</span>
          </template>
          <el-menu-item class="custom-sub-menu-item" index="/admin/exam-management">试卷列表</el-menu-item>
          <el-menu-item class="custom-sub-menu-item" index="/admin/question-management">试题列表</el-menu-item>
        </el-submenu>
        <el-submenu index="answer">
          <template #title>
            <el-icon><message /></el-icon>
            <span>答卷管理</span>
          </template>
          <el-menu-item class="custom-sub-menu-item" index="/admin/answer-management">答卷列表</el-menu-item>
        </el-submenu>
      </el-menu>
    </el-aside>

    <!-- 主内容区 -->
    <el-container>
      <el-header style="background-color: #fff; padding: 0 20px; display: flex; justify-content: space-between; align-items: center;">
        <div class="header-title">{{ pageTitle }}</div>
        <div class="user-info">
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              <el-avatar :size="32" icon="el-icon-user" />
              <span>{{ username }}</span>
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

      <el-main style="padding: 20px; background-color: #f5f7fa;">
        <!-- 子路由内容将在这里渲染 -->
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'AdminLayout',
  data() {
    return {
      username: this.getUsername(),
      activeMenu: this.$route.path
    }
  },
  computed: {
    pageTitle() {
      // 从路由meta获取页面标题
      return this.$route.meta.title || '首页';
    }
  },
  methods: {
    goHome() {
      this.$router.push('/admin/dashboard');
    },
    handleCommand(command) {
      if (command === 'logout') {
        localStorage.removeItem('user');
        this.$router.push('/login');
      }
    },
    getUsername() {
      const user = JSON.parse(localStorage.getItem('user'));
      return user ? user : '';
    }
  },
  watch: {
    // 监听路由变化，更新活跃菜单
    '$route.path': function (newPath) {
      this.activeMenu = newPath;
    }
  }
}
</script>

<style scoped>
/* 侧边栏整体样式 */
.el-aside {
  display: flex;
  flex-direction: column;
}
.logo {
  text-align: center;
  padding: 20px 0;
  cursor: pointer;
  font-size: 18px;
  font-weight: bold;
}
/* 自定义菜单类名，用于覆盖默认样式 */
.custom-menu {
  border-right: none;
}
/* 二级菜单样式，设置缩进，实现靠右效果 */
.custom-sub-menu-item {
  padding-left: 60px !important; /* 比一级菜单更靠右，可根据需求调整 */
}
</style>