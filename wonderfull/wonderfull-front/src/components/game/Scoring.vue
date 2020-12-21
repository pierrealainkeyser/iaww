<template>
  <span >
      <span v-if="matchesDual" class="d-flex align-center pl-1 pr-1 font-weight-medium">
        {{amount}}<v-icon class="x-cross" size="9">mdi-close</v-icon>
        <Token :type="matchesDual[1]" :size="size" alt/>
        <Token :type="matchesDual[3]" :size="size" alt/>
      </span>
      <SingleToken v-else :amount="amount" :type="type" :size="size" alt/>
</span>
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
    matchesDual(){

      if (this.score && this.score.dual) {
        return this.score.dual.match(/(\w+)\*(\d+), (\w+)\*\d+/);
      }

      return null;
    },
    matches() {
      if (this.score && this.score.empire) {
        return this.score.empire.match(/(\w+)(?:\*(\d))?/);
      }
      return null;
    },
    amount() {
      if(this.matchesDual){
        return parseInt(this.matchesDual[2]);
      }

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
