<template>
<v-dialog v-model="action.affectation.active" persistent max-width="600px">
  <v-card>
    <v-card-title>Affect resources to
      <template v-if="action.affectation.src">
        <Token :type="action.affectation.src.def.type" class="ml-1" alt :size="25" />
        <span class="text-capitalize ml-1 subtitle-2">{{action.affectation.src.def.label}}</span>
      </template>
    </v-card-title>
    <v-card-text>
      <v-row dense>
        <v-col cols="4">
          Cost
        </v-col>
        <v-col>
          <v-btn-toggle v-for="(slot,i) in slots" :key="i" v-model="selection[i]" mandatory tile class="flex-column">
            <v-btn :disabled="slot.affectation.length===0">
              <Token :type="slot.type" :size="20" :disabled="slot.disabled" />
            </v-btn>

            <v-btn v-for="(s,j) in slot.affectation" :disabled="hasNoneLeft(s)" :key="j">
              <Token :type="s" :size="20" />
            </v-btn>
          </v-btn-toggle>
        </v-col>
      </v-row>
      <v-row dense>
        <v-col cols="4">
          Resources
        </v-col>
        <v-col>
          <AvailableTokens class="ma-1" :tokens="available" :size="30" />
        </v-col>
      </v-row>
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
  props: {
    available: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      selection: []
    }
  },
  watch: {
    slots(newValue) {
      this.selection.splice(0, this.selection.length);
      newValue.forEach(() => {
        this.selection.push(0);
      });
    }
  },
  computed: {

    ...mapGetters({
      action: 'game/action'
    }),

    proceedDisabled() {
      return this.selection.filter(selected => selected > 0).length === 0;
    },
    consumed() {
      const consumed = {};
      this.selection.forEach((selected, index) => {
        if (selected > 0 && this.slots.length > index) {
          const affect = this.slots[index].affectation[selected - 1];
          if (affect) {
            const type = affect.toLowerCase();
            const oldValue = consumed[type] || 0;
            consumed[type] = oldValue + 1;
          }
        }
      });

      return consumed;
    },
    slots() {
      const card = this.action.affectation.src;
      if (card) {
        const affectableSlots = this.action.affectation.slots;

        return card.slots.values.map((slot, i) => {
          const status = affectableSlots[i] || [];
          return {
            type: slot.type,
            disabled: slot.status === 'EMPTY',
            affectation: status
          }
        });
      }
      return [];

    }
  },
  methods: {
    hasNoneLeft(token) {
      const key = token.toLowerCase();
      return this.available[key] <= (this.consumed[key] || 0);
    },
    proceed() {
      const card = this.action.affectation.src;
      const slots = {};

      this.selection.forEach((selected, index) => {
        if (selected > 0) {
          slots[index] = 1 === selected ? this.slots[index].type : "KRYSTALIUM";
        }
      });


      this.$emit('action', {
        parent: {
          action: 'proceedAffectation'
        },
        action: {
          targetId: card.id,
          slots
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
