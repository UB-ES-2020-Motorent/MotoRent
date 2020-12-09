import { mount, createLocalVue } from '@vue/test-utils'
import VueRouter from 'vue-router'
import BootstrapVue from 'bootstrap-vue'
import App from '@/App.vue'
import Home from '@/components/Home'
import Motos from '@/components/Motos'
import Users from '@/components/Users'
import router from '@/router/index'

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
    it('routing', async () => {
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
});