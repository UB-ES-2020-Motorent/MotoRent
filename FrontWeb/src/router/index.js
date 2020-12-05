import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/Home'
import Motos from '@/components/Motos'
import Users from '@/components/Users'
Vue.use(Router)

export default new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/motos',
      name: 'Motos',
      component: Motos
    },
    {
      path: '/users',
      name: 'Usuaris',
      component: Users
    }
  ]
})
