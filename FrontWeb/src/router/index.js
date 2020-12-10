import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/Home'
import Motos from '@/components/Motos'
import Users from '@/components/Users'
import Incidencies from '@/components/Incidents'
import Bankdatas from '@/components/Bankdatas'
import Payments from '@/components/Payments'
import Moto from '@/components/Moto'

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
    },
    {
      path: '/incidents',
      name: 'Incidencies',
      component: Incidencies
    },
    {
      path: '/bankdatas',
      name: 'BankDatas',
      component: Bankdatas
    },
    {
      path: '/payments',
      name: 'Payments',
      component: Payments
    },
    {
      path: '/moto/:id',
      name: 'Moto',
      component: Moto
    }
  ]
})
