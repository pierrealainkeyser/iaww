<template>
<span class="font-weight-bold" :class="color">
    {{name}}
  </span>
</template>

<script>
export default {
  props: {
    empire: {
      type: Object,
      required: false
    }
  },
  computed: {
    color() {
      return this.empire ? this.empire.playerColor + '--text' : null;
    },
    name() {
      return this.empire ? this.empire.player : null;
    }
  }
}
</script>
