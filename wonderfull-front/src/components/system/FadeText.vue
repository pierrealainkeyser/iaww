<template>
<v-fade-transition mode="out-in">
  <span :key="key">{{text}}</span>
</v-fade-transition>
</template>

<script>
export default {
  props: {
    text: {
      type: [String, Number],
      defaultValue: ""
    },
    k: {
      type: [String, Number]
    }
  },
  computed: {
    key() {
      return this.k ? this.k : this.text;
    }
  }
}
</script>
