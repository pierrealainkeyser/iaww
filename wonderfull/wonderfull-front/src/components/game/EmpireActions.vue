<template>
<component v-bind:is="component">
  <v-tooltip v-if="action.pass" bottom>
    <template v-slot:activator="{ on }">
        <v-btn v-on="on" @click="pass">Pass</v-btn>
      </template>
    <span>End this step</span>
  </v-tooltip>

  <v-tooltip v-if="action.convert" bottom>
    <template v-slot:activator="{ on }">
        <v-btn class="ml-1" v-on="on" @click="convert">Convert</v-btn>
      </template>
    <span>Put all the remaining to your empire card</span>
  </v-tooltip>

  <v-tooltip v-if="action.undo" bottom>
    <template v-slot:activator="{ on }">
        <v-btn class="ml-1" v-on="on" @click="undo">Undo</v-btn>
      </template>
    <span>Undo all the actions done in this step, reset to the situation add the begining of the current step</span>
  </v-tooltip>

  <v-tooltip v-if="action.dig" bottom>
    <template v-slot:activator="{ on }">
        <v-btn class="ml-1" v-on="on" @click="startDig">Dig</v-btn>
      </template>
    <span>Discard two cards, then draw one out of five new cards</span>
  </v-tooltip>
</component>
</template>

<script>
import {
  mapActions,
  mapGetters
} from 'vuex'

export default {
  props: {
    component: {
      type: String,
      required: true
    }
  },
  computed: {
    ...mapGetters({
      action: 'game/action',
    })
  },
  methods: {
    ...mapActions({
      onAction: 'game/action'
    }),

    pass() {
      this.onAction({
        parent: {
          action: 'pass'
        }
      });
    },

    startDig() {
      this.onAction({
        parent: {
          action: 'startDig'
        }
      });
    },

    undo() {
      this.onAction({
        parent: {
          action: 'undo'
        }
      });
    },

    convert() {
      this.onAction({
        parent: {
          action: 'convert'
        }
      });
    }
  }
}
</script>
