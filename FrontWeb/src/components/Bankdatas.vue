<template>
  <div id="app">
    <h1 data-test="bankdatas-title">BankDatas</h1>
    <div class="container">
      <b-table data-test="bankdatas-b-table" responsive striped hover :items="bankdatas" :fields="fields" :filter="filterId" :filter-included-fields="filterOn" :filter-function="filterTable" sort-by="id_bank_data">
        <template #cell(actions)="row">
          <button class="btn btn-info btn-sm" @click="info(row.item, row.item.id_bank_data, $event.target)"> json </button>
          <button class="btn btn-secondary btn-sm" @click="modifyBankData(row.item, $event.target)"> modify </button>
          <button class="btn btn-danger btn-sm" @click="deleteBankData(row.item.id_bank_data, row.item.user_id)"> X </button>
        </template>
      </b-table>
      <button class="btn btn-success btn-md" v-if="notDetail" @click="addBankData($event.target)"> Add BankData </button>
      <!-- Modal Json -->
      <b-modal :id="infoModal.id" :title="infoModal.title" ok-only centered @hide="resetInfoModal">
        <pre>{{ infoModal.content }}</pre>
      </b-modal>
      <!-- Modal Add BankData -->
      <b-modal :id="addBankDataModal.id" :title="addBankDataModal.title" centered  @ok="handleOk" @hidden="resetAddBankDataModal">
        <form ref="form" @submit.stop.prevent="handleSubmit">
          <b-form-group
            :state="userIdValidity"
            label="User ID"
            label-for="user-id-input"
            invalid-feedback="User ID is required"
          >
            <b-form-input
              id="user-id-input"
              v-model="addBankDataModal.bankdata.user_id"
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
              v-model="addBankDataModal.bankdata.card_number"
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
              v-model="addBankDataModal.bankdata.card_owner"
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
              v-model="addBankDataModal.bankdata.card_cvv"
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
              v-model="addBankDataModal.bankdata.card_expiration"
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
  name: 'BankDatas',
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
    userIdValidity () {
      if (this.addBankDataModal.user_id_state === false) { return null } else {
        if (this.addBankDataModal.bankdata.user_id === '') { return false } else { return null }
      }
    },
    cardNumberValidity () {
      if (this.addBankDataModal.card_number_state === false) { return null } else {
        if (this.addBankDataModal.bankdata.card_number === '') { return false } else { return null }
      }
    },
    cardOwnerValidity () {
      if (this.addBankDataModal.card_owner_state === false) { return null } else {
        if (this.addBankDataModal.bankdata.card_owner === '') { return false } else { return null }
      }
    },
    cardCvvValidity () {
      if (this.addBankDataModal.card_cvv_state === false) { return null } else {
        if (this.addBankDataModal.bankdata.card_cvv === '') { return false } else { return null }
      }
    },
    cardExpirationValidity () {
      if (this.addBankDataModal.card_expiration_state === false) { return null } else {
        if (this.addBankDataModal.bankdata.card_expiration === '') { return false } else { return null }
      }
    }
  },
  data () {
    return {
      bankdatas: [],
      fields: [
        {key: 'id_bank_data', sortable: true},
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
      addBankDataModal: {
        id: 'add-bankdata-modal',
        title: '',
        bankdata: {
          id_bank_data: '',
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
      isPostNotPut: true,
      filterId: null,
      filterOn: [],
      notDetail: true
    }
  },
  methods: {
    getBankDatas () {
      const path = this.$heroku + '/bankdatas'
      axios.get(path, {
        auth: { username: this.token }
      })
        .then((res) => {
          this.bankdatas = res.data.bankdatas
        })
        .catch((error) => {
          console.error(error)
        })
    },
    deleteBankData (bankdataId, userId) {
      const path = this.$heroku + `/bankdata/${bankdataId}`
      axios.delete(path, {data: { user_id: userId }}, {
        auth: { username: this.token }
      })
        .then((res) => {
          console.log(res)
          this.getBankDatas()
        })
        .catch((error) => {
          console.error(error)
        })
    },
    addBankData (button) {
      this.isPostNotPut = true
      this.addBankDataModal.title = 'Add a new BankData'
      this.openBankDataModal(button)
    },
    modifyBankData (bankdata, button) {
      this.isPostNotPut = false
      this.addBankDataModal.bankdata = bankdata
      this.addBankDataModal.title = `Modify BankData with id: ${bankdata.id_bank_data}`
      this.openBankDataModal(button)
    },
    openBankDataModal (button) {
      this.$root.$emit('bv::show::modal', this.addBankDataModal.id, button)
    },
    checkFormValidity () {
      const valid = this.$refs.form.checkValidity()
      this.addBankDataModal.user_id_state = true
      this.addBankDataModal.card_number_state = true
      this.addBankDataModal.card_owner_state = true
      this.addBankDataModal.card_cvv_state = true
      this.addBankDataModal.card_expiration_state = true
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
        this.postBankData()
      } else {
        this.putBankData()
      }
      this.$nextTick(() => {
        this.resetAddBankDataModal()
        this.$bvModal.hide(this.addBankDataModal.id)
      })
    },
    postBankData () {
      const path = this.$heroku + `/bankdata`
      const bankdata = {
        'user_id': this.addBankDataModal.bankdata.user_id,
        'card_number': this.addBankDataModal.bankdata.card_number,
        'card_owner': this.addBankDataModal.bankdata.card_owner,
        'card_cvv': this.addBankDataModal.bankdata.card_cvv,
        'card_expiration': this.addBankDataModal.bankdata.card_expiration
      }
      axios.post(path, bankdata, {
        auth: { username: this.token }
      })
        .then((res) => {
          console.log(res)
          this.getBankDatas()
        })
        .catch((error) => {
          console.error(error)
          this.getBankDatas()
        })
    },
    putBankData () {
      const path = this.$heroku + `/bankdata/${this.addBankDataModal.bankdata.id_bank_data}`
      const bankdata = {
        'user_id': this.addBankDataModal.bankdata.user_id,
        'card_number': this.addBankDataModal.bankdata.card_number,
        'card_owner': this.addBankDataModal.bankdata.card_owner,
        'card_cvv': this.addBankDataModal.bankdata.card_cvv,
        'card_expiration': this.addBankDataModal.bankdata.card_expiration
      }
      axios.put(path, bankdata, {
        auth: { username: this.token }
      })
        .then((res) => {
          console.log(res)
          this.getBankDatas()
        })
        .catch((error) => {
          console.error(error)
          this.getBankDatas()
        })
    },
    resetAddBankDataModal () {
      this.addBankDataModal = {
        id: 'add-bankdata-modal',
        title: '',
        bankdata: {
          id_bank_data: '',
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
      this.infoModal.title = `BankData Detail of bankdata ${id}`
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
    this.getBankDatas()
    const id = this.$route.params.id
    if (id != null) {
      this.filterId = id.toString()
      this.notDetail = false
    }
    this.token = this.$store.state.token
  }
}
</script>
