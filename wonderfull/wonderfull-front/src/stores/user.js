import StompService from '@/services/StompService';

export default {
  namespaced: true,
  state: {
    name: null,
    label: null,
    xcsrf: null
  },
  mutations: {
    set: (state, user) => {
      const v = user || {}
      state.label = v.label;
      state.name = v.name;
      state.xcsrf = v.xcsrf;
    }
  },
  actions: {
    login: ({
        commit
      },
      user
    ) => {
      StompService.connect(user.xcsrf);
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
