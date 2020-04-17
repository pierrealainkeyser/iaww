<template>
<v-container>
  <v-card>
    <v-card-title>Please identify yourself</v-card-title>
    <v-card-text>Choose an OpenID Connect provider bellow</v-card-text>
    <v-card-actions>
      <v-btn v-for="(l,i) in links" :key="i" :href="l.uri">
        {{l.name}}
      </v-btn>
    </v-card-actions>
  </v-card>
</v-container>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      links: []
    };
  },
  mounted() {

    var url = process.env.VUE_APP_BACKEND;
    if (!url) {
      var hostport = location.hostname + ":" + location.port;
      url = "http://" + hostport + "/";
    }
    axios.get(url + "auth/principal", {
      withCredentials: true
    }).then(r => {

      const xcsrf = r.headers['x-csrf'];
      this.$store.dispatch('user/login', {
        name: r.data.name,
        uid: r.data.uid,
        xcsrf
      });
      this.$router.push('/lobby');
    }).catch(e => {
      const response = e.response;
      if (response && 401 === response.status) {
        const data = response.data;
        Object.keys(data).forEach(key => {
          const value = data[key];
          this.links.push({
            name: key,
            uri: url + value
          })
        });
      } else {
        console.log(e);
      }

    });
  },
  methods: {
    login() {
      this.$store.dispatch('user/login', this.user);
      this.$router.push('/lobby');
    }
  }
}
</script>
