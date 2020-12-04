<template>
  <div id="app">
    <h1> {{ message }} </h1>
    <button class="btn btn-dark btn-lg" @click="goToMotos"> MOTOS </button>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data () {
    return {
    }
  },
  methods: {
    goToMotos () {
      this.$router.replace({path: '/motos'})
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
