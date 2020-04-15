import StompService from '@/services/StompService';

export default {
  namespaced: true,
  state: {
    user: null
  },
  mutations: {
    set: (state, user) => {
      state.user = user;
    }
  },
  actions: {
    login: ({
        commit
      },
      user
    ) => {
      StompService.user = user;
      StompService.connect();
      commit('set', user);
    },
    logout: ({
      commit
    }) => {
      StompService.user = null;
      StompService.deactivate();
      commit('set', null);
    }
  },
  getters: {
    get: state => {
      return state.user;
    }
  }
}
