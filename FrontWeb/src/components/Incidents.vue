<template>
  <div id="app">
    <h1>Incidents</h1>
    <div class="container" v-if="$store.getters.isLoggedIn">
      <b-table hover :items="incidents" :fields="fields" :filter="filter" :filter-included-fields="filterOn" :filter-function="filterTable" sort-by="id">
        <template #cell(comment)="row">
          <b-button size="sm" @click="row.toggleDetails" class="mr-1">Comment user</b-button>
          <button class="btn btn-danger btn-sm" @click="deleteIncident(row.item.id)"> X </button>
        </template>
        <template #row-details="row">
          <b-card>
            {{row.item.comment}}
          </b-card>
        </template>
      </b-table>
    </div>
    <div class="container" v-if="!$store.getters.isLoggedIn">
      <img src="@/assets/stop.jpg" height="200" margin>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'Incident',
  props: [ 'moto_id', 'user_id' ],
  watch: {
    moto_id () {
      this.filterOn.push('moto_id')
    },
    user_id () {
      this.filterOn.push('user_id')
    }
  },
  computed: {
    filter () { if (this.filterId != null) { return this.filterId } else { return null } }
  },
  data () {
    return {
      incidents: [],
      fields: ['id', 'user_id', 'moto_id', 'comment'],
      infoModal: {
        id: 'info-modal',
        title: '',
        content: ''
      },
      filterId: null,
      filterOn: []
    }
  },
  methods: {
    getIncidents () {
      const path = this.$heroku + `/incidents`
      axios.get(path, {
        auth: { username: this.token }
      })
        .then((res) => {
          this.incidents = res.data.incidents
        })
        .catch((error) => {
          console.error(error)
        })
    },
    deleteIncident (id) {
      const path = this.$heroku + `/incident/${id}`
      axios.delete(path, {
        auth: { username: this.token }
      })
        .then((res) => {
          console.log(res)
          this.getIncidents()
        })
        .catch((error) => {
          console.error(error)
        })
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
    this.token = this.$store.state.token
    this.getIncidents()
    const id = this.$route.params.id
    if (id != null) { this.filterId = id.toString() }
  }
}
</script>
