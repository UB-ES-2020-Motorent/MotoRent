<template>
  <div id="app">
    <div class="container" v-if="$store.getters.isLoggedIn">
      <div v-for="(year) in Object.keys(statistics)" v-bind:key="year">
        <h1>{{ year }}</h1>
        <div v-for="(month) in returnMonths(year)" v-bind:key="month">
          <h4>&nbsp;</h4>
          <h4 style="text-align:left">{{ getMonthName(month) }}</h4>
          <b-table data-test="statistics-b-table" striped hover :items="[getMonthData(year, month)]" :fields="fields">
          </b-table>
        </div>
        <h1>&nbsp;</h1>
        <h1>&nbsp;</h1>
      </div>
    </div>
    <div class="container" v-if="!$store.getters.isLoggedIn">
      <h1>Statistics</h1>
      <img src="@/assets/stop.jpg" height="200" margin>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import router from '../router'
export default {
  name: 'Statistics',
  data () {
    return {
      statistics: [],
      fields: [
        {key: 'total_rentals'},
        {key: 'rental_earnings'},
        {key: 'rentals_mean_duration_(minutes)'}
      ],
      monthNames: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December']
    }
  },
  methods: {
    navigate () {
      router.go(-1)
    },
    getStatistics () {
      const path = this.$heroku + `/statistics`
      axios.get(path, {
        auth: { username: this.token }
      })
        .then((res) => {
          this.statistics = res.data.statistics
        })
        .catch((error) => {
          console.error(error)
        })
    },
    returnMonths (year) {
      return Object.keys(this.statistics[year])
    },
    getMonthName (month) {
      return this.monthNames[month - 1]
    },
    getMonthData (year, month) {
      return {
        'total_rentals': this.statistics[year][month]['num_rentals'],
        'rental_earnings': this.statistics[year][month]['total_money'] + ' \u20AC',
        'rentals_mean_duration_(minutes)': (this.statistics[year][month]['rental_duration_total'] / this.statistics[year][month]['num_rentals']).toFixed(2)
      }
    }
  },
  created () {
    this.token = this.$store.state.token
    this.getStatistics()
  }
}
</script>
