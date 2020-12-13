import { mount, createLocalVue } from '@vue/test-utils'
import VueRouter from 'vue-router'
import BootstrapVue from 'bootstrap-vue'
import App from '@/App.vue'
import Home from '@/components/Home'
import Motos from '@/components/Motos'
import Users from '@/components/Users'
import router from '@/router/index'
import BankDatas from '@/components/Bankdatas'
import Payments from '@/components/Payments'
import Rentals from '@/components/Rentals'
import Incidents from '@/components/Incidents'

const localVue = createLocalVue()
localVue.use(BootstrapVue)
localVue.use(VueRouter)

describe('App', () => {
    it('should mount for testing', () => {
      expect(1).toEqual(1);
    });
    it('buttons contains motos', () => {
      const wrapper = mount(App, { localVue, router });
      expect(wrapper.find('[data-test="app-button-group"]').text()).toContain('Motos');
    });
    it('buttons contains users', () => {
      const wrapper = mount(App, { localVue, router });
      expect(wrapper.find('[data-test="app-button-group"]').text()).toContain('Users');
    });
    it('buttons contains rentals', () => {
      const wrapper = mount(App, { localVue, router });
      expect(wrapper.find('[data-test="app-button-group"]').text()).toContain('Rentals');
    });
    it('routing home', async () => {
      const wrapper = mount(App, { localVue, router });
      try{
        await wrapper.vm.$nextTick()
      } catch(e) {
        console.log('Catch an error: ', e)
      }
      expect(wrapper.findComponent(Home).exists()).toBe(true)
      expect(wrapper.findComponent(Motos).exists()).toBe(false)
    });
    it('buttons routes /motos correctly', async () => {
      const wrapper = mount(App, { localVue, router });
      try{
        await wrapper.find('input[id="motosID"]').trigger('click')
      } catch(e) {
        console.log('Catch an error: ', e)
      }
      expect(wrapper.findComponent(Motos).exists()).toBe(true)
      expect(wrapper.findComponent(Home).exists()).toBe(false)
    });
    it('buttons routes /users correctly', async () => {
      const wrapper = mount(App, { localVue, router });
      try{
        await wrapper.find('input[id="usersID"]').trigger('click')
      } catch(e) {
        console.log('Catch an error: ', e)
      }
      expect(wrapper.findComponent(Users).exists()).toBe(true)
      expect(wrapper.findComponent(Home).exists()).toBe(false)
    });
    it('buttons routes /rentals correctly', async () => {
      const wrapper = mount(App, { localVue, router });
      try{
        await wrapper.find('input[id="rentalsID"]').trigger('click')
      } catch(e) {
        console.log('Catch an error: ', e)
      }
      expect(wrapper.findComponent(Rentals).exists()).toBe(true)
      expect(wrapper.findComponent(Home).exists()).toBe(false)
    });
    it('buttons routes /incidents correctly', async () => {
      const wrapper = mount(App, { localVue, router });
      try{
        await wrapper.find('input[id="incidentsID"]').trigger('click')
      } catch(e) {
        console.log('Catch an error: ', e)
      }
      expect(wrapper.findComponent(Incidents).exists()).toBe(true)
      expect(wrapper.findComponent(Home).exists()).toBe(false)
    });
    it('buttons routes /bankdatas correctly', async () => {
      const wrapper = mount(App, { localVue, router });
      try{
        await wrapper.find('input[id="bankdatasID"]').trigger('click')
      } catch(e) {
        console.log('Catch an error: ', e)
      }
      expect(wrapper.findComponent(BankDatas).exists()).toBe(true)
      expect(wrapper.findComponent(Home).exists()).toBe(false)
    });
    it('buttons routes /payments correctly', async () => {
      const wrapper = mount(App, { localVue, router });
      try{
        await wrapper.find('input[id="paymentsID"]').trigger('click')
      } catch(e) {
        console.log('Catch an error: ', e)
      }
      expect(wrapper.findComponent(Payments).exists()).toBe(true)
      expect(wrapper.findComponent(Home).exists()).toBe(false)
    });
});