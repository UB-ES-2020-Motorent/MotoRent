<template>
  <div id="app">
    <h1>Usuaris</h1>
    <div class="container">
      <b-table striped hover :items="users" :fields="fields"></b-table>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'User',
  data () {
    return {
      users: [],
      // fields: ['id', 'id_bank_data', 'national_id_document', 'country', 'name', 'surname', 'mail', 'google_token', 'role']
      fields: ['id', 'id_bank_data', 'national_id_document', 'country', 'name', 'surname', 'mail', 'role']
    }
  },
  methods: {
    getUsers () {
      const path = this.$heroku + `/users?admin_code=admin_secret_code`
      axios.get(path)
        .then((res) => {
          this.users = res.data.users
        })
        .catch((error) => {
          console.error(error)
        })
    },
    deleteUser (user) {

    }
  },
  created () {
    this.getUsers()
  }
}
</script>
