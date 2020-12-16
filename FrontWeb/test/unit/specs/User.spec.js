import { mount, createLocalVue } from '@vue/test-utils'
import VueRouter from 'vue-router'
import BootstrapVue from 'bootstrap-vue'
import Vuex from 'vuex'
import Home from '@/components/Home'
import router from '@/router/index'
import Rentals from '@/components/Rentals'
import Incidents from '@/components/Incidents'
import BankDatas from '@/components/Bankdatas'
import Payments from '@/components/Payments'
import User from '@/components/User'

const localVue = createLocalVue()
localVue.use(BootstrapVue)
localVue.use(VueRouter)

localVue.use(Vuex)

const store = new Vuex.Store({
  state: {
    token: ''
  },
  mutations: {
    setToken (state, newToken) {
      state.token = newToken
    }
  }
})


describe('User', () => {
    it('should mount for testing', () => {
      expect(1).toEqual(1);
    });
    it('should build all in-components', async () => {
      const wrapper = mount(User, { localVue, router, store });
      expect(wrapper.findComponent(Home).exists()).toBe(false)
      expect(wrapper.findComponent(BankDatas).exists()).toBe(true)
      expect(wrapper.findComponent(Payments).exists()).toBe(true)
      expect(wrapper.findComponent(Incidents).exists()).toBe(true)
      expect(wrapper.findComponent(Rentals).exists()).toBe(true)
      wrapper.destroy()
    });
});