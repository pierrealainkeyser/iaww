<template>
<v-app>
  <v-content>
    <system-drawer />
    <v-fade-transition mode="out-in">
      <router-view />
    </v-fade-transition>
  </v-content>
</v-app>
</template>

<script>
export default {
  name: 'IAWW',

  data() {
    return {
      subs: []
    }
  },

  methods: {
    receive(event) {
      this.$store.dispatch('users/event', event);
    }
  },

  mounted() {
    ['/topic/users','/app/users'].forEach(item => {
      this.subs.push(this.$stomp.subscribe(item, this.receive));
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
