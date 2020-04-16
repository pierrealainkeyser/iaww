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
import StompService from '@/services/StompService';

export default {
  name: 'IAWW',
  data() {
    return {
      myGames: []
    };
  },

  mounted() {
    StompService.subscribe('/app/my-games', games => {
      this.myGames.splice(0, this.myGames.length);
      Array.prototype.push.apply(this.myGames, games);
    });
  },

  created() {
    this.$vuetify.theme.dark = true
  }
};
</script>
