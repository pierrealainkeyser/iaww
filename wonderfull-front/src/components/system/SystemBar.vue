<template>
<v-system-bar lights-out>

  <v-fade-transition mode="out-in">
    <template v-if="connected">
      <v-icon key="connected">mdi-access-point-network</v-icon>
    </template>
    <template v-else>
        <v-icon key="disconnected">mdi-access-point-network-off</v-icon>
    </template>
  </v-fade-transition>


  <v-tooltip bottom>
    <template v-slot:activator="{ on }">
        <span v-on="on">
          <v-fade-transition mode="out-in">
            <span key="connected" v-if="connected">
              Connected
            </span>
            <span key="disconnected" v-else>
              Disconnected
            </span>
          </v-fade-transition>
        </span>
    </template>
    <span>Remote server : {{connectedURL}}</span>
  </v-tooltip>

  <v-spacer />
  <fade-text :text="user" />
</v-system-bar>
</template>

<script>
import StompService from '@/services/StompService';

export default {
  data() {
    return {
      connected: false,
      connectedURL: StompService.brokerURL
    };
  },

  computed: {
    user() {
      return this.$store.getters['user/get'];
    }
  },

  mounted() {
    StompService.addListener(s => {
      this.connected = s.status;
    });
  }
};
</script>
