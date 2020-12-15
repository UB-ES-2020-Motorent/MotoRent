<template>
  <div id="app">
    <h1 data-test="payments-title">Payments</h1>
    <div class="container" v-if="$store.getters.isLoggedIn">
      <b-table data-test="payments-b-table" responsive striped hover :items="payments" :fields="fields" :filter="filterId" :filter-included-fields="filterOn" :filter-function="filterTable" sort-by="id_payment">
        <template #cell(actions)="row">
          <button class="btn btn-info btn-sm" @click="info(row.item, row.item.id_payment, $event.target)"> json </button>
          <button class="btn btn-danger btn-sm" @click="deletePayment(row.item.id_payment)"> X </button>
        </template>
        <template #cell(payment_import)="row">
          {{row.value}} €
        </template>
        <template #cell(payment_date)="row">
          {{ getParsedTime(row.value) }}
        </template>
      </b-table>
      <!-- Modal Json -->
      <b-modal :id="infoModal.id" :title="infoModal.title" ok-only centered @hide="resetInfoModal">
        <pre>{{ infoModal.content }}</pre>
      </b-modal>
    </div>
    <div class="container" v-if="!$store.getters.isLoggedIn">
      <img src="@/assets/stop.jpg" height="200" margin>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import moment from 'moment'
export default {
  name: 'Payments',
  props: [ 'moto_id', 'user_id' ],
  watch: {
    moto_id () {
      this.filterOn.push('moto_id')
    },
    user_id () {
      this.filterOn.push('user_id')
    }
  },
  data () {
    return {
      payments: [],
      fields: [
        {key: 'user_id', sortable: true},
        {key: 'id_payment', sortable: true},
        {key: 'id_rental', sortable: true},
        {key: 'id_bank_data', sortable: false},
        {key: 'payment_import', sortable: true},
        {key: 'payment_date', sortable: false},
        {key: 'actions', sortable: false}
      ],
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
    getPayments () {
      const path = this.$heroku + '/payments'
      axios.get(path, {
        auth: { username: this.token }
      })
        .then((res) => {
          this.payments = res.data.payments
        })
        .catch((error) => {
          console.error(error)
        })
    },
    deletePayment (paymentId) {
      const path = this.$heroku + `/payment/${paymentId}`
      axios.delete(path, {
        auth: { username: this.token }
      })
        .then((res) => {
          console.log(res)
          this.getPayments()
        })
        .catch((error) => {
          console.error(error)
        })
    },
    info (item, id, button) {
      this.infoModal.title = `Payment Detail of payment ${id}`
      this.infoModal.content = JSON.stringify(item, null, 2)
      this.$root.$emit('bv::show::modal', this.infoModal.id, button)
    },
    resetInfoModal () {
      this.infoModal.title = ''
      this.infoModal.content = ''
    },
    getParsedTime (time) {
      if (time !== '') { return moment(time).format('LLL') } else { return '-' }
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
    const id = this.$route.params.id
    if (id != null) { this.filterId = id.toString() }
    this.token = this.$store.state.token
    this.getPayments()
  }
}
</script>
