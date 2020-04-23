export default {
  namespaced: true,
  state: {
    users: [],
  },
  mutations: {
    add: (state, user) => {
      if (!state.users.find(u => u.name === user.name)) {
        state.users.push(user);
      }
    },
    remove: (state, user) => {
      const index = state.users.findIndex(u => u.name === user.name);
      if (index >= 0)
        state.users.splice(index, 1);
    },
    set: (state, users) => {
      state.users.splice(0, state.users.length);
      Array.prototype.push.apply(state.users, users);
    }
  },

  actions: {
    event: ({
      commit
    }, event) => {
      if ('connected' === event.type) {
        commit('add', event.user);
      } else if ('disconnected' === event.type) {
        commit('remove', event.user);
      } else if ('all' === event.type) {
        commit('set', event.users);
      }
    }
  },

  getters: {
    all: state => {
      return state.users;
    }
  }
}
