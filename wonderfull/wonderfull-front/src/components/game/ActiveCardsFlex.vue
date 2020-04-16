<template>
<div>
  <span class="font-weight-light caption">{{title}}</span>
  <div class="d-flex flex-wrap align-start">
    <CardView v-for="(c,i) in cards" :key="i+'card'" :card="c" @action="action" />
  </div>
</div>
</template>

<script>
export default {
  props: {
    title: {
      type: String,
      required: true
    },
    cards: {
      required: true
    }
  },
  methods: {
    action(action) {
      this.$emit('action', action);
    }
  }
}
</script>
