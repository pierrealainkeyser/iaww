<template>
<div class="d-flex align-center pl-1 pr-1 font-weight-medium">
  {{amount}}
  <template v-if="type">
      <v-icon class="x-cross" size="9">mdi-close</v-icon>
      <Token :type="type" :alt="alt" :size="size" />
  </template>
</div>
</template>

<script>
export default {
  props: {
    amount: {
      type: [String, Number],
      required: false
    },
    type: {
      type: String,
      required: false
    },
    alt: {
      type: Boolean,
      defaultValue: true
    },
    size: {
      type: Number,
      default: 15
    }
  }
}
</script>
