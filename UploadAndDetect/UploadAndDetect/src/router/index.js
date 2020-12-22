import Vue from 'vue'
import Router from 'vue-router'
import View from '@/components/View'
import SetConfig from '@/components/SetConfig'
import Detect from '@/components/Detect'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      redirect: '/detect'
    },
    {
      path:'/detect',
      name: 'Detect',
      component: Detect
    },
    {
      path:'/view',
      name: 'View',
      component: View
    },
    {
      path:'/set',
      name:'Set',
      component:SetConfig
    },
  ]
})
