<template>
<component v-bind:is="component">
  <v-row>
    <v-col lg="5" md="5" cols="12">
      <BuildedCards :builded="empire.builded" />
    </v-col>

    <v-col lg="7" md="7" cols="12">
      <ActiveCardsFlex title="Production" :cards="empire.inProduction" @action="onAction" />

      <v-expand-transition>
        <ActiveCardsFlex v-if="drafteds.length" title="Drafted" :cards="drafteds" @action="onAction" />
      </v-expand-transition>

      <v-expand-transition>
        <ActiveCardsFlex v-if="inHand.length" title="In hand" :cards="inHand" @action="onAction" />
      </v-expand-transition>
    </v-col>
  </v-row>
  <v-row>
    <v-col>
      <AvailableTokens class="justify-center" :tokens="currentEmpire.available" :size="30" />
    </v-col>
  </v-row>
</component>
</template>

<script>
export default {
  props: {
    currentEmpire: {
      type: Object
    },
    component: {
      type: String,
      required: true
    }
  },
  computed: {
    drafteds() {
      if (this.currentEmpire) {
        return this.currentEmpire.drafteds;
      }
      return []
    },
    inHand() {
      if (this.currentEmpire) {
        return this.currentEmpire.inHand;
      }
      return []
    },
    empire() {
      if (this.currentEmpire) {
        return this.currentEmpire.empire;
      }
      return {
        inProduction: [],
        builded: []
      }
    }
  },
  methods: {
    onAction(event) {
      this.$emit('action', event);
    }
  }
}
</script>
