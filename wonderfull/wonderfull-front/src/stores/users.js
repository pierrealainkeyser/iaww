import StompService from '@/services/StompService';

export default {
  namespaced: true,
  state: {
    name: null,
    label: null
  },
  mutations: {
    set: (state, user) => {
      const v = user || {}
      state.label = v.label;
      state.name = v.name;
    }
  },
  actions: {
    login: ({
        commit
      },
      user
    ) => {
      StompService.csrf = user.xcsrf;
      StompService.connect();
      commit('set', user);
    },
    logout: ({
      commit
    }) => {
      StompService.deactivate();
      commit('set', null);
    }
  },
  getters: {
    get: state => {
      return state.label;
    },
    uid: state => {
      return state.name;
    }
  }
}
