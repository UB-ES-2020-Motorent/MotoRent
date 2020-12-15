<template>
  <div id="app">
    <h1> Welcome to MotoRent DataBase Control Center </h1>
    <div id="app">
      <div class="mx-auto" style="width: 300px">
        <form ref="form">
          <b-form-group
            label="Mail"
            label-for="mail-input"
            invalid-feedback="Mail is required"
          >
            <b-form-input
              id="mail-input"
              v-model="loginData.mail"
              required
            ></b-form-input>
          </b-form-group>
          <b-form-group
            label="Password"
            label-for="password-input"
            invalid-feedback="Password is required"
          >
            <b-form-input
              id="password-input"
              v-model="loginData.password"
              required
            ></b-form-input>
          </b-form-group>
        </form>
      </div>
    </div>
    <div>
      <button class="btn btn-success btn-md" @click="logIn()"> Sign In </button>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import firebase from 'firebase/app'
import 'firebase/auth'
export default {
  name: 'Home',
  data () {
    return {
      modalID: 'modaliD',
      loginData: {
        mail: '',
        password: '',
        token: ''
      },
      tokenDB: ''
    }
  },
  methods: {
    logIn () {
      this.loginFirebase()
    },
    loginFirebase () {
      try {
        firebase.auth().signInWithEmailAndPassword(this.loginData.mail, this.loginData.password)
          .then(() => {
            this.loginData.token = firebase.auth().currentUser.uid
            this.loginPost()
          })
      } catch (err) {
        console.log(err)
      }
    },
    loginPost () {
      const path = this.$heroku + '/login'
      const logIn = {
        'mail': this.loginData.mail,
        'google_token': this.loginData.token
      }
      axios.post(path, logIn)
        .then((res) => {
          console.log(res)
          this.tokenDB = res.data.token
          this.$store.commit('setToken', this.tokenDB)
        })
        .catch((error) => {
          console.error(error)
        })
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
