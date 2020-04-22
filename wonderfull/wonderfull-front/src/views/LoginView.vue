<template>
<v-container>
  <v-card>
    <v-card-title>Please identify yourself</v-card-title>
    <v-card-text>Choose an OpenID Connect provider bellow</v-card-text>
    <v-card-text>
      <v-progress-circular v-if="loading" indeterminate color="primary" />
    </v-card-text>
    <v-card-actions>
      <v-btn v-for="(l,i) in links" :key="i" :href="l.uri">
        {{l.name}}
      </v-btn>
    </v-card-actions>
  </v-card>
</v-container>
</template>

<script>
export default {
  data() {
    return {
      links: [],
      loading: false
    };
  },
  mounted() {
    this.loading = true;
    this.$axios.get("auth/principal").then(r => {
      const xcsrf = r.headers['x-csrf-token'];
      this.$axios.defaults.headers["X-CSRF-TOKEN"] = xcsrf;
      this.$store.dispatch('user/login', {
        name: r.data.name,
        label: r.data.label,
        xcsrf: xcsrf
      });
      this.$router.push('/lobby');
    }).catch(e => {
      const response = e.response;
      if (response && 401 === response.status) {
        const data = response.data;
        const baseURL = this.$axios.defaults.baseURL || "";
        Object.keys(data).forEach(key => {
          const value = data[key];
          this.links.push({
            name: key,
            uri: baseURL + value
          })
        });
      } else {
        console.log(e);
      }

    }).finally(() => {
      this.loading = false;
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
