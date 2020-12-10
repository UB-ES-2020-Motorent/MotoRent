import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/Home'
import Motos from '@/components/Motos'
import Users from '@/components/Users'
import Rentals from '@/components/Rentals'
import Incidents from '@/components/Incidents'

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
    }
  ]
})
