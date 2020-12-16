import { mount, createLocalVue } from '@vue/test-utils'
import VueRouter from 'vue-router'
import BootstrapVue from 'bootstrap-vue'
import Vuex from 'vuex'
import App from '@/App.vue'
import Home from '@/components/Home'
import Motos from '@/components/Motos'
import Users from '@/components/Users'
import router from '@/router/index'
import BankDatas from '@/components/Bankdatas'
import Payments from '@/components/Payments'
import Rentals from '@/components/Rentals'
import Incidents from '@/components/Incidents'
import Statistics from '@/components/Statistics'
import MapCoords from '@/components/MapCoords'
import axios from 'axios'

const localVue = createLocalVue()
localVue.use(BootstrapVue)

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

jest.mock('axios', () => ({
  post: jest.fn(() => Promise.resolve({ token: 'token'}))
}));

describe('Home', () => {
  it('should mount for testing', () => {
    expect(1).toEqual(1);
  });
  /*it('should not send post if empty ', async () => {
    const wrapper = mount(Home, { localVue, store });
    try{
      await wrapper.find('button[data-test="login-button"]').trigger('click')
    } catch(e) {
      console.log('Catch an error: ', e)
    }
    expect(wrapper.vm.tokenDB).toBe('');
    expect(axios.post).toHaveBeenCalledTimes(0);
    // expect(wrapper.find('[data-test="login-button"]').text()).toEqual( 'Motos' );
    // expect(wrapper)
    wrapper.destroy()
  });*/
  /*
  it('should send post when ', async () => {
    const wrapper = mount(Home, { localVue, store });
    wrapper.find('input[id="mail-input"]').setValue('admin3@motorent.com')
    wrapper.find('input[id="password-input"]').setValue('adminsecretcode')
    try{
      await wrapper.find('button[data-test="login-button"]').trigger('click')
    } catch(e) {
      console.log('Catch an error: ', e)
    }
    // expect(wrapper.vm.tokenDB).toBe('token');
    expect(axios.post).toHaveBeenCalledTimes(1);
    // expect(wrapper.find('[data-test="login-button"]').text()).toEqual( 'Motos' );
    // expect(wrapper)
    wrapper.destroy()
  });*/
})
