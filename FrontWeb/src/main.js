// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import '@/../bootstrap/css/bootstrap.css'
import BootstrapVue from 'bootstrap-vue'
import Vue from 'vue'
import App from './App'
import router from './router'
import firebase from 'firebase/app'
import axios from 'axios'

Vue.prototype.$axios = axios
Vue.use(BootstrapVue)
Vue.config.productionTip = false
Vue.prototype.$heroku = 'https://motorent-apitest.herokuapp.com'

const configOptions = {
  apiKey: 'AIzaSyAsADSDi32pUw1L_GMHmuQF5bIHODKALIQ',
  authDomain: 'motorent-7cb40.firebaseapp.com',
  databaseURL: 'https://motorent-7cb40.firebaseio.com',
  projectId: 'motorent-7cb40',
  storageBucket: 'motorent-7cb40.appspot.com',
  messagingSenderId: '657403564853',
  appId: '1:657403564853:web:4c7ed58267a6fc1939b27c'
}

firebase.initializeApp(configOptions)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  render: h => h(App),
  components: { App },
  template: '<App/>'
}).$mount('#app')
