import Vue from 'vue';
import Router from 'vue-router';
import Login from '../views/Login.vue';
import Register from '../views/Register.vue';

Vue.use(Router);

const router = new Router({
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/register',
      name: 'Register',
      component: Register
    },
    {
      path: '*',
      redirect: '/login'
    }
  ]
});

// 路由守卫，验证登录状态
router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth) {
    // 需要登录的页面
    const user = localStorage.getItem('user');
    if (!user) {
      // 未登录，跳转到登录页
      next({ name: 'Login' });
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router;
