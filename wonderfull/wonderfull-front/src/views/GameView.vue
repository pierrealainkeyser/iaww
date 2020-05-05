<template>
<v-row dense class="ma-1">
  <AffectationDialog @action="onAction" :available="available" />
  <SupremacyDialog @action="onAction" />

  <v-col :lg="monoLayout?7:10" cols="12">
    <v-row dense>
      <v-col cols="12" class="mt-0 pt-0">
        <GameStatusHeader>
          <v-btn-toggle v-model="layout" class="mr-1" dense group>
            <v-btn small text>
              <v-icon>mdi-fit-to-page-outline</v-icon>
            </v-btn>
          </v-btn-toggle>
        </GameStatusHeader>
      </v-col>

      <template v-if="monoLayout">
        <v-col cols="12">
          <SingleEmpireView @empire="viewEmpire" :empire="empire" />
        </v-col>
      </template>

      <template v-else>
        <v-col cols="12">
          <EmpireCardView :empire="myself">
              <EmpireActions component="v-card-actions" />
          </EmpireCardView>
        </v-col>

        <v-col cols="6" v-for="i in othersEmpire" :key="i">
          <EmpireCardView :empire="i" />
        </v-col>

        <v-col v-if="last>=0" cols="12">
          <EmpireCardView :empire="last"/>
        </v-col>
      </template>

    </v-row>
  </v-col>



  <template v-if="monoLayout">
    <v-col lg="2" md="6" cols="12">
      <v-card>
        <v-card-text>
          <EmpireTableStats @empire="viewEmpire" />
        </v-card-text>
      </v-card>
    </v-col>

    <v-col lg="3" md="6" cols="12">
      <v-card>
        <v-card-text>
          <EventList />
        </v-card-text>
      </v-card>
    </v-col>
  </template>

  <v-col v-else lg="2" cols="12">
    <v-card>
      <v-card-text>
        <EmpireTableStats />
      </v-card-text>
    </v-card>

    <v-card class="mt-2">
      <v-card-text>
        <EventList />
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
} from 'vuex';

export default {

  props: {
    game: {
      type: String,
      required: true
    }
  },

  data() {
    return {
      subs: [],
      lastSync: moment(),
      empire: -1,
      layout: undefined
    }
  },

  computed: {
    available() {
      return this.loaded ? this.empires[this.myself].available : {}
    },

    monoLayout() {
      return this.layout===undefined;
    },

    loaded() {
      return this.empires && this.myself >= 0;
    },

    othersEmpire() {
      const out = [];
      if (this.loaded) {
        const len = this.empires.length;
        if (len >= 3) {
          out.push(this.next(-1));
          out.push(this.next(1));

          if (len >= 5) {
            out.push(this.next(-2));
            out.push(this.next(2));
          }
        }
      }

      return out;
    },



    last() {
      if (this.loaded) {
        const len = this.empires.length;

        if (2 === len) {
          return this.next(1);
        } else if (4 === len) {
          return this.next(2);
        }
      }

      return -1;
    },

    ...mapGetters({
      empires: 'game/empires',
      turnStatus: 'game/turnStatus',
      loaded: 'game/loaded',
      myself: 'game/myself'
    })
  },

  methods: {
    ...mapActions({
      receiveData: 'game/receive',
      onAction: 'game/action'
    }),

    viewEmpire(event) {
      this.empire = event.index;
    },

    next(delta) {
      const len = this.empires.length;
      return (this.myself + delta + len) % len;
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
    }
  },

  watch: {
    myself(newValue) {
      this.empire = newValue;
    }
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
