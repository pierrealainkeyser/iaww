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
      <AvailableTokens class="justify-center" :tokens="available" :size="30" />
    </v-col>
  </v-row>
</component>
</template>

<script>
import {
  mapActions
} from 'vuex'

export default {
  props: {
    currentEmpire: {
      type: Object,
      required: false
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
    available() {
      if (this.currentEmpire) {
        return this.currentEmpire.available;
      }
      return {}
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
    ...mapActions({
      onAction: 'game/action'
    })
  }
}
</script>
