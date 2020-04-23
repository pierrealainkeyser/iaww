import Vue from 'vue'
import Vuex from 'vuex'

import user from '@/stores/user'
import users from '@/stores/users'

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    user,
    users
  }
});
