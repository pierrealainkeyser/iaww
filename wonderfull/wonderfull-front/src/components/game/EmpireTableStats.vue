<template>
<v-simple-table dense>
  <thead>
    <tr>
      <th></th>
      <th v-for="(empire,i) in empires" :key="i" style="text-align: center;">
        <span :class="empire.playerColor+'--text rotate'" v-ripple @click="onEmpire(empire, i)">
          {{empire.player}}
        </span>
      </th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <v-icon v-on="on" :size="25">mdi-access-point-network</v-icon>
          </template>
          <span>Connected status</span>
        </v-tooltip>
      </td>
      <td v-for="(empire,i) in empires" :key="i" class="text-center font-weight-bold">
        <v-fade-transition mode="out-in">
          <v-icon key="on" class="pa-0" v-if="isConnected(empire)" :size="20">mdi-access-point-network</v-icon>
          <v-icon key="of" class="pa-0" v-else :size="20">mdi-access-point-network-off</v-icon>
        </v-fade-transition>
      </td>
    </tr>
    <tr>
      <td>
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <v-icon v-on="on" :size="25">mdi-timer-sand</v-icon>
          </template>
          <span>Waiting status</span>
        </v-tooltip>
      </td>
      <td v-for="(empire,i) in empires" :key="i" class="text-center font-weight-bold">
        <v-progress-circular class="pa-0" :size="20" v-if="!empire.done" indeterminate color="primary" />
        <v-icon class="pa-0" v-else :size="20">mdi-checkbox-marked-circle-outline</v-icon>
      </td>
    </tr>
    <tr v-for="(s,index) in stats" :key="index" :class="rowClass(s)">
      <td class="pl-1">
        <template v-if="isDelta(s)">+ </template>
        <Token :type="tokenType(s)" :size="20" />
      </td>
      <td v-for="(empire,i) in empires" :key="i" class="text-center">
        <fade-text :text="empire.stats[s]" />
      </td>
    </tr>
    <tr>
      <td class="pl-1">
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <v-icon v-on="on"  color="yellow" :size="25">mdi-star</v-icon>
          </template>
          <span>Score</span>
        </v-tooltip>

      </td>
      <td v-for="(empire,i) in empires" :key="i" class="text-center">
        <fade-text class="font-weight-bold" :text="empire.score" />
      </td>
    </tr>
  </tbody>
</v-simple-table>
</template>

<script>
import {
  mapGetters
} from 'vuex'


export default {
  computed: {
    ...mapGetters({
      empires: 'game/empires',
      turnStatus: 'game/turnStatus'
    }),
    stats() {
      return Object.keys(this.empires[0].stats);
    }
  },
  methods: {
    onEmpire(empire, index) {
      this.$emit('empire', {
        empire,
        index
      })
    },
    rowClass(s) {
      var out = "colored";
      if (this.step === 'KRYSTALIUM') {
        if (s.match(/delta(.*)/)) {
          out += " blue-grey darken-2";
        }
      } else if (this.step === s.toUpperCase()) {
        out += " blue-grey darken-2";
      }
      return out;
    },
    tokenType(s) {
      const match = s.match(/delta(.*)/);
      if (match) {
        s = match[1];
      }
      return s.toUpperCase();
    },
    isDelta(s) {
      return !!s.match(/delta(.*)/);
    },
    isConnected(empire) {
      const id = empire.playerId;
      const index = this.$store.getters["users/all"].findIndex(u => u.name === id);
      return index >= 0;
    }
  }
}
</script>

<style scoped>
.v-data-table td,
.v-data-table th {
  padding: 0px;
}


.rotate {
  -ms-writing-mode: tb-rl;
  writing-mode: vertical-rl;
  transform: rotate(180deg);
  white-space: nowrap;
  cursor: pointer;
}

.colored {
  transition: background-color .5s;
}
</style>
