<template>
<v-menu transition="slide-x-transition" max-width="350">
  <template v-slot:activator="{ on }">
    <v-btn v-on="on" small class="ma-1" :color="btnColor">
      <Token :type="card.def.type" alt :size="20" />
      <span class="ml-1 font-weight-bold" :class="typeColor" v-if="card.slots">
          <fade-text :text="card.slots.filled"/> / {{card.slots.total}}
      </span>
      <span class="text-capitalize ml-1">{{card.def.label}}</span>
    </v-btn>
  </template>
  <v-card>
    <v-card-title :class="headerColor+ ' subtitle-1 pt-1 pb-1'">
      <Token :type="card.def.type" alt :size="25" />
      <span class="text-capitalize ml-1 subtitle-2">{{card.def.label}}</span>
    </v-card-title>
    <v-card-text>
      <CardStats :def="card.def" :slots="card.slots" />
    </v-card-text>
    <v-expand-transition>
      <template v-if="hasAction">
          <div>
            <v-divider :class="headerColor"/>
            <v-card-actions>
              <v-btn v-for="(act,i) in actions" :key="i+'action'" small outlined @click="doAct(act)">{{formatAction(act)}}</v-btn>
            </v-card-actions>
          </div>
        </template>
    </v-expand-transition>
  </v-card>
</v-menu>
</template>

<script>
import {
  typeColor
} from './colors'

import {
  mapGetters
} from 'vuex'

export default {
  props: {
    card: {
      type: Object,
      required: true
    }
  },
  computed: {

    ...mapGetters({
      action: 'game/action'
    }),

    actions() {

      if (this.srcInteraction) {
        return [{
          action: 'cancel'
        }];
      } else if (this.targetInteraction) {
        return [{
          action: 'choose'
        }];
      }

      return this.card.actions;
    },
    srcInteraction() {
      const recycleToProduction = this.action.recycleToProduction;
      return recycleToProduction.active && this.card.id === recycleToProduction.src;
    },
    targetInteraction() {
      const recycleToProduction = this.action.recycleToProduction;
      return recycleToProduction.active && recycleToProduction.targets.includes(this.card.id)
    },
    hasAction() {
      return this.srcInteraction || this.targetInteraction || (this.action.ready && this.card.actions && this.card.actions.length);
    },
    headerColor() {
      return typeColor(this.card.def.type);
    },
    typeColor() {
      return this.headerColor + "--text";
    },
    btnColor() {
      if (this.srcInteraction) {
        return "red";
      } else if (this.targetInteraction) {
        return "primary";
      }
      return null;
    }
  },
  methods: {
    formatAction(act) {
      const action = act.action;
      if ('recycleToProduction' === action) {
        return 'recycle to production'
      }

      if ('moveToProduction' === action) {
        return 'build'
      }

      if ('recycleDraft' === action || 'recycleProduction' === action) {
        return 'recycle'
      }

      return action;
    },
    doAct(action) {
      this.$emit('action', {
        action: {
          targetId: this.card.id
        },
        parent: action
      });
    }
  }
}
</script>
