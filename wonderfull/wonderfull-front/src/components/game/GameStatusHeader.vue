<template>
<div>
  <v-card>
    <v-card-title class="subtitle-1 pa-2">
      <slot></slot>
      <fade-text class="font-weight-light" :k="clock" :text="status" />
      <v-spacer />
      <TurnStatus v-if="!done" />
      <v-btn v-else small icon @click="scoreBoard=true">
        <v-icon>mdi-trophy</v-icon>
      </v-btn>
    </v-card-title>
  </v-card>
  <v-dialog v-model="scoreBoard" max-width="600px">
    <v-card>
      <v-card-title class="grey darken-3">
        <v-icon class="pr-1" :color="trophyColor">mdi-trophy</v-icon> - {{status}}

        <template v-if="soloRank">
            - <span class="text-capitalize ml-1">{{soloRank.toLowerCase()}}</span>
        </template>
      </v-card-title>
      <v-card-text class="pa-3">
        <EmpireScoreBoards @empire="onEmpire" />
      </v-card-text>
      <v-card-actions>
        <v-btn @click="scoreBoard=false">Close</v-btn>
        <v-btn outlined @click="backToLobby">To Lobby</v-btn>
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
      soloRank: 'game/soloRank',
      status: 'game/status',
      turnStatus: 'game/turnStatus',
      clock: 'game/clock'
    }),
    done() {
      return this.turnStatus.done;
    },
    trophyColor() {
      if (this.soloRank) {
        if ('FAILURE' === this.soloRank) {
          return null;
        } else if ('BRONZE' === this.soloRank) {
          return "deep-orange accent-3";
        } else if ('SILVER' === this.soloRank) {
          return "grey";
        } else if ('GOLD' === this.soloRank) {
          return "amber";
        }
      }

      return "amber";
    }

  }
}
</script>
