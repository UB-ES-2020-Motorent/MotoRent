<template>
  <div id="app">
    <h1>Moto {{id}}</h1>
    <div class="container" v-if="$store.getters.isLoggedIn">
      <b-table data-test="motos-b-table" responsive striped hover :items="[moto]">
        <template #cell(available)="row">
          <b-button :variant="availableButton(row.value)" size="sm" @click="putMotoAvailable(row.item.id, row.value)"> {{row.value}} </b-button>
        </template>
      </b-table>
    </div>
    <b-container>
      <incidents :moto_id="moto.id" />
      <rentals :moto_id="moto.id" />
      <h1>Last Users</h1>
      <b-table striped hover :items="justUsers" sort-by="rental_id"></b-table>
    </b-container>
    <div class="container" v-if="!$store.getters.isLoggedIn">
      <img src="@/assets/stop.jpg" height="200" margin>
    </div>
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
      moto: {},
      id_rentals: [],
      justUsers: [],
      token: '',
      userDetails: {
        rental_id: '',
        id: '',
        id_bank_data: '',
        national_id_document: '',
        country: '',
        name: '',
        surname: '',
        mail: '',
        role: ''
      }
    }
  },
  methods: {
    navigate () {
      router.go(-1)
    },
    getMoto () {
      const path = this.$heroku + `/moto/${this.id}`
      axios.get(path, {
        auth: { username: this.token }
      })
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
      axios.put(path, param, {
        auth: { username: this.token }
      })
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
    },
    lastUsersMoto (id, num) {
      const path = this.$heroku + `/lastrentals/${id}?num_rentals=${num}`
      axios.get(path, {
        auth: {username: this.token}
      })
        .then((res) => {
          this.users = res.data.last_rentals
          this.getJustUsers()
          console.log(this.users)
        })
        .catch((error) => {
          console.error(error)
        })
    },
    getJustUsers () {
      var i = 1
      while (this.users[i] != null) {
        this.userDetails.rental_id = this.users[i][0].id
        this.userDetails.id = this.users[i][1].id
        this.userDetails.id_bank_data = this.users[i][1].id_bank_data
        this.userDetails.national_id_document = this.users[i][1].national_id_document
        this.userDetails.country = this.users[i][1].country
        this.userDetails.name = this.users[i][1].name
        this.userDetails.surname = this.users[i][1].surname
        this.userDetails.mail = this.users[i][1].mail
        this.userDetails.role = this.users[i][1].role
        this.justUsers.push(this.userDetails)
        this.resetUserDetails()
        i = i + 1
      }
    },
    resetUserDetails () {
      this.userDetails = {
        rental_id: '',
        id: '',
        id_bank_data: '',
        national_id_document: '',
        country: '',
        name: '',
        surname: '',
        mail: '',
        role: ''
      }
    }
  },
  created () {
    this.id = this.$route.params.id
    this.getMoto()
    this.token = this.$store.state.token
    this.lastUsersMoto(this.id, 10)
  }
}
</script>
