<template>
<div class="d-flex">
  <SingleToken v-for="(token,i) in objects" :type="token.type" :amount="token.amount" :key="i" :size="size" />
</div>
</template>

<script>
export default {
  props: {
    tokens: {
      type: Object,
      required: true
    },
    size: {
      type: Number,
      default: 15
    }
  },
  computed: {
    objects() {
      return Object.keys(this.tokens).flatMap(key => {
        const amount = this.tokens[key];
        if (amount > 0) {
          return [{
            type: key.toUpperCase(),
            amount
          }];
        }
        return [];
      });
    }
  }
}
</script>

<style scoped>
.d-flex>*:not(:first-child) {
  margin-left: 1px;
}
</style>
