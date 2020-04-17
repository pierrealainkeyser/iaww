<template>
<div class="d-flex">
  <AffectationDialog @action="onAction" :available="myEmpire?myEmpire.available:{}" />
  <SupremacyDialog @action="onAction" />
  <div class="ma-1" style="width:350px;min-width:350px;">
    <v-card>
      <v-card-text>
        <EventList :events="events" />
      </v-card-text>
    </v-card>
  </div>
  <div class="flex-grow-1 ma-1">
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
        <fade-text class="font-weight-light" :k="clock" :text="currentStatus" />
        <v-spacer />

        <template v-if="!done">
          Turn
          <fade-text class="ml-1 mr-1" :text="turn" />
          -
          <fade-text class="ml-1 mr-1" :text="phase" />
          <v-fade-transition mode="out-in">
            <Token v-if="step" :key="step" class="ml-2" alt :type="step" :size="25" />
          </v-fade-transition>
        </template>
      </v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="4">
            <div>
              <BuildedCards :builded="currentEmpire.empire.builded" />
            </div>
          </v-col>

          <v-col>
            <ActiveCardsFlex title="Production" :cards="currentEmpire.empire.inProduction" @action="onAction" />

            <v-expand-transition>
              <ActiveCardsFlex v-if="currentEmpire.drafteds.length" title="Drafted" :cards="currentEmpire.drafteds" @action="onAction" />
            </v-expand-transition>

            <v-expand-transition>
              <ActiveCardsFlex v-if="currentEmpire.inHand.length" title="In hand" :cards="currentEmpire.inHand" @action="onAction" />
            </v-expand-transition>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <AvailableTokens class="justify-center" :tokens="currentEmpire.available" :size="30" />
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-btn v-if="action.pass" @click="pass">Pass</v-btn>
      </v-card-actions>
    </v-card>
  </div>

  <div class="ma-1">
    <v-card>
      <v-card-text>
        <EmpireTableStats :empires="empires" style="max-width:min-content;" />
      </v-card-text>
    </v-card>
  </div>
</div>
</template>

<script>
//import StompService from '@/services/StompService';

const PLAYERS_COLOR = ["blue", "red", "green", "pink", "amber"];

function createEmptyEmpire(index) {
  return {
    score: 0,
    empire: {
      builded: [],
      inProduction: []
    },
    player: null,
    playerColor: PLAYERS_COLOR[index % PLAYERS_COLOR.length],
    inHand: [],
    stats: {
      material: 0,
      energy: 0,
      science: 0,
      gold: 0,
      discovery: 0,
      krystalium: 0,
      raw: 0,
      businessman: 0,
      general: 0
    },
    drafteds: [],
    available: {
      material: 0,
      energy: 0,
      science: 0,
      gold: 0,
      discovery: 0,
      krystalium: 0,
      businessman: 0,
      general: 0
    },
    done: false
  };
}

function wrapCard(card, dictionnary, cardActions) {
  const id = card.id;
  const actions = (cardActions || []).find(a => a.targetId === id);
  const def = dictionnary.find(d => d.name === card.name);
  var slots = null;

  if (card.slots) {
    const filled = card.slots.filter(s => s.status !== 'EMPTY').length
    slots = {
      values: card.slots,
      filled,
      total: card.slots.length
    };
  }

  return {
    id,
    actions: actions != null ? actions.actions : [],
    label: def.label,
    def,
    slots
  }
}

function mapCards(srcs, targets, dictionnary, cardActions) {
  targets.splice(0, targets.length);
  if (srcs) {
    srcs.forEach(item => {
      targets.push(wrapCard(item, dictionnary, cardActions));
    });
  }
}


function mapEmpire(src, target, dictionnary, actions) {
  mapCards(src.inHand, target.inHand, dictionnary, actions);
  mapCards(src.drafteds, target.drafteds, dictionnary, actions);
  mapCards(src.empire.builded, target.empire.builded, dictionnary, actions);
  mapCards(src.empire.inProduction, target.empire.inProduction, dictionnary, actions);
  target.score = src.score;
  target.done = src.done;
  target.player = src.player;

  Object.keys(target.stats).forEach(key => {
    target.stats[key] = src.stats[key.toUpperCase()] || 0
  });
  target.stats.raw = src.empire.storedRaw;

  const srcAvailable = src.available || {};
  Object.keys(target.available).forEach(key => {
    target.available[key] = srcAvailable[key.toUpperCase()] || 0
  });
}

function mapEvent(evt, empires, dictionnary) {
  var out = {
    at: evt.at,
    player: {
      name: empires[evt.player].player,
      color: empires[evt.player].playerColor
    },
    type: evt.event.type
  }

  if ("draft" === out.type || "move" === out.type) {
    out.card = wrapCard(evt.event.card, dictionnary).def;
  } else if ("recycle" === out.type) {
    out.krystaliumDelta = evt.event.krystaliumDelta;
    out.quantity = evt.event.quantity;
    if (evt.event.recycled) {
      out.type = "recycle_card";
      out.card = wrapCard(evt.event.recycled, dictionnary).def;
    }
  } else if ("affect" === out.type) {
    out.card = wrapCard(evt.event.inProduction, dictionnary).def;
    if (evt.event.recycled) {
      out.recycled = wrapCard(evt.event.recycled, dictionnary).def;
    }
    out.builded = !!evt.event.builded;
    out.tokens = Object.keys(evt.event.affected).map(key => evt.event.affected[key]);
  } else if ("supremacy" === out.type) {
    out.gain = evt.event.gain;
  }

  return out;
}

