<template>
<v-card>
  <v-card-title class="subtitle-1 pa-2">
    <slot></slot>
    <fade-text class="font-weight-light" :k="clock" :text="status" />
    <v-spacer />
    <TurnStatus v-if="!turnStatus.done" />
  </v-card-title>
</v-card>
</template>

<script>
import {
  mapGetters
} from 'vuex';

export default {

  computed: {
    ...mapGetters({
      status: 'game/status',
      turnStatus: 'game/turnStatus',
      clock: 'game/clock'
    })
  }
}
</script>
