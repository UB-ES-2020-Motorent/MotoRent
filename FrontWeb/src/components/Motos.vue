<template>
  <div id="app">
    <h1>Motos</h1>
    <div class="container">
      <b-table responsive striped hover :items="motos" :fields="fields"></b-table>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'Motos',
  data () {
    return {
      motos: [],
      fields: [
        {key: 'id', sortable: true},
        {key: 'license_number', sortable: false},
        {key: 'battery', sortable: true},
        {key: 'available', sortable: true},
        {key: 'latitude', sortable: false},
        {key: 'longitude', sortable: false}
      ]
    }
  },
  methods: {
    getMotos () {
      const path = this.$heroku + '/motos'
      axios.get(path)
        .then((res) => {
          this.motos = res.data.motos
        })
        .catch((error) => {
          console.error(error)
        })
    },
    deleteMoto (moto) {

    }
  },
  created () {
    this.getMotos()
  }
}
</script>
