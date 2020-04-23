<template>
<v-simple-table dense>
  <thead>
    <tr>
      <th></th>
      <th v-for="(empire,i) in empires" :key="i" class="rotate">
        <div :class="empire.playerColor+'--text'">
          {{empire.player}}
        </div>
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
    <tr v-for="(s,index) in stats" :key="index">
      <td>
        <Token :type="s.toUpperCase()" :size="20" />
      </td>
      <td v-for="(empire,i) in empires" :key="i+'/stat'" class="text-center">
        <fade-text :text="empire.stats[s]" />
      </td>
    </tr>
    <tr>
      <td>

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
export default {
  props: {
    empires: {
      type: Array,
      required: true
    }
  },
  computed: {
    stats() {
      return Object.keys(this.empires[0].stats);
    }
  },
  methods: {
    isConnected(empire) {
      const id = empire.playerId;
      const index = this.$store.getters["users/all"].findIndex(u => u.name === id);
      return index >= 0;
    }
  }
}
</script>

<style scoped>
.rotate {
  vertical-align: bottom;
  text-align: center;
}

.v-data-table td,
.v-data-table th {
  padding: 0px;
}

.rotate div {
  -ms-writing-mode: tb-rl;
  writing-mode: vertical-rl;
  transform: rotate(180deg);
  white-space: nowrap;
  margin-top: 3px;
  margin-bottom: 3px;
}
</style>
