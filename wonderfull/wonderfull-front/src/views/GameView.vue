<template>
<v-row dense class="ma-1">
  <AffectationDialog @action="onAction" :available="myEmpire?myEmpire.available:{}" />
  <SupremacyDialog @action="onAction" />

  <v-col lg="7" cols="12">
    <v-card>
      <v-card-title class="subtitle-1 pa-2" :class="current===myself?'grey darken-3':null">
        <v-btn icon @click="viewNext(-1)">
          <v-icon>mdi-chevron-left</v-icon>
        </v-btn>
        <v-btn icon @click="viewSelf" :disabled="current===myself">
          <v-icon class="mr-2">mdi-castle</v-icon>
        </v-btn>
        <span class="font-weight-bold" :class="currentEmpire.playerColor+'--text'">
          {{currentEmpire.player}}
        </span>
        <v-btn icon @click="viewNext(1)">
          <v-icon>mdi-chevron-right</v-icon>
        </v-btn>

        <v-spacer />
        <fade-text class="font-weight-light" :k="clock" :text="status" />
        <v-spacer />

        <template v-if="!turnStatus.done">
          Turn
          <fade-text class="ml-1 mr-1" :text="turnStatus.turn" />
          -
          <fade-text class="ml-1 mr-1" :text="turnStatus.phase" />
          <v-fade-transition mode="out-in">
            <Token v-if="turnStatus.step" :key="turnStatus.step" class="ml-2" alt :type="turnStatus.step" :size="25" />
          </v-fade-transition>
        </template>

        <template v-if="'DRAFT'===turnStatus.phase">
          <v-fade-transition mode="out-in">
            <v-icon v-if="turnStatus.turn%2===0" :key="l">mdi-arrow-left</v-icon>
            <v-icon v-else :key="2">mdi-arrow-right</v-icon>
          </v-fade-transition>
        </template>

      </v-card-title>

      <EmpireCards :currentEmpire="currentEmpire" @action="onAction" component="v-card-text" />

      <v-card-actions>
        <v-tooltip v-if="action.pass" bottom>
          <template v-slot:activator="{ on }">
            <v-btn v-on="on" @click="pass">Pass</v-btn>
          </template>
          <span>End this step</span>
        </v-tooltip>

        <v-tooltip v-if="action.convert" bottom>
          <template v-slot:activator="{ on }">
            <v-btn class="ml-1" v-on="on" @click="convert">Convert</v-btn>
          </template>
          <span>Put all the remaining to your empire card</span>
        </v-tooltip>

        <v-tooltip v-if="action.undo" bottom>
          <template v-slot:activator="{ on }">
            <v-btn class="ml-1" v-on="on" @click="undo">Undo</v-btn>
          </template>
          <span>Undo all the actions done in this step, reset to the situation add the begining of the current step</span>
        </v-tooltip>
      </v-card-actions>
    </v-card>
  </v-col>

  <v-col lg="2" md="6" cols="12">
    <v-card>
      <v-card-text>
        <EmpireTableStats :empires="empires" :step="turnStatus.step" @empire="viewEmpire" />
      </v-card-text>
    </v-card>
  </v-col>

  <v-col lg="3" md="6" cols="12">
    <v-card>
      <v-card-text>
        <EventList :events="events" />
      </v-card-text>
    </v-card>
  </v-col>
</v-row>
</template>

<script>
import moment from 'moment';

import {
  mapActions,
  mapGetters
} from 'vuex'

export default {

  props: {
    game: {
      type: String,
      required: true
    }
  },

  methods: {
    ...mapActions({
      receiveData: 'game/receive',
      onAction: 'game/action'
    }),


    pass() {
      this.onAction({
        parent: {
          action: 'pass'
        }
      });
    },

    undo() {
      this.onAction({
        parent: {
          action: 'undo'
        }
      });
    },

    convert() {
      this.onAction({
        parent: {
          action: 'convert'
        }
      });
    },

    viewNext(direction) {
      this.current = (this.current + direction + this.empires.length) % this.empires.length;
    },

    viewSelf() {
      this.current = this.myself;
    },

    viewEmpire(event) {
      this.current = event.index;
    },

    keepSessionAlive() {
      const now = moment();
      const deltaSync = now.diff(this.lastSync);
      if (deltaSync > 60000) {
        this.$axios.get("auth/principal");
        this.lastSync = now;
      }
    },

    receive(data) {
      this.keepSessionAlive();
      this.receiveData(data);

      if (this.myself < 0) {
        this.myself = data.myself;
        this.current = this.myself;
      }
    }
  },

  data() {
    return {
      subs: [],
      lastSync: moment(),
      myself: -1,
      current: 0
    }
  },

  computed: {
    currentEmpire() {
      return this.empires[this.current];
    },
    myEmpire() {
      if (this.myself < 0)
        return null;
      return this.empires[this.myself];
    },
    ...mapGetters({
      status: 'game/status',
      empires: 'game/empires',
      turnStatus: 'game/turnStatus',
      events: 'game/events',
      loaded: 'game/loaded',
      action: 'game/action',
      clock: 'game/clock'
    })
  },

  mounted() {
    this.$store.dispatch('game/start', this.game);
    [`/app/game/${this.game}`, `/user/game/${this.game}`].forEach(item => {
      this.subs.push(this.$stomp.subscribe(item, this.receive));
    });

  },

  unmounted() {
    this.$store.dispatch('game/reset');
    this.subs.forEach(s => s.unsubscribe());
  }
};
</script>

<style>
.tokens>.v-sheet:not(:first-child) {
  margin-left: 1px;
}
</style>
