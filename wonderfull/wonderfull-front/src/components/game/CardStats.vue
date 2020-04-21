<template>
<v-row dense>
  <v-col :cols="title">Cost</v-col>
  <v-col :cols="token" class="tokens">
    <Token v-for="(token,i) in def.cost" :key="i" :type="token" :size="size" />
  </v-col>

  <template v-if="slots">
    <v-col :cols="title">Slots</v-col>
    <v-col :cols="token" class="tokens">
      <Token v-for="(s,i) in slots.values" :key="i" :type="s.status==='KRYSTALIUM'?'KRYSTALIUM':s.type" :size="size" :disabled="s.status==='EMPTY'"/>
    </v-col>
  </template>

  <v-col :cols="title">Production</v-col>
  <v-col :cols="token">
    <Produce :produce="def.produce" :size="size" />
  </v-col>

  <v-col :cols="title">Recycle</v-col>
  <v-col :cols="token">
    <Token :type="def.recycle" :size="size" />
  </v-col>

  <v-col :cols="title">Score</v-col>
  <v-col :cols="token">
    <span class="d-inline-block">
      <Scoring :score="def.scoring" :size="size"/>
    </span>
  </v-col>

  <v-col :cols="title">Bonus</v-col>
  <v-col :cols="token" class="tokens">
    <Token v-for="(token,i) in def.bonus" :key="i" :type="token" :size="size" />
  </v-col>
</v-row>
</template>

<script>
export default {
  data() {
    return {
      title: 4,
      token: 8,
      size: 19
    }
  },
  props: {
    def: {
      type: Object,
      required: true
    },
    slots: {
      type: Object,
      required: false
    }
  }
}
</script>
