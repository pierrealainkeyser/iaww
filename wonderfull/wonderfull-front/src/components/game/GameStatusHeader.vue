<template>
<div>
  <v-card>
    <v-card-title class="subtitle-1 pa-2">
      <slot></slot>
      <fade-text class="font-weight-light" :k="clock" :text="status" />
      <v-spacer />
      <TurnStatus v-if="!done" />
    </v-card-title>
  </v-card>
  <v-dialog v-model="scoreBoard" max-width="600px">
    <v-card>
      <v-card-title class="grey darken-3">
        <v-icon class="pr-1">mdi-trophy</v-icon>{{status}}
      </v-card-title>
      <v-card-text class="pa-3">
        <EmpireScoreBoards @empire="onEmpire" />
      </v-card-text>
      <v-card-actions>
        <v-btn outlined @click="backToLobby">Back</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</div>
</template>

<script>
import {
  mapGetters
} from 'vuex';

export default {
  data() {
    return {
      scoreBoard: false
    };
  },
  methods: {
    backToLobby() {
      this.$router.push('/');
    },
    onEmpire(evt) {
      this.$emit('empire', evt);
    }
  },
  watch: {
    done(newValue) {
      if (newValue) {
        this.scoreBoard = true;
      }
    }
  },
  computed: {
    ...mapGetters({
      status: 'game/status',
      turnStatus: 'game/turnStatus',
      clock: 'game/clock'
    }),
    done() {
      return this.turnStatus.done;
    }
  }
}
</script>
