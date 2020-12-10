<template>
  <div id="app">
    <h1 data-test="payments-title">Payments</h1>
    <div class="container">
      <b-table data-test="payments-b-table" responsive striped hover :items="payments">
        <template #cell(actions)="row">
          <button class="btn btn-info btn-sm" @click="info(row.item, row.item.id_payment, $event.target)"> json </button>
          <button class="btn btn-secondary btn-sm" @click="modifyPayment(row.item, $event.target)"> modify </button>
          <button class="btn btn-danger btn-sm" @click="deletePayment(row.item.id_payment)"> X </button>
        </template>
      </b-table>
      <button class="btn btn-success btn-md" @click="addPayment($event.target)"> Add Payment </button>
      <!-- Modal Json -->
      <b-modal :id="infoModal.id" :title="infoModal.title" ok-only centered @hide="resetInfoModal">
        <pre>{{ infoModal.content }}</pre>
      </b-modal>
      <!-- Modal Add Payment -->
      <b-modal :id="addPaymentModal.id" :title="addPaymentModal.title" centered  @ok="handleOk" @hidden="resetAddPaymentModal">
        <form ref="form" @submit.stop.prevent="handleSubmit">
          <b-form-group
            :state="userIdValidity"
            label="User ID"
            label-for="user-id-input"
            invalid-feedback="User ID is required"
          >
            <b-form-input
              id="user-id-input"
              v-model="addPaymentModal.payment.user_id"
              :state="userIdValidity"
              required
            ></b-form-input>
          </b-form-group>
          <b-form-group
            :state="cardNumberValidity"
            label="Card Number"
            label-for="card-number-input"
            invalid-feedback="Card Number is required"
          >
            <b-form-input
              id="card-number-input"
              v-model="addPaymentModal.payment.card_number"
              :state="cardNumberValidity"
              required
            ></b-form-input>
          </b-form-group>
          <b-form-group
            :state="cardOwnerValidity"
            label="Card Owner"
            label-for="card-owner-input"
            invalid-feedback="Card Owner is required"
          >
            <b-form-input
              id="card-owner-input"
              v-model="addPaymentModal.payment.card_owner"
              :state="cardOwnerValidity"
              required
            ></b-form-input>
          </b-form-group>
          <b-form-group
            :state="cardCvvValidity"
            label="Card cvv"
            label-for="card-cvv-input"
            invalid-feedback="Card cvv is required"
          >
            <b-form-input
              id="card-cvv-input"
              v-model="addPaymentModal.payment.card_cvv"
              :state="cardCvvValidity"
              required
            ></b-form-input>
          </b-form-group>
          <b-form-group
            :state="cardExpirationValidity"
            label="Card Expiration"
            label-for="card-expiration-input"
            invalid-feedback="Card Expiration is required"
          >
            <b-form-input
              id="card-expiration-input"
              v-model="addPaymentModal.payment.card_expiration"
              :state="cardExpirationValidity"
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
export default {
  name: 'Payments',
  computed: {
    userIdValidity () {
      if (this.addPaymentModal.user_id_state === false) { return null } else {
        if (this.addPaymentModal.payment.user_id === '') { return false } else { return null }
      }
    },
    cardNumberValidity () {
      if (this.addPaymentModal.card_number_state === false) { return null } else {
        if (this.addPaymentModal.payment.card_number === '') { return false } else { return null }
      }
    },
    cardOwnerValidity () {
      if (this.addPaymentModal.card_owner_state === false) { return null } else {
        if (this.addPaymentModal.payment.card_owner === '') { return false } else { return null }
      }
    },
    cardCvvValidity () {
      if (this.addPaymentModal.card_cvv_state === false) { return null } else {
        if (this.addPaymentModal.payment.card_cvv === '') { return false } else { return null }
      }
    },
    cardExpirationValidity () {
      if (this.addPaymentModal.card_expiration_state === false) { return null } else {
        if (this.addPaymentModal.payment.card_expiration === '') { return false } else { return null }
      }
    }
  },
  data () {
    return {
      payments: [],
      fields: [
        {key: 'id_payment', sortable: true},
        {key: 'user_id', sortable: true},
        {key: 'card_number', sortable: false},
        {key: 'card_owner', sortable: true},
        {key: 'card_cvv', sortable: false},
        {key: 'card_expiration', sortable: false},
        {key: 'actions', sortable: false}
      ],
      infoModal: {
        id: 'info-modal',
        title: '',
        content: ''
      },
      addPaymentModal: {
        id: 'add-payment-modal',
        title: '',
        payment: {
          id_payment: '',
          user_id: '',
          card_number: '',
          card_owner: '',
          card_cvv: '',
          card_expiration: ''
        },
        user_id_state: false,
        card_number_state: false,
        card_owner_state: false,
        card_cvv_state: false,
        card_expiration_state: false
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
    addPayment (button) {
      this.isPostNotPut = true
      this.addPaymentModal.title = 'Add a new Payment'
      this.openPaymentModal(button)
    },
    modifyPayment (payment, button) {
      this.isPostNotPut = false
      this.addPaymentModal.payment = payment
      this.addPaymentModal.title = `Modify Payment with id: ${payment.id_payment}`
      this.openPaymentModal(button)
    },
    openPaymentModal (button) {
      this.$root.$emit('bv::show::modal', this.addPaymentModal.id, button)
    },
    checkFormValidity () {
      const valid = this.$refs.form.checkValidity()
      this.addPaymentModal.user_id_state = true
      this.addPaymentModal.card_number_state = true
      this.addPaymentModal.card_owner_state = true
      this.addPaymentModal.card_cvv_state = true
      this.addPaymentModal.card_expiration_state = true
      return valid
    },
    handleOk (bvModalEvt) {
      bvModalEvt.preventDefault()
      this.handleSubmit()
    },
    handleSubmit () {
      if (!this.checkFormValidity()) {
        return
      }
      if (this.isPostNotPut) {
        this.postPayment()
      } else {
        this.putPayment()
      }
      this.$nextTick(() => {
        this.resetAddPaymentModal()
        this.$bvModal.hide(this.addPaymentModal.id)
      })
    },
    postPayment () {
      const path = this.$heroku + `/payment`
      const payment = {
        'user_id': this.addPaymentModal.payment.user_id,
        'card_number': this.addPaymentModal.payment.card_number,
        'card_owner': this.addPaymentModal.payment.card_owner,
        'card_cvv': this.addPaymentModal.payment.card_cvv,
        'card_expiration': this.addPaymentModal.payment.card_expiration
      }
      axios.post(path, payment)
        .then((res) => {
          console.log(res)
          this.getPayments()
        })
        .catch((error) => {
          console.error(error)
          this.getPayments()
        })
    },
    putPayment () {
      const path = this.$heroku + `/payment/${this.addPaymentModal.payment.id_payment}`
      const payment = {
        'user_id': this.addPaymentModal.payment.user_id,
        'card_number': this.addPaymentModal.payment.card_number,
        'card_owner': this.addPaymentModal.payment.card_owner,
        'card_cvv': this.addPaymentModal.payment.card_cvv,
        'card_expiration': this.addPaymentModal.payment.card_expiration
      }
      axios.put(path, payment)
        .then((res) => {
          console.log(res)
          this.getPayments()
        })
        .catch((error) => {
          console.error(error)
          this.getPayments()
        })
    },
    resetAddPaymentModal () {
      this.addPaymentModal = {
        id: 'add-payment-modal',
        title: '',
        payment: {
          id_payment: '',
          user_id: '',
          card_number: '',
          card_owner: '',
          card_cvv: '',
          card_expiration: ''
        },
        user_id_state: false,
        card_number_state: false,
        card_owner_state: false,
        card_cvv_state: false,
        card_expiration_state: false
      }
    },
    info (item, id, button) {
      this.infoModal.title = `Payment Detail of payment ${id}`
      this.infoModal.content = JSON.stringify(item, null, 2)
      this.$root.$emit('bv::show::modal', this.infoModal.id, button)
    },
    resetInfoModal () {
      this.infoModal.title = ''
      this.infoModal.content = ''
    }
  },
  created () {
    this.getPayments()
  }
}
</script>
