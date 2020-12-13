<template>
  <div id="app">
    <h1 data-test="rentals-title">Rentals</h1>
    <div class="container">
      <b-table data-test="rentals-b-table" striped hover :items="rentals" :fields="fields" :filter="filter" :filter-included-fields="filterOn" :filter-function="filterTable" sort-by="id">
        <template #cell(actions)="row">
            <b-button variant="secondary" size="sm" @click="row.toggleDetails"> details </b-button>
        </template>
        <template #row-details="row">
          <b-card>
            <!--div v-for="(value, key) in row.item" :key="key">{{ key }}: {{ value }}</div-->
            <b-container>
                <b-row>
                    <b-col cols="3">
                        <h6>User ID : {{row.item.user_id}}</h6>
                        <h6>Moto ID : {{row.item.moto_id}}</h6>
                        <h6>Active : {{row.item.active}}</h6>
                    </b-col>
                    <b-col >
                        <h6>Book Hour :</h6>
                        <h6>{{getParsedTime(row.item.book_hour)}}</h6>
                    </b-col>
                    <b-col>
                        <h6>Finish Book Hour :</h6>
                        <h6>{{getParsedTime(row.item.finish_book_hour)}}</h6>
                    </b-col>
                    <b-col>
                        <h6>Finish Rental Hour :</h6>
                        <h6>{{getParsedTime(row.item.finish_rental_hour)}}</h6>
                    </b-col>
                </b-row>
                <b-row>
                    <b-col><b-button block variant="info" size="sm" @click="info(row.item, row.item.id, $event.target)"> json </b-button></b-col>
                    <b-col><b-button block variant="secondary" size="sm" @click="putRentalState(row.item)"> CHANGE STATE </b-button></b-col>
                    <b-col><b-button block variant="danger" size="sm" @click="deleteRental(row.item.id)"> DELETE RENTAL </b-button></b-col>
                </b-row>
            </b-container>
          </b-card>
        </template>
      </b-table>
      <!-- Modal Json -->
      <b-modal :id="infoModal.id" :title="infoModal.title" ok-only centered @hide="resetInfoModal">
        <pre>{{ infoModal.content }}</pre>
      </b-modal>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import moment from 'moment'
export default {
  name: 'Rentals',
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
      rentals: [],
      fields: [
        {key: 'id', sortable: true},
        {key: 'user_id', sortable: true},
        {key: 'moto_id', sortable: true},
        {key: 'active', sortable: true},
        {key: 'actions', sortable: false}
      ], // book_hour finish_book_hour finish_rental_hour
      infoModal: {
        id: 'info-modal',
        title: '',
        content: ''
      },
      filterId: null,
      filterOn: [],
      isPostNotPut: true,
      token: ''
    }
  },
  methods: {
    getRentals () {
      const path = this.$heroku + '/rentals'
      axios.get(path, {
        auth: { username: this.token }
      })
        .then((res) => {
          this.rentals = res.data.rentals
        })
        .catch((error) => {
          console.error(error)
        })
    },
    deleteRental (rentalId) {
      const path = this.$heroku + `/rental/${rentalId}`
      axios.delete(path, {
        auth: { username: this.token }
      })
        .then((res) => {
          console.log(res)
          this.getRentals()
        })
        .catch((error) => {
          console.error(error)
        })
    },
    modifyRental (rental, button) {
      this.isPostNotPut = false
      this.addRentalModal.rental = rental
      this.openRentalModal(`Modify Rental with id: ${rental.id}`, button)
    },
    putRentalState (rental) {
      if (rental.finish_rental_hour == null) {
        const motoPath = this.$heroku + `/moto/${rental.moto_id}`
        axios.get(motoPath, {
          auth: { username: this.token }
        })
          .then((motoRes) => {
            const path = this.$heroku + `/rental/${rental.id}`
            var end = true
            if (rental.finish_book_hour != null) { end = true }
            const param = {
              'end_rental': end,
              'latitude': motoRes.data.moto.latitude,
              'longitude': motoRes.data.moto.longitude
            }
            axios.put(path, param, {
              auth: { username: this.token }
            })
              .then((res) => {
                console.log(res)
                this.getRentals()
              })
              .catch((error) => {
                console.error(error)
              })
          })
          .catch((error) => {
            console.error(error)
          })
      }
    },
    getParsedTime (time) {
      if (time != null) { return moment(time).format('LLL') } else { return '-' }
    },
    info (item, id, button) {
      this.infoModal.title = `Rental Detail of rental ${id}`
      this.infoModal.content = JSON.stringify(item, null, 2)
      this.$root.$emit('bv::show::modal', this.infoModal.id, button)
    },
    resetInfoModal () {
      this.infoModal.title = ''
      this.infoModal.content = ''
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
    this.getRentals()
    const id = this.$route.params.id
    if (id != null) { this.filterId = id.toString() }
    this.token = this.$store.state.token
  }
}
</script>
