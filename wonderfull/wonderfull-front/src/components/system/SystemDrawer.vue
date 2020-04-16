<template>
<v-navigation-drawer app right mini-variant expand-on-hover>

  <v-list dense nav class="py-0">
    <v-list-item two-line class="px-0">
      <v-list-item-avatar>
        <v-fade-transition mode="out-in">
          <template v-if="connected">
              <v-icon key="connected">mdi-access-point-network</v-icon>
            </template>
          <template v-else>
                <v-icon key="disconnected">mdi-access-point-network-off</v-icon>
            </template>
        </v-fade-transition>
      </v-list-item-avatar>

      <v-list-item-content>
        <v-list-item-title>IAWW</v-list-item-title>
        <v-list-item-subtitle>
          <fade-text :text="status" />
        </v-list-item-subtitle>
      </v-list-item-content>
    </v-list-item>

    <v-list-item link to="/lobby" v-if="user">
      <v-list-item-icon>
        <v-icon>mdi-folder-search</v-icon>
      </v-list-item-icon>

      <v-list-item-content>
        <v-list-item-title>Lobby</v-list-item-title>
      </v-list-item-content>
    </v-list-item>

    <v-list-item v-if="user">
      <v-list-item-icon>
      </v-list-item-icon>

      <v-list-item-content>
        <v-list-item-title>
          <v-btn outlined @click="logout">Logout</v-btn>
        </v-list-item-title>
      </v-list-item-content>
    </v-list-item>



  </v-list>
</v-navigation-drawer>
</template>

<script>
import StompService from '@/services/StompService';

export default {
  data() {
    return {
      miniVariant: false,
      connected: false,
      connectedURL: StompService.brokerURL
    };
  },

  computed: {
    status() {
      return this.connected ? 'Connected as ' + this.user : 'Not connected';
    },
    user() {
      return this.$store.getters['user/get'];
    }
  },

  methods: {
    logout() {
      this.$store.dispatch('user/logout');
      this.$router.push('/login');
    }
  },

  mounted() {
    StompService.addListener(s => {
      this.connected = s.status;
    });
  }
};
</script>
