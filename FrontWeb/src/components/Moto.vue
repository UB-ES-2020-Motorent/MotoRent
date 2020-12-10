<template>
  <div id="app">
    <div class="container">
      {{moto}}
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import router from '../router'
export default {
  name: 'Moto',
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
    }
  }
}
</script>
