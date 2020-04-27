<template>
<v-app>
  <v-content>
    <system-drawer />
    <v-fade-transition mode="out-in">
      <router-view />
    </v-fade-transition>
  </v-content>

  <v-snackbar v-model="popDisconnected" color="error">
    You have been disconnected

    <v-btn text @click="popDisconnected = false">
      Close
    </v-btn>
  </v-snackbar>
</v-app>
</template>

<script>
export default {
  name: 'IAWW',

  data() {
    return {
      subs: [],
      connected: false,
      popDisconnected: false
    }
  },

  methods: {
    receive(event) {
      this.$store.dispatch('users/event', event);
    }
  },

  watch: {
    connected(newValue) {
      this.popDisconnected = !newValue;
    }
  },

  mounted() {
    ['/topic/users', '/app/users'].forEach(item => {
      this.subs.push(this.$stomp.subscribe(item, this.receive));
    });

    this.$stomp.addListener(s => {
      this.connected = s.status;
    });
  },
  unmounted() {
    this.subs.forEach(s => s.unsubscribe());
  },

  created() {
    this.$vuetify.theme.dark = true
  }
};
</script>