function mapDictionnary(entry) {
  const out = { ...entry,
    bonus: []
  };
  if (out.name.startsWith('empire/'))
    out.label = "empire";
  else
    out.label = out.name.replace(/_/g, ' ');

  if (entry.bonus) {
    const matches = entry.bonus.match(/(\w+)\*(\d+)/);
    if (matches) {
      const type = matches[1];
      const count = parseInt(matches[2]);
      for (var i = 0; i < count; ++i)
        out.bonus.push(type);
    } else {
      out.bonus.push(entry.bonus);
    }
  }

  if (out.produce) {
    const values = out.produce.values;

    out.produce = Object.keys(values).map(key => {
      const obj = values[key];
      return {
        type: key,
        ...obj
      };
    });
  }
  if (out.cost) {
    out.cost = out.cost.split(", ").flatMap(c => {
      const matches = c.match(/(\w+)\*(\d+)/);
      if (matches) {
        const type = matches[1];
        const count = parseInt(matches[2]);
        const out = [];
        for (var i = 0; i < count; ++i)
          out.push(type);
        return out;
      } else {
        return [c];
      }
    })
  }

  return out;
}

export default {

  props: {
    game: {
      type: String,
      required: true
    }
  },

  methods: {
    sendAction(name, action) {
      this.$stomp.publish(`/app/game/${this.game}/${name}`, action);
    },

    clearInteraction() {
      const recycleToProduction = this.action.recycleToProduction;
      recycleToProduction.active = false;
      recycleToProduction.src = null;
      recycleToProduction.targets.splice(0, recycleToProduction.targets.length);

      const affectation = this.action.affectation;
      affectation.active = false;
      affectation.src = null;
      affectation.slots = {};
    },

    pass() {
      this.onAction({
        parent: {
          action: 'pass'
        }
      });
    },

    onAction(event) {
      const name = event.parent.action;
      const action = event.action;

      const recycleToProduction = this.action.recycleToProduction;
      if ('recycleToProduction' === name) {
        recycleToProduction.active = true;
        recycleToProduction.src = action.targetId;
        Array.prototype.push.apply(recycleToProduction.targets, event.parent.targets);
      } else if ('choose' === name) {
        this.action.ready = false;
        this.sendAction('recycleToProduction', {
          targetId: recycleToProduction.src,
          productionId: action.targetId
        });
        this.clearInteraction();
      } else if ('affectation' === name) {
        const id = event.action.targetId;
        const found = this.myEmpire.empire.inProduction.find(c => c.id === id);

        const affectation = this.action.affectation;
        affectation.active = true;
        affectation.src = found;
        affectation.slots = event.parent.slots;
      } else if ('proceedAffectation' === name) {
        this.action.ready = false;
        this.sendAction('affectation', action);
        this.clearInteraction();
      } else {
        if ('cancel' !== name) {
          this.action.ready = false;
          this.sendAction(name, action);
        }
        this.clearInteraction();
      }
    },

    viewNext(direction) {
      this.current = (this.current + direction + this.empires.length) % this.empires.length;
    },

    viewSelf() {
      this.current = this.myself;
    },

    receive(data) {
      if (this.clock < data.clock) {
        if (data.dictionnary != null) {
          this.dictionnary.splice(0, this.dictionnary.length);
          Array.prototype.push.apply(this.dictionnary, data.dictionnary.map(mapDictionnary));
        }

        this.action.ready = true;
        this.done = data.terminated;
        this.clock = data.clock;
        this.turn = data.turn;
        this.phase = data.phase;
        this.step = data.step;

        const myself = data.myself;

        if (this.myself < 0) {
          this.myself = myself;
          this.current = myself;
        }

        const empires = data.empires;
        const length = empires.length;
        while (this.empires.length < empires.length) {
          this.empires.push(createEmptyEmpire(this.empires.length));
        }

        const actions = (data.actions || {});
        this.action.pass = actions.pass || false;
        this.action.supremacy = actions.supremacy || false;
        const cardsActions = actions.cards;
        for (var i = 0; i < length; ++i) {
          mapEmpire(empires[i], this.empires[i], this.dictionnary, myself === i ? cardsActions : null);
        }

        const newEvents = data.events.map(event => mapEvent(event, this.empires, this.dictionnary));
        newEvents.reverse();

        this.events.splice(0, this.events.length);
        Array.prototype.push.apply(this.events, newEvents);
      }
    },
  },

  provide() {
    return {
      action: this.action
    };
  },

  data() {
    return {
      subs: [],
      clock: -1,
      turn: 0,
      step: null,
      phase: null,
      myself: -1,
      current: 0,
      done: false,
      action: {
        ready: true,
        pass: false,
        supremacy: false,
        recycleToProduction: {
          active: false,
          src: null,
          targets: []
        },
        affectation: {
          active: false,
          src: null,
          slots: {}
        }
      },
      events: [],
      empires: [createEmptyEmpire(0)],
      dictionnary: []
    }
  },

  computed: {
    loaded() {
      return this.clock >= 0;
    },
    currentEmpire() {
      return this.empires[this.current];
    },
    myEmpire() {
      if (this.myself < 0)
        return null;
      return this.empires[this.myself];
    },
    currentStatus() {
      if (this.done) {
        return "Game is terminated";
      }

      if (this.myEmpire) {
        if (this.myEmpire.done)
          return "Waiting for other players";

        if ("DRAFT" === this.phase) {
          return "Choose a card to draft";
        }

        if ("PLANNING" === this.phase) {
          return "Set up your production line";
        }

        if ("PRODUCTION" === this.phase) {
          return "Affect your resources";
        }
      }
      return null;
    }
  },

  mounted() {
    [`/app/game/${this.game}`, `/user/game/${this.game}`].forEach(item => {
      this.subs.push(this.$stomp.subscribe(item, this.receive));
    });
  },

  unmounted() {
    this.subs.forEach(s => s.unsubscribe());
  }
};
</script>
