<template>
<v-container>
  <v-dialog v-model="doOpen">

  </v-dialog>

  <v-card>
    <v-toolbar>
      Lobby
      <template v-slot:extension>
        <v-btn fab small color="primary" bottom left absolute @click="openDialog">
          <v-icon>mdi-plus</v-icon>
        </v-btn>
      </template>
      <v-spacer />
      <v-btn outlined @click="logout">Logout</v-btn>
    </v-toolbar>

    <v-card-text>
      <v-list>
        <v-subheader>My games</v-subheader>
        <v-list-item v-for="(g,i) in myGames" :key="i" link :to="'/game/'+g.externalId">
          <v-list-item-content>
            <v-list-item-title>{{formatDict(g)}}</v-list-item-title>
            <v-list-item-subtitle> {{formatUsers(g)}}</v-list-item-subtitle>
          </v-list-item-content>
          <v-list-item-avatar>
            <v-icon v-if="g.terminated">mdi-check</v-icon>
            <v-icon v-else>mdi-arrow-expand</v-icon>
          </v-list-item-avatar>
        </v-list-item>
      </v-list>
    </v-card-text>
  </v-card>
</v-container>
</template>

<script>
import StompService from '@/services/StompService';

export default {
  data() {
    return {
      myGames: [],
      subs: [],
      doOpen: false
    };
  },
  methods: {
    openDialog() {
      this.doOpen = true;
      console.log("openDialog")
    },
    formatUsers(g) {
      return g.users.join(', ');
    },
    formatDict(g) {
      return g.dictionaries.join(' + ');
    },
    receive(games) {
      this.myGames.splice(0, this.myGames.length);
      Array.prototype.push.apply(this.myGames, games);
    },
    logout() {
      this.$store.dispatch('user/logout');
      this.$router.push('/login');
    }
  },
  mounted() {
    [`/app/my-games`, `/user/my-games`].forEach(item => {
      this.subs.push(StompService.subscribe(item, this.receive));
    });
  },
  unmounted() {
    this.subs.forEach(s => s.unsubscribe());
  }
}
</script>
