import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/Home'
import Motos from '@/components/Motos'
import Users from '@/components/Users'
import Bankdatas from '@/components/Bankdatas'
import Payments from '@/components/Payments'
import Moto from '@/components/Moto'
import Rentals from '@/components/Rentals'
import Incidents from '@/components/Incidents'
import User from '@/components/User'

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
      name: 'Users',
      component: Users
    },
    {
      path: '/rentals',
      name: 'Rentalss',
      component: Rentals
    },
    {
      path: '/incidents',
      name: 'Incidents',
      component: Incidents
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
    },
    {
      path: '/user/:id',
      name: 'User',
      component: User
    }
  ]
})
