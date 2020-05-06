<template>
<v-card>
  <v-card-title class="subtitle-1 pa-2" :class="self?'grey darken-3':null">
    <v-btn icon @click="viewNext(-1)">
      <v-icon>mdi-chevron-left</v-icon>
    </v-btn>
    <v-btn icon @click="viewSelf" :disabled="self">
      <v-icon class="mr-2">mdi-flag</v-icon>
    </v-btn>
    <EmpireName :empire="currentEmpire" />
    <v-btn icon @click="viewNext(1)">
      <v-icon>mdi-chevron-right</v-icon>
    </v-btn>
  </v-card-title>

  <EmpireCards :currentEmpire="currentEmpire" component="v-card-text" />

  <EmpireActions component="v-card-actions" />

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

  methods: {
    viewNext(direction) {
      this.viewEmpire({
        index: (this.empire + direction + this.empires.length) % this.empires.length
      });
    },

    viewSelf() {
      this.viewEmpire({
        index: this.myself
      });
    },

    viewEmpire(event) {
      this.$emit('empire', event);
    },
  },

  computed: {
    ...mapGetters({
      empires: 'game/empires',
      myself: 'game/myself'
    }),

    currentEmpire() {
      if (this.empire < 0)
        return null;

      return this.empires[this.empire];
    },

    self() {
      return this.empire === this.myself;
    }
  }
}
</script>
