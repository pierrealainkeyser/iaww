<template>
<v-list>

  <v-menu v-for="(item,i) in definitions" :key="i" transition="slide-x-transition">
    <template v-slot:activator="{ on }">
  <v-list-item v-on="on">
    <v-list-item-icon>
      <Scoring :score="item.scoring" :size="20" />
    </v-list-item-icon>

    <v-list-item-content>
      <Produce :produce="item.produce" :size="20" style="justify-content:center;" />
    </v-list-item-content>
    <v-list-item-icon>
      <Token v-if="item.type" :type="item.type" alt :size="20" />
      <v-icon v-else :size="20">mdi-flag</v-icon>
    </v-list-item-icon>
  </v-list-item>
</template>
<v-card width="300">
  <v-card-title :class="headerColor(item.type)+ ' subtitle-1 pt-1 pb-1'">
    <Token v-if="item.type" :type="item.type" alt :size="25" />
    <v-icon v-else :size="25">mdi-flag</v-icon>
    <span class="text-capitalize ml-1 subtitle-2">{{item.label}}</span>
  </v-card-title>
  <v-card-text>
    <CardStats :def="item" />
  </v-card-text>
</v-card>
</v-menu>
  <v-subheader class="justify-center font-weight-light caption">{{cardsCount}} in empire</v-subheader>
</v-list>
</template>

<script>
import {
  typeColor
} from './colors'

export default {

  props: {
    builded: {
      type: Array,
      required: true
    }
  },

  methods: {
    headerColor(type) {
      return typeColor(type);
    }
  },

  computed: {
    cardsCount() {
      const count = this.definitions.length - 1;
      if (count === 0)
        return 'no card';
      else if (count > 1)
        return `${count} cards`;
      else
        return `${count} card`;
    },
    definitions() {
      return this.builded.map(b => b.def).reverse();
    }
  }
}
</script>
<style scoped>
.v-list-item {
  min-height: inherit;
  padding: 0px;
}

.v-list-item__content {
  padding: 1px;
}

.v-list-item__icon {
  margin: 1px;
}
</style>
