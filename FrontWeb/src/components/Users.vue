<template>
  <div id="app">
    <h1>Usuaris</h1>
    <div class="container">
      <b-table striped hover :items="users"></b-table>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'User',
  data () {
    return {
      users: []
    }
  },
  methods: {
    getUsers () {
      const path = `https://motorent-apitest.herokuapp.com/users?admin_code=admin_secret_code`
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
