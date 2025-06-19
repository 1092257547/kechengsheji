import Vue from 'vue'
import VueRouter from 'vue-router'
import UserDashboard from '../views/user/dashboard/UserDashboard.vue'
import ExamCenter from '../views/user/exam-center/ExamCenter.vue'
import ExamHistory from '../views/user/exam-history/ExamHistory.vue'
import Profile from '../views/user/profile/Profile.vue'
import AdminDashboard from '../views/admin/dashboard/AdminDashboard.vue'
import UserManagement from '../views/admin/user-management/UserManagement.vue'
import ExamManagement from '../views/admin/exam-management/ExamManagement.vue'
import AnswerManagement from '../views/admin/answer-management/AnswerManagement.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register'

Vue.use(VueRouter)

// 解决重复导航错误
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => {
    if (err.name !== 'NavigationDuplicated') throw err
  })
}

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { title: '登录 - 心灵守护' }
  },
    {
    path: '/register',
    name: 'Register',
    component: Register,
    meta: { title: '注册 - 心灵守护' }
  },
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/user',
    component: () => import('../components/layout/UserLayout.vue'),
    children: [
      {
        path: 'dashboard',
        name: 'UserDashboard',
        component: UserDashboard,
        meta: { title: '用户首页', requiresAuth: true }
      },
      {
        path: 'exam-center',
        name: 'ExamCenter',
        component: ExamCenter,
        meta: { title: '试卷中心', requiresAuth: true }
      },
      {
        path: 'exam-history',
        name: 'ExamHistory',
        component: ExamHistory,
        meta: { title: '答题记录', requiresAuth: true }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: Profile,
        meta: { title: '个人中心', requiresAuth: true }
      }
    ]
  },
  {
    path: '/admin',
    component: () => import('../components/layout/AdminLayout.vue'),
    children: [
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: AdminDashboard,
        meta: { title: '管理后台', requiresAuth: true, isAdmin: true }
      },
      {
        path: 'user-management',
        name: 'UserManagement',
        component: UserManagement,
        meta: { title: '用户管理', requiresAuth: true, isAdmin: true }
      },
      {
        path: 'exam-management',
        name: 'ExamManagement',
        component: ExamManagement,
        meta: { title: '试卷管理', requiresAuth: true, isAdmin: true }
      },
      {
        path: 'answer-management',
        name: 'AnswerManagement',
        component: AnswerManagement,
        meta: { title: '答卷管理', requiresAuth: true, isAdmin: true }
      }
    ]
  }
]

const router = new VueRouter({
  routes
})

// 全局前置守卫 - 权限控制
router.beforeEach((to, from, next) => {
  document.title = to.meta.title || '心灵守护 - 心理健康管理系统'

  const isLoggedIn = localStorage.getItem('user')
  const isAdmin = localStorage.getItem('roles')?.includes('admin')

  // 需要认证的页面
  if (to.meta.requiresAuth) {
    if (isLoggedIn) {
      // 管理员页面权限控制
      if (to.meta.isAdmin && !isAdmin) {
        next({ name: 'UserDashboard' })
      } else {
        next()
      }
    } else {
      next({ name: 'Login', query: { redirect: to.fullPath } })
    }
  } else {
    next()
  }
})

export default router