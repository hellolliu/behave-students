import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/views/layout/Layout'

/** note: Submenu only appear when children.length>=1
 *  detail see  https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 **/

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    perms: ['GET /aaa','POST /bbb']     will control the page perms (you can set multiple perms)
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
    noCache: true                if true ,the page will no be cached(default is false)
  }
**/
export const constantRouterMap = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path*',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/authredirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/errorPage/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/errorPage/401'),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: 'dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: { title: '首页', icon: 'dashboard', affix: true }
      }
    ]
  }
]

export default new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

export const asyncRouterMap = [
  {
    path: '/sys',
    component: Layout,
    redirect: 'noredirect',
    alwaysShow: true,
    name: 'sysManage',
    meta: {
      title: '系统管理',
      icon: 'chart'
    },
    children: [
      {
        path: 'admin',
        component: () => import('@/views/sys/admin'),
        name: 'admin',
        meta: {
          perms: ['GET /admin/admin/list', 'POST /admin/admin/create', 'POST /admin/admin/update', 'POST /admin/admin/delete'],
          title: '管理员',
          noCache: true
        }
      },
      {
        path: 'log',
        component: () => import('@/views/sys/log'),
        name: 'log',
        meta: {
          perms: ['GET /admin/log/list'],
          title: '操作日志',
          noCache: true
        }
      },
      {
        path: 'role',
        component: () => import('@/views/sys/role'),
        name: 'role',
        meta: {
          perms: ['GET /admin/role/list', 'POST /admin/role/create', 'POST /admin/role/update', 'POST /admin/role/delete', 'GET /admin/role/permissions', 'POST /admin/role/permissions'],
          title: '角色管理',
          noCache: true
        }
      }
    ]
  },
  {
    path: '/behave',
    component: Layout,
    redirect: 'noredirect',
    alwaysShow: true,
    name: 'behaveManage',
    meta: {
      title: '功能中心',
      icon: 'chart'
    },
    children: [
      {
        path: 'class',
        component: () => import('@/views/behave/class'),
        name: 'class',
        meta: {
          perms: ['GET /admin/class/list', 'POST /admin/class/create', 'POST /admin/class/update', 'POST /admin/class/delete'],
          title: '班级管理',
          noCache: true
        }
      },
      {
        path: 'student',
        component: () => import('@/views/behave/student'),
        name: 'student',
        meta: {
          perms: ['GET /admin/student/list','GET /admin/student/read'],
          title: '学生管理',
          noCache: true
        }
      },
      {
        path: 'teacher',
        component: () => import('@/views/behave/teacher'),
        name: 'teacher',
        meta: {
          perms: ['GET /admin/teacher/list','POST /admin/teacher/create','GET /admin/teacher/read','POST /admin/teacher/update','POST /admin/teacher/delete'],
          title: '教师管理',
          noCache: true
        }
      },
      {
        path: 'course',
        component: () => import('@/views/behave/course'),
        name: 'course',
        meta: {
          perms: ['GET /admin/course/list','POST admin/course:create','GET /admin/course:read','POST /admin/course:update','POST /admin/course/delete','GET /admin/score/export'],
          title: '课程管理',
          noCache: true
        }
      },
      {
        path: 'schedule',
        component: () => import('@/views/behave/schedule'),
        name: 'schedule',
        meta: {
          perms: ['GET /admin/schedule/list','POST admin/schedule/create','GET admin/schedule/read','POST admin/schedule/update','POST admin/schedule/delete'],
          title: '课程表管理',
          noCache: true
        }
      },
      {
        path: 'schedulesolt',
        component: () => import('@/views/behave/schedulesolt'),
        name: 'schedulesolt',
        meta: {
          perms: ['GET /admin/scheduleSolt/list','POST /admin/scheduleSolt/create','GET /admin/scheduleSolt/read','POST admin/scheduleSolt/update','POST admin/scheduleSolt/delete'],
          title: '时间段管理',
          noCache: true
        }
      },
      {
        path: 'task',
        component: () => import('@/views/behave/task'),
        name: 'task',
        meta: {
          perms: ['GET /admin/task/list','POST /admin/task/create','GET /admin/task/read','POST /admin/task/update','POST /admin/task/delete'],
          title: '练习管理',
          noCache: true
        }
      }
    ]
  },
  {
    path: '/profile',
    component: Layout,
    redirect: 'noredirect',
    alwaysShow: true,
    children: [
      {
        path: 'password',
        component: () => import('@/views/profile/password'),
        name: 'password',
        meta: { title: '修改密码', noCache: true }
      }
    ],
    hidden: true
  },

  { path: '*', redirect: '/404', hidden: true }
]
