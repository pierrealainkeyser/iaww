import Vue from 'vue'
import IAWW from '@/IAWW.vue'
import vuetify from '@/plugins/vuetify'
import '@/plugins/components'
import router from '@/plugins/router'
import store from '@/plugins/vuex'
import '@/plugins/vuex'
import 'roboto-fontface/css/roboto/roboto-fontface.css'
import '@mdi/font/css/materialdesignicons.css'

import stomp from '@/services/StompService';
import axios from 'axios';

Vue.config.productionTip = false

const url = process.env.VUE_APP_BACKEND;
const axiosConfig = {
  withCredentials: true
}
if (url) {
  axiosConfig.baseURL = url;
  axiosConfig.headers = {
    "X-CSRF-TOKEN": null
  }
}

Vue.prototype.$axios = axios.create(axiosConfig);
Vue.prototype.$stomp = stomp;

new Vue({
  router,
  vuetify,
  store,
  render: h => h(IAWW)
}).$mount('#app')
