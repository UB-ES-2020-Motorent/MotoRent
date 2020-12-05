<template>
  <div id="app">
    <h1>Motos</h1>
    <div class="container">
      <b-table striped hover :items="motos"></b-table>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'Motos',
  data () {
    return {
      motos: []
    }
  },
  methods: {
    getMotos () {
      const path = `https://motorent-apitest.herokuapp.com/motos`
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
