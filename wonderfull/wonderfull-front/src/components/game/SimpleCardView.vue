<template>
<v-tooltip bottom>
  <template v-slot:activator="{ on }">
      <v-chip label x-small v-on="on" class="text-capitalize font-weight-bold" :color="headerColor">{{card.label}}</v-chip>
  </template>
  <v-card>
    <v-card-title :class="headerColor+ ' subtitle-1 pt-1 pb-1'">
      <Token :type="card.type" alt :size="25" />
      <span class="text-capitalize ml-1 subtitle-2">{{card.label}}</span>
    </v-card-title>
    <v-card-text>
      <CardStats :def="card" />
    </v-card-text>
  </v-card>
</v-tooltip>
</template>

<script>
import {
  typeColor
} from './colors'

export default {
  props: {
    card: {
      type: Object,
      required: true
    }
  },
  computed: {
    headerColor() {
      return typeColor(this.card.type);
    },
    color() {
      return this.headerColor + '--text';
    }
  }
}
</script>

<style scoped>
.v-tooltip__content {
  padding: 0px;
}
</style>
