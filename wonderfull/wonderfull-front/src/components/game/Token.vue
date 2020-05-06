<template>
<v-tooltip bottom>
  <template v-slot:activator="{ on }">
    <v-sheet v-on="on" :class="disabled?'disabled':null" :color="color">
      <v-icon v-if="type" :size="size">{{vIcon}}</v-icon>

      <v-icon v-if="negated" class="overlay" :size="size" color="red darken-2">mdi-cancel</v-icon>
    </v-sheet>
  </template>
  <span>{{tooltip}}</span>
</v-tooltip>
</template>
<script>
import {
  typeColor
} from './colors'

export default {
  props: {
    type: {
      type: String
    },
    alt: {
      type: Boolean,
      default: false
    },
    disabled: {
      type: Boolean,
      default: false
    },
    negated: {
      type: Boolean,
      default: false
    },
    size: {
      type: Number,
      default: 15
    }
  },
  computed: {
    color() {
      return typeColor(this.type);
    },
    tooltip() {
      var tooltip = null;
      if ("BUSINESSMAN" === this.type) {
        tooltip = "Financier";
      } else if ("GENERAL" === this.type) {
        tooltip = "General";
      } else if ("KRYSTALIUM" === this.type) {
        tooltip = "Krystalium";
      } else if ("RAW" === this.type) {
        tooltip = "Raw resource";
      } else if ("MATERIAL" === this.type) {
        tooltip = this.alt ? "Structure" : "Material";
      } else if ("ENERGY" === this.type) {
        tooltip = this.alt ? "Vehicle" : "Energy";
      } else if ("SCIENCE" === this.type) {
        tooltip = this.alt ? "Research" : "Science";
      } else if ("GOLD" === this.type) {
        tooltip = this.alt ? "Project" : "Gold";
      } else if ("DISCOVERY" === this.type) {
        tooltip = this.alt ? "Discovery" : "Exploration";
      } else if ("MEMORIAL" === this.type) {
        tooltip = "Memorial";
      }

      if (this.negated) {
        tooltip = "Corrupted " + tooltip.toLowerCase();
      }

      return tooltip;
    },
    vIcon() {
      if ("BUSINESSMAN" === this.type) {
        return "mdi-account-tie";
      } else if ("GENERAL" === this.type) {
        return "mdi-account-star";
      } else if ("KRYSTALIUM" === this.type) {
        return "mdi-alpha-k-box-outline";
      } else if ("RAW" === this.type) {
        return "mdi-alpha-r-box-outline";
      } else if ("MATERIAL" === this.type) {
        return this.alt ? "mdi-factory" : "mdi-cart-outline";
      } else if ("ENERGY" === this.type) {
        return this.alt ? "mdi-truck" : "mdi-radioactive";
      } else if ("SCIENCE" === this.type) {
        return this.alt ? "mdi-react" : "mdi-flask";
      } else if ("GOLD" === this.type) {
        return this.alt ? "mdi-bank" : "mdi-currency-usd";
      } else if ("DISCOVERY" === this.type) {
        return this.alt ? "mdi-treasure-chest" : "mdi-compass";
      } else if ("MEMORIAL" === this.type) {
        return "mdi-castle";
      }
      return null;
    }
  }

}
</script>

<style scoped>
.v-sheet {
  display: inline-block;
  vertical-align: middle;
  text-align: center;
  position: relative;
}

.v-sheet:empty {
  visibility: hidden;
}

.v-sheet.disabled {
  opacity: 0.4;
}

.overlay {
  position: absolute;
  left: 0px;
}
</style>
