<template>
<span>
  Turn
  <fade-text class="ml-1 mr-1" :text="turnStatus.turn" />
  -
  <fade-text class="ml-1 mr-1" :text="turnStatus.phase" />
  <v-fade-transition mode="out-in">
    <Token v-if="turnStatus.step" :key="turnStatus.step" class="ml-2" alt :type="turnStatus.step" :size="25" />
  </v-fade-transition>

    <template v-if="'DRAFT'===turnStatus.phase">
      <v-fade-transition mode="out-in">
        <v-icon v-if="turnStatus.turn%2===0" :key="l">mdi-arrow-left</v-icon>
        <v-icon v-else :key="2">mdi-arrow-right</v-icon>
      </v-fade-transition>
    </template>
</span>
</template>

<script>
import {
  mapGetters
} from 'vuex'


export default {
  computed: {
    ...mapGetters({
      turnStatus: 'game/turnStatus',
    })
  }
}
</script>
