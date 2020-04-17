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

  created() {
    this.$vuetify.theme.dark = true
  }
};
</script>
