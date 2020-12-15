<template>
  <div id="app">
    <h1>User {{id}}</h1>
    <div class="container">
      <b-table data-test="users-b-table" responsive striped hover :items="[user]" :fields="fields">
        <template #cell(available)="row">
          <b-button :variant="availableButton(row.value)" size="sm" @click="putUserAvailable(row.item.id, row.value)"> {{row.value}} </b-button>
        </template>
      </b-table>
    </div>
    <b-container>
      <bankdatas :user_id="user.id" detail="true"/>
      <!--payments :user_id="user.id" /-->
      <rentals :user_id="user.id" />
      <incidents :user_id="user.id" />
    </b-container>
  </div>
</template>

<script>
import axios from 'axios'
import router from '../router'
import Rentals from '@/components/Rentals.vue'
import Incidents from '@/components/Incidents.vue'
import Payments from './Payments.vue'
import Bankdatas from './Bankdatas.vue'
export default {
  name: 'User',
  components: {
    Rentals,
    Incidents,
    Payments,
    Bankdatas
  },
  data () {
    return {
      id: 0,
      user: {},
      fields: ['id_bank_data', 'national_id_document', 'country', 'name', 'surname', 'mail', 'google_token']
    }
  },
  methods: {
    navigate () {
      router.go(-1)
    },
    getUser () {
      const path = this.$heroku + `/user?id=${this.id}`
      axios.get(path, {
        auth: { username: this.token }
      })
        .then((res) => {
          console.log(res)
          this.user = res.data.user
        })
        .catch((error) => {
          console.error(error)
        })
    },
    putUserAvailable (id, available) {
      const path = this.$heroku + `/user/${id}`
      const param = {'available': !available}
      axios.put(path, param, {
        auth: { username: this.token }
      })
        .then((res) => {
          console.log(res)
          this.getUser()
        })
        .catch((error) => {
          console.error(error)
        })
    },
    availableButton (available) {
      if (available) { return 'outline-success' } else { return 'outline-danger' }
    }
  },
  created () {
    this.id = this.$route.params.id
    this.getUser()
    this.token = this.$store.state.token
  }
}
</script>
