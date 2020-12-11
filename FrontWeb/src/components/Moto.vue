<template>
  <div id="app">
    <h1>Moto {{id}}</h1>
    <div class="container">
      <b-table data-test="motos-b-table" responsive striped hover :items="[moto]">
        <template #cell(available)="row">
          <b-button :variant="availableButton(row.value)" size="sm" @click="putMotoAvailable(row.item.id, row.value)"> {{row.value}} </b-button>
        </template>
      </b-table>
    </div>
    <b-container>
      <incidents :moto_id="moto.id" />
      <rentals :moto_id="moto.id" />
    </b-container>
  </div>
</template>

<script>
import axios from 'axios'
import router from '../router'
import Rentals from '@/components/Rentals.vue'
import Incidents from '@/components/Incidents.vue'
export default {
  name: 'Moto',
  components: {
    Rentals,
    Incidents
  },
  data () {
    return {
      id: 0,
      moto: {}
    }
  },
  created () {
    this.id = this.$route.params.id
    this.getMoto()
  },
  methods: {
    navigate () {
      router.go(-1)
    },
    getMoto () {
      const path = this.$heroku + `/moto/${this.id}`
      axios.get(path)
        .then((res) => {
          console.log(res)
          this.moto = res.data.moto
        })
        .catch((error) => {
          console.error(error)
        })
    },
    putMotoAvailable (id, available) {
      const path = this.$heroku + `/moto/${id}`
      const param = {'available': !available}
      axios.put(path, param)
        .then((res) => {
          console.log(res)
          this.getMoto()
        })
        .catch((error) => {
          console.error(error)
        })
    },
    availableButton (available) {
      if (available) { return 'outline-success' } else { return 'outline-danger' }
    }
  }
}
</script>
