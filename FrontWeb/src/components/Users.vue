<template>
  <div id="app">
    <h1>Users</h1>
    <div class="container">
      <b-table striped hover :items="users" :fields="fields" :filter-function="filterTable" sort-by="id">
        <template #cell(actions)="row">
          <button class="btn btn-warning btn-sm" @click="goToUser(row.item.id, $event.target)"> user </button>
          <button class="btn btn-danger btn-sm" @click="deleteUser(row.item.id)"> X </button>
        </template>
      </b-table>
      <button class="btn btn-success btn-md" @click="addUser($event.target)"> Add User </button>
       <!-- Modal Add Moto -->
      <b-modal :id="addUserModal.id" :title="addUserModal.title" centered  @ok="handleOk" @hidden="resetAddUserModal">
        <form ref="form">
          <b-form-group
            label="National Id Document"
            label-for="national-id-document-input"
            invalid-feedback="National id document is required"
          >
            <b-form-input
              id="national-id-document-input"
              v-model="addUserModal.user.national_id_document"
              required
            ></b-form-input>
          </b-form-group>
          <b-form-group
            label="Country"
            label-for="country-input"
            invalid-feedback="Country is required"
          >
            <b-form-input
              id="country-input"
              v-model="addUserModal.user.country"
              required
            ></b-form-input>
          </b-form-group>
          <b-form-group
            label="Name"
            label-for="name-input"
            invalid-feedback="Name is required"
          >
            <b-form-input
              id="name-input"
              v-model="addUserModal.user.name"
              required
            ></b-form-input>
          </b-form-group>
          <b-form-group
            label="Surname"
            label-for="surname-input"
            invalid-feedback="Surname is required"
          >
            <b-form-input
              id="surname-input"
              v-model="addUserModal.user.surname"
              required
            ></b-form-input>
          </b-form-group>
          <b-form-group
            label="Password"
            label-for="password-input"
            invalid-feedback="Password is required"
          >
            <b-form-input
              id="passwword-input"
              v-model="addUserModal.user.password"
              required
            ></b-form-input>
          </b-form-group>
          <b-form-group
            label="Mail"
            label-for="mail-input"
            invalid-feedback="Mail is required"
          >
            <b-form-input
              id="mail-input"
              v-model="addUserModal.user.mail"
              required
            ></b-form-input>
          </b-form-group>
          <b-form-group
            label="Role"
            label-for="role-input"
            invalid-feedback="Role is required"
          >
            <b-form-input
              id="role-input"
              v-model="addUserModal.user.role"
              required
            ></b-form-input>
          </b-form-group>
        </form>
      </b-modal>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import firebase from 'firebase/app'
import 'firebase/auth'
export default {
  name: 'User',
  data () {
    return {
      users: [],
      fields: ['id', 'id_bank_data', 'national_id_document', 'country', 'name', 'surname', 'mail', 'role', 'actions'],
      isPostNotPut: true,
      addUserModal: {
        id: 'add-user-modal',
        title: '',
        user: {
          id: '',
          id_bank_data: 0,
          national_id_document: '',
          country: '',
          name: '',
          surname: '',
          password: '',
          mail: '',
          google_token: '',
          role: 0
        }
      },
      aux: 'aux'
    }
  },
  methods: {
    getUsers () {
      const path = this.$heroku + '/users'
      axios.get(path, {
        auth: { username: this.token }
      })
        .then((res) => {
          this.users = res.data.users
        })
        .catch((error) => {
          console.error(error)
        })
    },
    deleteUser (userId) {
      const path = this.$heroku + `/user/${userId}`
      axios.delete(path, {
        auth: { username: this.token }
      })
        .then((res) => {
          this.getUsers()
        })
        .catch((error) => {
          console.error(error)
        })
    },
    addUser (button) {
      this.isPostNotPut = true
      this.openUserModal('Add a new User', button)
    },
    modifyUser (user, button) {
      this.isPostNotPut = false
      this.addUserModal.user = user
      this.openMotoModal(`Modify User with id: ${user.id}`, button)
    },
    openUserModal (title, button) {
      this.addUserModal.title = title
      this.$root.$emit('bv::show::modal', this.addUserModal.id, button)
    },
    handleOk (bvModalEvt) {
      bvModalEvt.preventDefault()
      this.submit()
    },
    handleSubmit () {
      this.postUser()
      this.$nextTick(() => {
      })
    },
    postUser () {
      const path = this.$heroku + `/user`
      const user = {
        'mail': this.addUserModal.user.mail,
        'google_token': this.addUserModal.user.google_token,
        'role': this.addUserModal.user.role
      }
      axios.post(path, user, {
        auth: { username: this.token }
      })
        .then((res) => {
          this.addUserModal.user.id = res.data.user.id
          this.getUsers()
          this.putUser()
        })
        .catch((error) => {
          console.error(error)
          this.getUsers()
        })
    },
    putUser () {
      const path = this.$heroku + `/user/${this.addUserModal.user.id}`
      const user = {
        'national_id_document': this.addUserModal.user.national_id_document,
        'country': this.addUserModal.user.country,
        'name': this.addUserModal.user.name,
        'surname': this.addUserModal.user.surname
      }
      this.aux = this.addUserModal.user.national_id_document
      axios.put(path, user, {
        auth: { username: this.token }
      })
        .then((res) => {
          console.log(res)
          this.getUsers()
          this.resetAddUserModal()
          this.$bvModal.hide(this.addUserModal.id)
        })
        .catch((error) => {
          console.error(error)
          this.getUsers()
        })
    },
    resetAddUserModal () {
      this.addUserModal = {
        id: 'add-user-modal',
        title: '',
        user: {
          id: '',
          id_bank_data: 0,
          national_id_document: '',
          country: '',
          name: '',
          surname: '',
          password: '',
          mail: '',
          google_token: '',
          role: 0
        }
      }
    },
    submit () {
      try {
        firebase.auth().createUserWithEmailAndPassword(this.addUserModal.user.mail, this.addUserModal.user.password)
          .then((user) => {
            const u = firebase.auth().currentUser
            if (u != null) {
              console.log(u)
              this.addUserModal.user.google_token = u.uid
              this.handleSubmit()
            } else {
              console.log(u)
            }
          })
      } catch (err) {
        console.log(err)
      }
    },
    goToUser (id, button) {
      this.$router.replace({path: `/user/${id}`})
    },
    filterTable (row, filter) {
      if (this.filterOn.includes('moto_id')) {
        if (row.moto_id.toString() === filter) {
          return true
        } else {
          return false
        }
      } else if (this.filterOn.includes('user_id')) {
        if (row.user_id.toString() === filter) {
          return true
        } else {
          return false
        }
      }
    }
  },
  created () {
    this.getUsers()
    this.token = this.$store.state.token
  }
}
</script>
