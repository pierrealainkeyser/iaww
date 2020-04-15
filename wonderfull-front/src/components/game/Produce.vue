<template>
<div class="produce">
  <template v-for="(p,i) in tokens">
      <v-icon v-if="p.mult" :key="i+'text'" class="x-cross" size="9">mdi-close</v-icon>
      <Token v-else :key="i+'token'" :type="p.type" :alt="p.alt" :size="size" />
  </template>
</div>
</template>

<script>

export default {

  props: {
    produce: {
      type: Array
    },
    size: {
      type: Number,
      default: 15
    }
  },

  computed: {
    tokens() {
      return (this.produce || []).flatMap(p => {

        if (p.constant > 0) {
          const out = [];
          for (var i = 0; i < p.constant; ++i)
            out.push({
              type: p.type,
              alt: false
            });
          return out;
        } else {
          return [{
              type: p.type,
              alt: false
            },
            {
              mult: true
            }, {
              type: p.empire,
              alt: true
            }
          ];
        }
      });
    }
  }
}
</script>

<style scoped>
.produce {
  display: flex;
  align-items: center;
}

.produce>.x-cross {
  margin-left: 0px !important;
  margin-right: 0px;
}

.produce>*:not(:first-child) {
  margin-left: 1px;
}
</style>
