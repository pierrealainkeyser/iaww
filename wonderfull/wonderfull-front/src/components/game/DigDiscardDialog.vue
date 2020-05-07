<template>
<v-dialog v-model="action.discard.active" persistent>
  <v-card>
    <v-card-title>Discard cards</v-card-title>
    <v-card-text>
      <v-checkbox v-for="(card,i) in cards" v-model="targetsIds" :key="i" :value="card.id" class="mb-1">
        <template #label>
          <SimpleCardView :card="card.def" />
        </template>
      </v-checkbox>
    </v-card-text>
    <v-card-actions>
      <v-btn outlined @click="proceed" :disabled="proceedDisabled">Proceed</v-btn>
      <v-btn text @click="cancel">Cancel</v-btn>
    </v-card-actions>
  </v-card>
</v-dialog>
</template>

<script>
import {
  mapGetters
} from 'vuex'

export default {
  data() {
    return {
      targetsIds: []
    }
  },
  computed: {

    ...mapGetters({
      action: 'game/action'
    }),
    proceedDisabled() {
      return this.targetsIds.length !== 2;
    },
    cards() {
      return this.action.discard.src;
    }
  },
  watch: {
    cards() {
      this.targetsIds.splice(0, this.targetsIds.length);
    }
  },
  methods: {
    proceed() {
      this.$emit('action', {
        parent: {
          action: 'discard'
        },
        action: {
          targetsIds: this.targetsIds
        }
      });
    },
    cancel() {
      this.$emit('action', {
        parent: {
          action: 'cancel'
        }
      });
    }
  }

}
</script>
