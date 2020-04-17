<template>
<v-container>
  <v-dialog v-model="doOpen" persistent>
    <v-card>
      <v-card-title>Create a new game</v-card-title>
      <v-card-text>
        <v-autocomplete v-model="selected" :items="possiblesUsers" filled chips label="Users" item-text="label" item-value="name" multiple />
      </v-card-text>
      <v-card-actions>
        <v-btn @click="doCreate">Create</v-btn>
        <v-btn @click="doOpen=false" outlined>Cancel</v-btn>
      </v-card-actions>
    </v-card>
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
            <v-list-item-subtitle>Created by {{g.creator}} at {{formatDate(g)}} with {{formatUsers(g)}}</v-list-item-subtitle>
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
import moment from 'moment';

export default {
  data() {
    return {
      myGames: [],
      users: [],
      selected: [],
      subs: [],
      doOpen: false
    };
  },
  computed: {
    uid() {
      return this.$store.getters['user/uid'];
    },
    possiblesUsers() {
      return this.users.flatMap(u => {
        if (u.name === this.uid)
          return [];
        else
          return [u];
      });
    }
  },
  methods: {
    openDialog() {
      this.doOpen = true;
    },
    doCreate() {
      const users = this.selected.map(s => {
        return {
          uid: s
        };
      });

      const uid = this.uid;
      if (!users.find(u => u.uid === uid)) {
        users.push({
          uid
        });
      }

      this.$axios.post("game/bootstrap", {
        users
      });
    },
    formatUsers(g) {
      return g.users.map(u => u.label).join(', ');
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
    },
    refreshUsers() {
      this.$axios.get("auth/users").then(r => {
        this.users.splice(0, this.users.length);
        Array.prototype.push.apply(this.users, r.data);
      });
    }
  },
  mounted() {
    [`/app/my-games`, `/user/my-games`].forEach(item => {
      this.subs.push(this.$stomp.subscribe(item, this.receive));
    });
    this.refreshUsers();
  },
  unmounted() {
    this.subs.forEach(s => s.unsubscribe());
  }
}
</script>
