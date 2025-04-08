import Vue from 'vue';
//配置路由
import VueRouter from 'vue-router'
Vue.use(VueRouter);
//1.创建组件
import Index from '@/views/index'
import Home from '@/views/home'
import Login from '@/views/login'
import NotFound from '@/views/404'
import UpdatePassword from '@/views/update-password'
import pay from '@/views/pay'
import register from '@/views/register'
import center from '@/views/center'

         import users from '@/views/modules/users/list'
        import dictionary from '@/views/modules/dictionary/list'
        import geli from '@/views/modules/geli/list'
        import gonggao from '@/views/modules/gonggao/list'
        import shangbao from '@/views/modules/shangbao/list'
        import shequchuru from '@/views/modules/shequchuru/list'
        import zhuhu from '@/views/modules/zhuhu/list'
        import dictionaryGonggao from '@/views/modules/dictionaryGonggao/list'
        import dictionarySex from '@/views/modules/dictionarySex/list'
        import dictionaryShenti from '@/views/modules/dictionaryShenti/list'
        import dictionaryShequchuru from '@/views/modules/dictionaryShequchuru/list'
        import dictionaryZhuhu from '@/views/modules/dictionaryZhuhu/list'
        import dictionaryZhuhuHesuan from '@/views/modules/dictionaryZhuhuHesuan/list'
        import dictionaryZhuhuYimiao from '@/views/modules/dictionaryZhuhuYimiao/list'





//2.配置路由   注意：名字
const routes = [{
    path: '/index',
    name: '首页',
    component: Index,
    children: [{
      // 这里不设置值，是把main作为默认页面
      path: '/',
      name: '首页',
      component: Home,
      meta: {icon:'', title:'center'}
    }, {
      path: '/updatePassword',
      name: '修改密码',
      component: UpdatePassword,
      meta: {icon:'', title:'updatePassword'}
    }, {
      path: '/pay',
      name: '支付',
      component: pay,
      meta: {icon:'', title:'pay'}
    }, {
      path: '/center',
      name: '个人信息',
      component: center,
      meta: {icon:'', title:'center'}
    } ,{
        path: '/users',
        name: '管理信息',
        component: users
      }
    ,{
        path: '/dictionaryGonggao',
        name: '疫情通知类型名称',
        component: dictionaryGonggao
    }
    ,{
        path: '/dictionarySex',
        name: '性别类型名称',
        component: dictionarySex
    }
    ,{
        path: '/dictionaryShenti',
        name: '身体状况',
        component: dictionaryShenti
    }
    ,{
        path: '/dictionaryShequchuru',
        name: '登记类型',
        component: dictionaryShequchuru
    }
    ,{
        path: '/dictionaryZhuhu',
        name: '住户类型',
        component: dictionaryZhuhu
    }
    ,{
        path: '/dictionaryZhuhuHesuan',
        name: '是否有7日内核酸证明',
        component: dictionaryZhuhuHesuan
    }
    ,{
        path: '/dictionaryZhuhuYimiao',
        name: '是否注射疫苗',
        component: dictionaryZhuhuYimiao
    }


    ,{
        path: '/dictionary',
        name: '字典表',
        component: dictionary
      }
    ,{
        path: '/geli',
        name: '隔离监控',
        component: geli
      }
    ,{
        path: '/gonggao',
        name: '疫情通知',
        component: gonggao
      }
    ,{
        path: '/shangbao',
        name: '疫情上报',
        component: shangbao
      }
    ,{
        path: '/shequchuru',
        name: '社区人员出入',
        component: shequchuru
      }
    ,{
        path: '/zhuhu',
        name: '住户',
        component: zhuhu
      }


    ]
  },
  {
    path: '/login',
    name: 'login',
    component: Login,
    meta: {icon:'', title:'login'}
  },
  {
    path: '/register',
    name: 'register',
    component: register,
    meta: {icon:'', title:'register'}
  },
  {
    path: '/',
    name: '首页',
    redirect: '/index'
  }, /*默认跳转路由*/
  {
    path: '*',
    component: NotFound
  }
]
//3.实例化VueRouter  注意：名字
const router = new VueRouter({
  mode: 'hash',
  /*hash模式改为history*/
  routes // （缩写）相当于 routes: routes
})

export default router;
