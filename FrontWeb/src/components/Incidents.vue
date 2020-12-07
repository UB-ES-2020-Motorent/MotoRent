<template>
  <div id="app">
    <h1>Incidents</h1>
    <div class="container">
      <b-table hover :items="incidents" :fields="fields">
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
  name: 'Incidence',
  data () {
    return {
      incidents: [],
      fields: ['id', 'user_id', 'moto_id', 'comment'],
      infoModal: {
        id: 'info-modal',
        title: '',
        content: ''
      }
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
  }
}
</script>
