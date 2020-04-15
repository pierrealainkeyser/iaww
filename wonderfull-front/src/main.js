import Vue from 'vue'
import IAWW from '@/IAWW.vue'
import vuetify from '@/plugins/vuetify'
import '@/plugins/components'
import router from '@/plugins/router'
import store from '@/plugins/vuex'
import '@/plugins/vuex'
import 'roboto-fontface/css/roboto/roboto-fontface.css'
import '@mdi/font/css/materialdesignicons.css'

Vue.config.productionTip = false

new Vue({
  router,
  vuetify,
  store,
  render: h => h(IAWW)
}).$mount('#app')
