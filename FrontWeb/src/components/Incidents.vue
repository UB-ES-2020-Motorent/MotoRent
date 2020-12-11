<template>
  <div id="app">
    <h1>Incidents</h1>
    <div class="container">
      <b-table hover :items="incidents" :fields="fields" :filter="filter" :filter-included-fields="filterOn">
        <template #cell(comment)="row">
          <b-button size="sm" @click="info(row.item.comment, row.item.id, $event.target)" class="mr-1">Comment user {{row.item.id}}</b-button>
        </template>
      </b-table>
    </div>
    <!-- Modal Json -->
    <b-modal :id="infoModal.id" :title="infoModal.title" ok-only centered @hide="resetInfoModal">
      <p>{{ infoModal.content }}</p>
    </b-modal>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'Incident',
  computed: {
    filter () { if (this.motoIdId != null) { return this.motoIdId } else { return null } },
    filterOn () { return ['moto_id'] }
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
      motoIdId: null
    }
  },
  methods: {
    getIncidents () {
      const path = this.$heroku + `/incidents`
      axios.get(path)
        .then((res) => {
          this.incidents = res.data.incidents
        })
        .catch((error) => {
          console.error(error)
        })
    },
    deleteIncidences (incident) {

    },
    info (item, index, button) {
      this.infoModal.title = `Comentari de l'usuari ${index}`
      this.infoModal.content = item
      this.$root.$emit('bv::show::modal', this.infoModal.id, button)
    },
    resetInfoModal () {
      this.infoModal.title = ''
      this.infoModal.content = ''
    }
  },
  created () {
    this.getIncidents()
    const id = this.$route.params.id
    if (id != null) { this.motoIdId = id.toString() }
  }
}
</script>
