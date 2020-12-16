<template>
  <div>
    <br>
    <gmap-map
      :center="center"
      :zoom="11"
      style="width:100%;  height: 400px;"
    >
      <gmap-marker
        :key="index"
        v-for="(m, index) in markers"
        :position="m.position"
        @click="center=m.position"
      ></gmap-marker>
      <gmap-polygon :paths="path"></gmap-polygon>
    </gmap-map>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'GoogleMap',
  data () {
    return {
      // default to Montreal to keep it simple
      // change this to whatever makes sense
      center: { lat: 41.385, lng: 2.187 },
      markers: [],
      places: [],
      currentPlace: null,
      path: []
    }
  },
  mounted () {
    // this.geolocate()
    this.getMapCoords()
  },
  methods: {
    // receives a place object via the autocomplete component
    setPlace (place) {
      this.currentPlace = place
    },
    addMarker () {
      if (this.currentPlace) {
        const marker = {
          lat: this.currentPlace.geometry.location.lat(),
          lng: this.currentPlace.geometry.location.lng()
        }
        this.markers.push({ position: marker })
        this.places.push(this.currentPlace)
        this.center = marker
        this.currentPlace = null
      }
    },
    getMapCoords () {
      const path = this.$heroku + '/mapcoords'
      axios.get(path, {
        auth: { username: this.token }
      })
        .then((res) => {
          this.setPath(res.data.map_coords)
        })
        .catch((error) => {
          console.error(error)
        })
    },
    setPath (coords) {
      let path = []
      for (let i = 0; i < coords.length; i++) {
        path.push({lat: coords[i].from_latitude, lng: coords[i].from_longitude})
      }
      this.path = path
    }
  }
}
</script>
