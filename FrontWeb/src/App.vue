<template>
  <div id="app">
    <img src="./assets/moto_toast.png" height="100" margin>
    <div class="container">
      <div class="btn-group btn-group-toggle mt-20" data-toggle="buttons" id = "buttonGroup" data-test="app-button-group">
        <label class="btn btn-secondary" >
          <input type="radio" name="options" id="motosID" autocomplete="off" checked @click="goToMotos"> Motos
        </label>
        <label class="btn btn-secondary">
          <input type="radio" name="options" id="usersID" autocomplete="off" checked @click="goToUsers"> Users
        </label>
        <label class="btn btn-secondary">
          <input type="radio" name="options" id="rentalsID" autocomplete="off"> Rentals
        </label>
      </div>
    </div>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',
  methods: {
    goToMotos () {
      this.$router.replace({path: '/motos'})
    },
    goToUsers () {
      this.$router.replace({path: '/users'})
    }
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
  margin-bottom: 20px;
}
</style>
