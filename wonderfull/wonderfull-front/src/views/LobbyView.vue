<template>
<v-container>
  <v-dialog v-model="doOpen" persistent>
    <v-card>
      <v-card-title>Create a new game</v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="12">
            <v-autocomplete v-model="selected" :items="possiblesUsers" chips label="Users" item-text="label" item-value="name" multiple />
          </v-col>
          <v-col cols="12">
            <div class="d-flex">
              <v-checkbox v-model="additionalDict" label="Kickstarter exclusive cards" value="ks0" />
              <v-checkbox v-model="additionalDict" label="War or Peace" value="wop" class="ml-1" />
            </div>
          </v-col>
          <v-col cols="12">
            <v-select v-model="startingEmpire" :items="startingEmpires" label="Starting empire" />
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-btn @click="doCreate" :disabled="!startingIsPossible">Create</v-btn>
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
      <v-list subheader three-line avatar>
        <v-subheader>My games</v-subheader>
        <v-list-item v-for="(g,i) in myGames" :key="i">

          <v-list-item-avatar>
            <v-icon v-if="g.terminated">mdi-check</v-icon>
            <v-btn v-else icon :to="formatLink(g)">
              <v-icon>mdi-arrow-expand</v-icon>
            </v-btn>
          </v-list-item-avatar>

          <v-list-item-content>
            <v-list-item-title>
              <router-link :to="formatLink(g)">{{formatDict(g)}}</router-link>
            </v-list-item-title>
            <v-list-item-subtitle class="text--primary">{{formatUsers(g)}}</v-list-item-subtitle>
            <v-list-item-subtitle class="font-weight-light">created at {{formatDate(g)}}</v-list-item-subtitle>
          </v-list-item-content>

          <v-list-item-action>
            <v-btn v-if="isCreator(g)" icon @click="doDelete(g)" :loading="g.deleting">
              <v-icon>mdi-delete</v-icon>
            </v-btn>
          </v-list-item-action>
        </v-list-item>
      </v-list>
    </v-card-text>
  </v-card>
</v-container>
</template>

<script>
import moment from 'moment';

function getRandomInt(max) {
  return Math.floor(Math.random() * Math.floor(max));
}

function getRandomEmpire(face) {
  const possiblesEmpires = ["noram", "panafrican", "north", "oceania", "asia", "astec", "europe"];
  const rnd = possiblesEmpires[getRandomInt(possiblesEmpires.length)];
  return `${rnd}_${face}`;
}

export default {
  data() {
    return {
      myGames: [],
      users: [],
      selected: [],
      additionalDict: [],
      subs: [],
      doOpen: false,
      startingEmpire: "basic",
      startingEmpires: [{
        value: "basic",
        text: "Basic"
      }, {
        value: "krystalium",
        text: "Krystalium"
      }, {
        value: "F",
        text: "Random F faces (corruption)"
      }]
    };
  },
  computed: {
    startingIsPossible() {
      return this.selected.length > 0 && this.selected.length < 5;
    },
    uid() {
      return this.$store.getters['user/uid'];
    },
    possiblesUsers() {
      return this.$store.getters["users/all"].flatMap(u => {
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

    createUser(uid) {
      return {
        uid,
        empire: this.startingEmpire
      };
    },

    doCreate() {
      const users = [this.createUser(this.uid)];
      this.selected.forEach(uid => users.push(this.createUser(uid)));
      const dictionaries = ["empire", "core", ...this.additionalDict];

      if ("F" === this.startingEmpire) {
        users.forEach(user => {
          user.empire = getRandomEmpire("F");
        });
      }

      this.$axios.post("game/bootstrap", {
          users,
          dictionaries,
          startingEmpire: this.startingEmpire
        })
        .then(r => {
          const id = r.data.externalId;
          this.$router.push(`/game/${id}`);
        })
        .finally(() => {
          this.doOpen = false;
        });
    },
    doDelete(g) {
      g.deleting = true;
      this.$axios.delete(`game/${g.id}`).then(() => {
        const index = this.myGames.findIndex(gi => gi.id === g.id);
        if (index >= 0)
          this.myGames.splice(index, 1);
      });
    },
    isCreator(g) {
      return g.creator === this.uid;
    },
    formatLink(g) {
      return `/game/${g.externalId}`;
    },
    formatUsers(g) {
      return g.users.map(u => u.label).join(', ');
    },
    formatDict(g) {
      const conf = this.startingEmpires.find(s => s.value === g.startingEmpire) || {
        text: g.startingEmpire
      };

      var text = conf.text;
      if (g.dictionaries.indexOf("ks0") >= 0)
        text += " + Kickstarter exclusive cards";

      if (g.dictionaries.indexOf("wop") >= 0)
        text += " + War or Peace";

      return `Empire card : ${text}`;
    },
    formatDate(g) {
      return moment(g.createdAt).format('YYYY/MM/DD HH:mm:ss');
    },
    receive(games) {
      this.myGames.splice(0, this.myGames.length);
      Array.prototype.push.apply(this.myGames, games.map(g => {
        return {
          ...g,
          deleting: false
        }
      }));

      this.myGames.sort((l, r) => {
        let comparison = 0;
        if (l.createdAt > r.createdAt) {
          comparison = -1;
        } else if (l.createdAt < r.createdAt) {
          comparison = 1;
        }
        return comparison;
      })
    }
  },
  mounted() {
    [`/app/my-games`, `/user/my-games`].forEach(item => {
      this.subs.push(this.$stomp.subscribe(item, this.receive));
    });
  },
  unmounted() {
    this.subs.forEach(s => s.unsubscribe());
  }
}
</script>
