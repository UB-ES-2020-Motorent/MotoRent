<template>
  <div id="app">
    <h1 data-test="mapcoords-title">MapCoords</h1>
    <div class="container" v-if="$store.getters.isLoggedIn">
      <div>
        <google-map/>
      </div>
      <b-table data-test="mapcoords-b-table" responsive striped hover :items="mapCoords" :fields="fields">
        <template #cell(actions)="row">
          <button class="btn btn-secondary btn-sm" @click="modifyMapCoord(row.item, $event.target)"> Modify </button>
          <button class="btn btn-danger btn-sm" @click="deleteMapCoord(row.item)"> X </button>
        </template>
      </b-table>
      <button class="btn btn-success btn-md" @click="addMapCoord($event.target)"> Add Map Coordinates </button>
      <!-- Modal Add MapCoord -->
      <b-modal :id="addMapCoordModal.id" :title="addMapCoordModal.title" centered  @ok="handleOk" @hidden="resetAddMapCoordModal">
        <form ref="form" @submit.stop.prevent="handleSubmit">
          <b-form-group
            :state="fromLatitudeValidity"
            label="From Latitude"
            label-for="from-latitude-input"
            invalid-feedback="From Latitude is required"
          >
            <b-form-input
              id="from-latitude-input"
              v-model="addMapCoordModal.mapCoord.from_latitude"
              :state="fromLatitudeValidity"
              required
            ></b-form-input>
          </b-form-group>
          <b-form-group
            :state="fromLongitudeValidity"
            label="From Longitude"
            label-for="from-longitude-input"
            invalid-feedback="From Longitude is required"
          >
            <b-form-input
              id="from-longitude-input"
              v-model="addMapCoordModal.mapCoord.from_longitude"
              :state="fromLongitudeValidity"
              required
            ></b-form-input>
          </b-form-group>
          <b-form-group
            :state="toLatitudeValidity"
            label="To Latitude"
            label-for="to-latitude-input"
            invalid-feedback="To Latitude is required"
          >
            <b-form-input
              id="to-latitude-input"
              v-model="addMapCoordModal.mapCoord.to_latitude"
              :state="toLatitudeValidity"
              required
            ></b-form-input>
          </b-form-group>
          <b-form-group
            :state="toLongitudeValidity"
            label="To longitude"
            label-for="to-longitude-input"
            invalid-feedback="To Longitude is required"
          >
            <b-form-input
              id="to-longitude-input"
              v-model="addMapCoordModal.mapCoord.to_longitude"
              :state="toLongitudeValidity"
              required
            ></b-form-input>
          </b-form-group>
        </form>
      </b-modal>
    </div>
    <div class="container" v-if="!$store.getters.isLoggedIn">
      <img src="@/assets/stop.jpg" height="200" margin>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import mdbGoogleMap from 'mdbvue'
