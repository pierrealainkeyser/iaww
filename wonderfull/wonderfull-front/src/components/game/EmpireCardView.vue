<template>
<v-card >
  <v-card-title class="subtitle-1 pa-2">
    <a :id="computedId"><v-icon class="mr-2">mdi-flag</v-icon></a>
    <EmpireName :empire="currentEmpire" />

    <v-spacer />
    <v-icon color="yellow" :size="25">mdi-star</v-icon>
    <fade-text class="font-weight-bold" :text="score" />
  </v-card-title>
  <EmpireCards :currentEmpire="currentEmpire" component="v-card-text" />
  <slot></slot>
</v-card>
</template>

<script>
import {
  mapGetters
} from 'vuex';

export default {
  props: {
    empire: {
      type: Number
    }
  },

  computed: {
    ...mapGetters({
      empires: 'game/empires'
    }),

    computedId() {
      return "empire_" + this.empire;
    },

    score() {
      if (this.currentEmpire) {
        return this.currentEmpire.score;
      }
      return null;
    },

    currentEmpire() {
      if (this.empire < 0)
        return null;

      return this.empires[this.empire];
    }
  }
}
</script>
