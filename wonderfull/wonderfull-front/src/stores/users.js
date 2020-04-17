import StompService from '@/services/StompService';

export default {
  namespaced: true,
  state: {
    name: null,
    uid: null,
    xcsrf: null
  },
  mutations: {
    set: (state, user) => {
      const v = user || {}
      state.name = v.name;
      state.uid = v.uid;
      state.xcsrf = v.xcsrf;
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
      return state.name;
    }
  }
}
