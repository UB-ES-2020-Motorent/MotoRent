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
          <input type="radio" name="options" id="RentalsID" autocomplete="off" checked @click="goToRentals"> Rentals
        </label>
        <label class="btn btn-secondary">
          <input type="radio" name="options" id="IncidentsID" autocomplete="off" checked @click="goToIncidents"> Incidents
        </label>
        <label class="btn btn-secondary">
          <input type="radio" name="options" id="BancdatasID" autocomplete="off" checked @click="goToBankdatas"> BankDatas
        </label>
        <label class="btn btn-secondary">
          <input type="radio" name="options" id="PaymentsID" autocomplete="off" checked @click="goToPayments"> Payments
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
    },
    goToRentals () {
      this.$router.replace({path: '/rentals'})
    },
    goToIncidents () {
      this.$router.replace({path: '/incidents'})
    },
    goToBankdatas () {
      this.$router.replace({path: '/bankdatas'})
    },
    goToPayments () {
      this.$router.replace({path: '/payments'})
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
