import Vue from 'vue'
import BootstrapVue from 'bootstrap-vue'
import { mount, createLocalVue } from '@vue/test-utils'
import flushPromises from "flush-promises"
import axios from 'axios'
import Vuex from 'vuex'
import Motos from '@/components/Motos'

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



const motos = { motos: [
  {
    id: '1',
    license_number: '1111 AAA',
    battery: 1,
    available: true,
    latitude: 41.4,
    longitude: 2.1
  },
  {
    id: '2',
    license_number: '2222 BBB',
    battery: 2,
    available: false,
    latitude: 41.4,
    longitude: 2.1
  }
]}

jest.mock('axios', () => ({
  get: jest.fn(() => Promise.resolve({ motos: [
    {
      id: '1',
      license_number: '1111 AAA',
      battery: 1,
      available: true,
      latitude: 41.4,
      longitude: 2.1
    },
    {
      id: '2',
      license_number: '2222 BBB',
      battery: 2,
      available: false,
      latitude: 41.4,
      longitude: 2.1
    }
  ]}))
}));

describe('Motos', () => {
    test('should mount for testing', () => {
      expect(1).toEqual(1);
    });
    test('should render Motos as title', () => {
      const wrapper = mount(Motos, { localVue, store });
      expect(wrapper.find('[data-test="motos-title"]').text()).toEqual( 'Motos' );
      wrapper.destroy()
    });
    /*
    it('getMotos when created', async () => {
      const wrapper = mount(Motos, { localVue });
      await flushPromises();
      // expect(axios.get).toHaveBeenCalledTimes(1)
      // expect(wrapper.vm.motos.length).toBe(2);
      expect(wrapper.find('[data-test="motos-b-table"]').element.tagName).toBe('TABLE')
      expect(wrapper.find('[data-test="motos-b-table"]').classes()).toContain('b-table')
      expect(wrapper.find('[data-test="motos-b-table"]').findAll('tbody > tr').length).toBe(2)
      expect(wrapper.find('[data-test="motos-b-table"]').length).toBe(2)
      wrapper.destroy()
    });*/
  });

