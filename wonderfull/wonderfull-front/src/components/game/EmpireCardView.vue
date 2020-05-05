<template>
<v-card>
  <v-card-title class="subtitle-1 pa-2">
      <v-icon class="mr-2">mdi-castle</v-icon> <EmpireName :empire="currentEmpire" />
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

    currentEmpire() {
      if (this.empire < 0)
        return null;

      return this.empires[this.empire];
    }
  }
}
</script>
