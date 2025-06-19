<template>
      <!-- 主要内容区域，用于渲染子路由组件 -->
      <el-main style="padding: 20px; background-color: #f3f3f4;">
        <router-view />
      </el-main>
</template>

<script>
export default {
  name: "UserLayout",
  data() {
    return {
      activeMenu: "/user/UserDashboard", // 默认激活的菜单
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
