<template>
<v-dialog v-model="action.supremacy" persistent max-width="500px">
  <v-card>
    <v-card-title>Supremacy</v-card-title>
    <v-card-text>You've won the science supremacy ! Choose your reward. </v-card-text>
    <v-card-actions>
      <v-btn outlined @click="doAct('GENERAL')">
        General
        <Token type="GENERAL" />
      </v-btn>
      <v-btn outlined @click="doAct('BUSINESSMAN')">
        Businessman
        <Token type="BUSINESSMAN" />
      </v-btn>
    </v-card-actions>
  </v-card>
</v-dialog>
</template>

<script>
import {
  mapGetters
} from 'vuex'


export default {

  computed: {
    ...mapGetters({
      action: 'game/action'
    })
  },

  methods: {
    doAct(token) {
      this.$emit('action', {
        parent: {
          action: 'supremacy'
        },
        action: {
          general: 'GENERAL' === token
        }
      });
    }
  }
}
</script>
