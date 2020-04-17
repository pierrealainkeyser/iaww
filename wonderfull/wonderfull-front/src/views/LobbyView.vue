<template>
<v-container>
  <v-dialog v-model="doOpen">

  </v-dialog>

  <v-card>
    <v-toolbar>
      <v-icon>mdi-folder-search</v-icon> Lobby
      <template v-slot:extension>
        <v-btn fab small color="primary" bottom left absolute @click="openDialog">
          <v-icon>mdi-plus</v-icon>
        </v-btn>
      </template>
    </v-toolbar>

    <v-card-text>
      <v-list>
        <v-subheader>My games</v-subheader>
        <v-list-item v-for="(g,i) in myGames" :key="i" link :to="'/game/'+g.externalId">
          <v-list-item-content>
            <v-list-item-title>{{formatDict(g)}}</v-list-item-title>
            <v-list-item-subtitle>Created at {{formatDate(g)}} with {{formatUsers(g)}}</v-list-item-subtitle>
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
import moment from 'moment';

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
    },
    formatUsers(g) {
      return g.users.join(', ');
    },
    formatDict(g) {
      return g.dictionaries.join(' + ');
    },
    formatDate(g) {
      return moment(g.createdAt).format('YYYY/MM/DD HH:mm:ss');
    },
    receive(games) {
      this.myGames.splice(0, this.myGames.length);
      Array.prototype.push.apply(this.myGames, games);
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
