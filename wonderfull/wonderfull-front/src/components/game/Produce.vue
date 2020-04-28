<template>
<div class="produce tokens">
  <template v-for="(p,i) in tokens">
      <v-icon v-if="p.mult" :key="i+'text'" class="x-cross" size="9">mdi-close</v-icon>
      <Token v-else :key="i+'token'" :type="p.type" :alt="p.alt" :negated="p.disabled" :size="size" />
  </template>
</div>
</template>

<script>
function createTokens(count, type, disabled) {
  const out = [];
  for (var i = 0; i < count; ++i)
    out.push({
      type,
      alt: false,
      disabled
    });
  return out;
}

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
          return createTokens(p.constant, p.type, false);
        } else if (p.constant < 0) {
          return createTokens(-p.constant, p.type, true);
        } else {
          return [{
              type: p.type,
              alt: false,
              disabled: false
            },
            {
              mult: true
            }, {
              type: p.empire,
              alt: true,
              disabled: false
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
</style>
