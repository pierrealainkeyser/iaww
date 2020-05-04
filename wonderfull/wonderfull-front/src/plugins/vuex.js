import Vue from 'vue'
import Vuex from 'vuex'

import user from '@/stores/user'
import users from '@/stores/users'
import game from '@/stores/game'

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    user,
    users,
    game
  }
});
