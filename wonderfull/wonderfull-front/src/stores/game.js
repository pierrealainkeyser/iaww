import StompService from '@/services/StompService';

const PLAYERS_COLOR = ["cyan", "light-green", "pink", "amber", "lime"];


function createEmptyEmpire(index, wop) {

  var stats = {
    material: 0,
    energy: 0,
    science: 0,
    gold: 0,
    discovery: 0,
    krystalium: 0,
    raw: 0,
    businessman: 0,
    general: 0
  };

  if (wop) {
    stats = {
      material: 0,
      energy: 0,
      science: 0,
      gold: 0,
      discovery: 0,
      deltakrystalium: 0,
      deltabusinessman: 0,
      deltageneral: 0,
      krystalium: 0,
      raw: 0,
      businessman: 0,
      general: 0
    }
  }

  return {
    score: 0,
    empire: {
      builded: [],
      inProduction: []
    },
    player: null,
    playerColor: PLAYERS_COLOR[index % PLAYERS_COLOR.length],
    inHand: [],
    stats,
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
  truncateArray(targets);
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
  target.player = src.player.label;
  target.playerId = src.player.name;

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
      color: empires[evt.player].playerColor,
      index: evt.player
    },
    type: evt.event.type
  }

  if ("draft" === out.type || "move" === out.type) {
    out.card = wrapCard(evt.event.card, dictionnary).def;
  } else if ("recycle" === out.type || "recycle_prod" === out.type) {
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

  const matchers = out.name.match(/empire\/(.*)/)

  if (matchers) {
    const name = matchers[1];
    const ename = name.match(/(.*)_.*/);
    if (ename)
      out.label = ename[1];
    else
      out.label = "empire";
  } else
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

function truncateArray(array) {
  array.splice(0, array.length);
}



function sendAction(state, name, action) {
  StompService.publish(`/app/game/${state.id}/${name}`, action);
}

export default {
  namespaced: true,
  state: {
    id: null,
    clock: -1,
    turn: 0,
    step: null,
    phase: null,
    done: false,
    myself: -1,
    winner: -1,
    action: {
      ready: true,
      pass: false,
      undo: false,
      convert: false,
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
  },
  mutations: {
    gameId: (state, game) => {
      state.id = game;
    },
    data: (state, data) => {
      const myself = data.myself;
      var wop = false;
      if (data.dictionnary != null) {
        state.myself = myself;
        truncateArray(state.dictionnary);
        Array.prototype.push.apply(state.dictionnary, data.dictionnary.map(mapDictionnary));
        wop = data.wop;
        truncateArray(state.empires);
      }

      state.done = data.terminated;
      state.clock = data.clock;
      state.turn = data.turn;
      state.phase = data.phase;
      state.step = data.step;
      state.winner = data.winner;

      const actions = (data.actions || {});
      state.action.ready = true;
      state.action.pass = actions.pass || false;
      state.action.supremacy = actions.supremacy || false;
      state.action.convert = actions.convert || false;
      state.action.undo = actions.undo || false;

      const empires = data.empires;
      const length = empires.length;
      while (state.empires.length < empires.length) {
        state.empires.push(createEmptyEmpire(state.empires.length, wop));
      }


      for (var i = 0; i < length; ++i) {
        mapEmpire(empires[i], state.empires[i], state.dictionnary, myself === i ? actions.cards : null);
      }

      const newEvents = data.events.map(event => mapEvent(event, state.empires, state.dictionnary));
      truncateArray(state.events);
      Array.prototype.push.apply(state.events, newEvents);
    },
    reset: (state) => {
      state.clock = -1;
      state.turn = 0;
      state.step = null;
      state.phase = null;
      state.done = false;
      state.wop = false;
      state.winner = 1;
      state.myself = -1;
      state.action.ready = true;
      state.action.pass = false;
      state.action.undo = false;
      state.action.convert = false;
      state.action.supremacy = false;
      state.action.recycleToProduction.active = false;
      state.action.recycleToProduction.src = null;
      truncateArray(state.action.recycleToProduction.targets);

      state.action.affectation.active = false;
      state.action.affectation.src = null;
      state.action.affectation.slots = {};

      truncateArray(state.events);
      truncateArray(state.empires);
      state.empires.push(createEmptyEmpire(0));
      truncateArray(state.dictionnary);
    },
    clearInteraction: state => {

      const recycleToProduction = state.action.recycleToProduction;
      recycleToProduction.active = false;
      recycleToProduction.src = null;
      truncateArray(recycleToProduction.targets);

      const affectation = state.action.affectation;
      affectation.active = false;
      affectation.src = null;
      affectation.slots = {};
    },
    recycleToProduction: (state, {
      id,
      targets
    }) => {
      const recycleToProduction = state.action.recycleToProduction;
      recycleToProduction.active = true;
      recycleToProduction.src = id;
      Array.prototype.push.apply(recycleToProduction.targets, targets);
    },
    affectation: (state, {
      src,
      slots
    }) => {
      const affectation = state.action.affectation;
      affectation.active = true;
      affectation.src = src;
      affectation.slots = slots;
    },
    actionNotReady: (state) => {
      state.action.ready = false;
    }
  },
  actions: {
    reset: ({
      commit
    }) => {
      commit('reset');
    },
    start: ({
      commit
    }, game) => {
      commit('reset');
      commit('gameId', game);
    },
    receive: ({
        commit,
        state
      },
      data
    ) => {
      if (state.clock < data.clock) {
        commit('data', data);
      }
    },
    action: ({
      commit,
      state
    }, event) => {
      const name = event.parent.action;
      const action = event.action;

      if ('recycleToProduction' === name) {
        commit('recycleToProduction', {
          id: action.targetId,
          targets: event.parent.targets
        });
      } else if ('choose' === name) {
        commit('actionNotReady');
        sendAction(state, 'recycleToProduction', {
          targetId: state.action.recycleToProduction.src,
          productionId: action.targetId
        });
        commit('clearInteraction');
      } else if ('affectation' === name) {
        const id = event.action.targetId;
        const found = state.empires[state.myself].empire.inProduction.find(c => c.id === id);

        commit('affectation', {
          src: found,
          slots: event.parent.slots
        });
      } else if ('proceedAffectation' === name) {
        commit('actionNotReady');
        sendAction(state, 'affectation', action);
        commit('clearInteraction');
      } else {
        if ('cancel' !== name) {
          commit('actionNotReady');
          sendAction(state, name, action);
        }
        commit('clearInteraction');
      }
    }
  },
  getters: {
    empires: state => {
      return state.empires;
    },

    myself: state => {
      return state.myself;
    },

    action: state => {
      return state.action;
    },

    clock: state => {
      return state.clock;
    },

    events: state => {
      return state.events;
    },

    loaded: state => {
      return state.clock >= 0;
    },

    turnStatus: state => {
      return {
        turn: state.turn,
        step: state.step,
        phase: state.phase,
        done: state.done
      };
    },

    status: state => {

      if (state.done) {
        var result = "Game is terminated without a winnner";
        if (state.winner >= 0) {
          const player = state.empires[state.winner].player;
          result = `${player} has won !`;
        }

        return result;
      }

      if (state.myself >= 0) {
        const my = state.empires[state.myself];
        if (my && my.done)
          return "Waiting for other players";

        if ("DRAFT" === state.phase) {
          return "Choose a card to draft";
        }

        if ("PLANNING" === state.phase) {
          return "Set up your production line";
        }

        if ("PRODUCTION" === state.phase) {
          return "Affect your resources";
        }
      }
      return null;
    }
  }
}
