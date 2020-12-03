<template>
  <div id="app">
    <h1> {{ message }} </h1>
    <button class="btn btn-success btn-lg" @click="addLike"> like </button>
    <h4> Total tickets bought: {{ likes }} </h4>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data () {
    return {
      message: 'Ramo Industries',
      likes: 0
    }
  },
  methods: {
    addLike () {
      this.likes += 1
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
