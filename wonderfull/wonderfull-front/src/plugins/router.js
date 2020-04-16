import Vue from 'vue';
import VueRouter from 'vue-router';
import routes from '@/routes';

import store from '@/plugins/vuex'

Vue.use(VueRouter);

const router = new VueRouter({
  routes
});

router.beforeEach((to, from, next) => {
  if (to.path !== '/login' && !store.getters['user/get']) {
    next('/login');
  } else {
    next();
  }
})

export default router;
