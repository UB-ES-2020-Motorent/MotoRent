import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/Home'
import Motos from '@/components/Motos'

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
    }
  ]
})
