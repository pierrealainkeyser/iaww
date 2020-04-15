<template>
<SingleToken :amount="amount" :type="type" :size="size" alt/>
</template>

<script>
export default {

  props: {
    score: {
      type: Object
    },
    size: {
      type: Number,
      default: 15
    }
  },

  computed: {
    matches() {
      if (this.score && this.score.empire) {
        return this.score.empire.match(/(\w+)(?:\*(\d))?/);
      }
      return null;
    },
    amount() {
      if (this.matches) {
        return parseInt(this.matches[2] || "1");
      }
      if (this.score) {
        return this.score.constant;
      }
      return null;
    },
    type() {
      if (this.matches) {
        return this.matches[1];
      }
      return null;
    }
  }
}
</script>