import GoogleMap from '@/components/GoogleMaps.vue'
export default {
  name: 'MapCoords',
  components: {
    mdbGoogleMap,
    GoogleMap
  },
  computed: {
    fromLatitudeValidity () {
      if (this.addMapCoordModal.from_latilude_state === false) { return null } else {
        if (this.addMapCoordModal.mapCoord.from_latilude === '') { return false } else { return null }
      }
    },
    fromLongitudeValidity () {
      if (this.addMapCoordModal.from_longitude_state === false) { return null } else {
        if (this.addMapCoordModal.mapCoord.from_longitude === '') { return false } else { return null }
      }
    },
    toLatitudeValidity () {
      if (this.addMapCoordModal.to_latitude_state === false) { return null } else {
        if (this.addMapCoordModal.mapCoord.to_latitude === '') { return false } else { return null }
      }
    },
    toLongitudeValidity () {
      if (this.addMapCoordModal.to_longitude_state === false) { return null } else {
        if (this.addMapCoordModal.mapCoord.to_longitude === '') { return false } else { return null }
      }
    }
  },
  data () {
    return {
      mapCoords: [],
      fields: [
        {key: 'from_latitude'},
        {key: 'from_longitude'},
        {key: 'to_latitude'},
        {key: 'to_longitude'},
        {key: 'actions'}
      ],
      addMapCoordModal: {
        id: 'add-map-coord-modal',
        title: '',
        mapCoord: {
          from_latitude: '',
          from_longitude: '',
          to_latitude: '',
          to_longitude: ''
        },
        from_longitude_state: false,
        battery_state: false,
        to_latitude_state: false,
        to_longitude_state: false
      },
      isPostNotPut: true,
      token: ''
    }
  },
  methods: {
    getMapCoords () {
      const path = this.$heroku + '/mapcoords'
      axios.get(path, {
        auth: { username: this.token }
      })
        .then((res) => {
          this.mapCoords = res.data.map_coords
        })
        .catch((error) => {
          console.error(error)
        })
    },
    deleteMapCoord (mapCoord) {
      const path = this.$heroku + `/mapcoord`
      const coords = {
        'from_latitude': mapCoord.from_latitude,
        'from_longitude': mapCoord.from_longitude
      }
      axios.delete(path, { data: coords,
        auth: { username: this.token }
      })
        .then((res) => {
          console.log(res)
          this.getMapCoords()
        })
        .catch((error) => {
          console.error(error)
        })
    },
    addMapCoord (button) {
      this.isPostNotPut = true
      this.openMapCoordModal('Add a new MapCoord', button)
    },
    modifyMapCoord (mapCoord, button) {
      this.isPostNotPut = false
      this.addMapCoordModal.mapCoord = mapCoord
      this.openMapCoordModal(`Modify MapCoord`, button)
    },
    openMapCoordModal (title, button) {
      this.addMapCoordModal.title = title
      this.$root.$emit('bv::show::modal', this.addMapCoordModal.id, button)
    },
    checkFormValidity () {
      const valid = this.$refs.form.checkValidity()
      this.addMapCoordModal.from_latitude = true
      this.addMapCoordModal.from_longitude = true
      this.addMapCoordModal.to_latitude = true
      this.addMapCoordModal.to_longitude = true
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
        this.postMapCoord()
      } else {
        this.putMapCoord()
      }
      this.$nextTick(() => {
        this.resetAddMapCoordModal()
        this.$bvModal.hide(this.addMapCoordModal.id)
      })
    },
    postMapCoord () {
      const path = this.$heroku + `/mapcoord`
      const mapCoord = {
        'from_latitude': this.addMapCoordModal.mapCoord.from_latitude,
        'from_longitude': this.addMapCoordModal.mapCoord.from_longitude,
        'to_latitude': this.addMapCoordModal.mapCoord.to_latitude,
        'to_longitude': this.addMapCoordModal.mapCoord.to_longitude
      }
      axios.post(path, mapCoord, {
        auth: { username: this.token }
      })
        .then((res) => {
          console.log(res)
          this.getMapCoords()
          GoogleMap.getMapCoords()
        })
        .catch((error) => {
          console.error(error)
        })
    },
    putMapCoord () {
      const path = this.$heroku + `/mapcoord`
      const mapCoord = {
        'from_latitude': this.addMapCoordModal.mapCoord.from_latitude,
        'from_longitude': this.addMapCoordModal.mapCoord.from_longitude,
        'to_latitude': this.addMapCoordModal.mapCoord.to_latitude,
        'to_longitude': this.addMapCoordModal.mapCoord.to_longitude
      }
      axios.put(path, mapCoord, {
        auth: { username: this.token }
      })
        .then((res) => {
          console.log(res)
          this.getMapCoords()
          GoogleMap.getMapCoords()
        })
        .catch((error) => {
          console.error(error)
        })
    },
    resetAddMapCoordModal () {
      this.addMapCoordModal = {
        id: 'add-map-coord-modal',
        title: '',
        mapCoord: {
          id: '',
          from_longitude: '',
          battery: '',
          available: '',
          latitude: '',
          longitude: ''
        },
        from_longitude_state: false,
        battery_state: false,
        latitude_state: false,
        longitude_state: false
      }
    }
  },
  created () {
    this.getMapCoords()
    this.token = this.$store.state.token
  }
}
</script>
