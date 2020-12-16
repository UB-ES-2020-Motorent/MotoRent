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


describe('App', () => {
    it('should mount for testing', () => {
      expect(1).toEqual(1);
    });
    it('buttons contains motos', () => {
      const wrapper = mount(App, { localVue, router, store });
      expect(wrapper.find('[data-test="app-button-group"]').text()).toContain('Motos');
      wrapper.destroy()
    });
    it('buttons contains users', () => {
      const wrapper = mount(App, { localVue, router, store });
      expect(wrapper.find('[data-test="app-button-group"]').text()).toContain('Users');
      wrapper.destroy()
    });
    it('buttons contains rentals', () => {
      const wrapper = mount(App, { localVue, router, store });
      expect(wrapper.find('[data-test="app-button-group"]').text()).toContain('Rentals');
      wrapper.destroy()
    });
    it('routing home', async () => {
      const wrapper = mount(App, { localVue, router, store });
      try{
        await wrapper.vm.$nextTick()
      } catch(e) {
        console.log('Catch an error: ', e)
      }
      expect(wrapper.findComponent(Home).exists()).toBe(true)
      expect(wrapper.findComponent(Motos).exists()).toBe(false)
      wrapper.destroy()
    });
    it('buttons routes /motos correctly', async () => {
      const wrapper = mount(App, { localVue, router, store });
      try{
        await wrapper.find('input[id="motosID"]').trigger('click')
      } catch(e) {
        console.log('Catch an error: ', e)
      }
      expect(wrapper.findComponent(Motos).exists()).toBe(true)
      expect(wrapper.findComponent(Home).exists()).toBe(false)
      wrapper.destroy()
    });
    it('buttons routes /users correctly', async () => {
      const wrapper = mount(App, { localVue, router, store });
      try{
        await wrapper.find('input[id="usersID"]').trigger('click')
      } catch(e) {
        console.log('Catch an error: ', e)
      }
      expect(wrapper.findComponent(Users).exists()).toBe(true)
      expect(wrapper.findComponent(Home).exists()).toBe(false)
      wrapper.destroy()
    });
    it('buttons routes /rentals correctly', async () => {
      const wrapper = mount(App, { localVue, router, store });
      try{
        await wrapper.find('input[id="rentalsID"]').trigger('click')
      } catch(e) {
        console.log('Catch an error: ', e)
      }
      expect(wrapper.findComponent(Rentals).exists()).toBe(true)
      expect(wrapper.findComponent(Home).exists()).toBe(false)
      wrapper.destroy()
    });
    it('buttons routes /incidents correctly', async () => {
      const wrapper = mount(App, { localVue, router, store });
      try{
        await wrapper.find('input[id="incidentsID"]').trigger('click')
      } catch(e) {
        console.log('Catch an error: ', e)
      }
      expect(wrapper.findComponent(Incidents).exists()).toBe(true)
      expect(wrapper.findComponent(Home).exists()).toBe(false)
      wrapper.destroy()
    });
    it('buttons routes /bankdatas correctly', async () => {
      const wrapper = mount(App, { localVue, router, store });
      try{
        await wrapper.find('input[id="bankdatasID"]').trigger('click')
      } catch(e) {
        console.log('Catch an error: ', e)
      }
      expect(wrapper.findComponent(BankDatas).exists()).toBe(true)
      expect(wrapper.findComponent(Home).exists()).toBe(false)
      wrapper.destroy()
    });
    it('buttons routes /payments correctly', async () => {
      const wrapper = mount(App, { localVue, router, store });
      try{
        await wrapper.find('input[id="paymentsID"]').trigger('click')
      } catch(e) {
        console.log('Catch an error: ', e)
      }
      expect(wrapper.findComponent(Payments).exists()).toBe(true)
      expect(wrapper.findComponent(Home).exists()).toBe(false)
      wrapper.destroy()
    });
    it('buttons routes /statistics correctly', async () => {
      const wrapper = mount(App, { localVue, router, store });
      try{
        await wrapper.find('input[id="statisticsID"]').trigger('click')
      } catch(e) {
        console.log('Catch an error: ', e)
      }
      expect(wrapper.findComponent(Statistics).exists()).toBe(true)
      expect(wrapper.findComponent(Home).exists()).toBe(false)
      wrapper.destroy()
    });
    it('buttons routes /mapcoords correctly', async () => {
      const wrapper = mount(App, { localVue, router, store });
      try{
        await wrapper.find('input[id="mapcoordsID"]').trigger('click')
      } catch(e) {
        console.log('Catch an error: ', e)
      }
      expect(wrapper.findComponent(MapCoords).exists()).toBe(true)
      expect(wrapper.findComponent(Home).exists()).toBe(false)
      wrapper.destroy()
    });
    it('routing home button', async () => {
      const wrapper = mount(App, { localVue, router, store });
      try{
        await wrapper.find('input[id="homeID"]').trigger('click')
      } catch(e) {
        console.log('Catch an error: ', e)
      }
      expect(wrapper.findComponent(Home).exists()).toBe(true)
      expect(wrapper.findComponent(Motos).exists()).toBe(false)
      wrapper.destroy()
    });
});