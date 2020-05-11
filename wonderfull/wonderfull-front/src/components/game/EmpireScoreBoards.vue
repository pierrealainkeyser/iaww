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
    <tr v-for="(s,index) in stats" :key="index">
      <td class="pl-1">
        <Token :type="s" :size="20" />
      </td>
      <td v-for="(empire,i) in empires" :key="i" class="text-center">
        <fade-text :text="empire.scoreBoard[s]" />
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
  data() {
    return {
      stats: ["MATERIAL", "ENERGY", "SCIENCE", "GOLD", "DISCOVERY", "GENERAL", "BUSINESSMAN"]
    }
  },
  computed: {
    ...mapGetters({
      empires: 'game/empires'
    })
  },
  methods: {
    onEmpire(empire, index) {
      this.$emit('empire', {
        empire,
        index
      })
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
</style>
