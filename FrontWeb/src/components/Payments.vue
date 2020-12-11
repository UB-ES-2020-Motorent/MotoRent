<template>
  <div id="app">
    <h1 data-test="payments-title">Payments</h1>
    <div class="container">
      <b-table data-test="payments-b-table" responsive striped hover :items="payments" :fields="fields">
        <template #cell(actions)="row">
          <button class="btn btn-info btn-sm" @click="info(row.item, row.item.id_payment, $event.target)"> json </button>
          <button class="btn btn-danger btn-sm" @click="deletePayment(row.item.id_payment)"> X </button>
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
  </div>
</template>

<script>
import axios from 'axios'
import moment from 'moment'
export default {
  name: 'Payments',
  data () {
    return {
      payments: [],
      fields: [
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
      isPostNotPut: true
    }
  },
  methods: {
    getPayments () {
      const path = this.$heroku + '/payments'
      axios.get(path)
        .then((res) => {
          this.payments = res.data.payments
        })
        .catch((error) => {
          console.error(error)
        })
    },
    deletePayment (paymentId) {
      const path = this.$heroku + `/payment/${paymentId}`
      axios.delete(path)
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
    }
  },
  created () {
    this.getPayments()
  }
}
</script>
