<template>
<v-slide-y-transition group tag="div">
  <p v-for="(event,i) in viewedEvents" :key="i" class="mb-1">
    <span class="font-weight-bold" :class="event.player.color+'--text'">
      {{event.player.name}}
    </span>
    <span v-if="'pass'===event.type">
      has passed
    </span>
    <span v-if="'recycle'===event.type">
      has recycled {{event.quantity}} raw material
    </span>
    <span v-if="'draft'===event.type">
       has drafted
    <SimpleCardView :card="event.card" />
    </span>
    <span v-if="'move'===event.type">has moved
    <SimpleCardView :card="event.card" /> to the production line
    </span>
    <span v-if="'recycle_card'===event.type">
        has recycled
    <SimpleCardView :card="event.card" />
    <span v-if="event.krystaliumDelta">
          and gained <Token v-for="n in event.krystaliumDelta" type="KRYSTALIUM" :key="n+'ks'"/>
      </span>
    </span>
    <span v-if="'affect'===event.type">
      has
    <template v-if="event.recycled"> recycled    <SimpleCardView :card="event.recycled" /> and </template>
    affected
    <span class="tokens">
      <Token v-for="(token,n) in event.tokens" :type="token" :key="n" />
    </span> to  
    <SimpleCardView :card="event.card" />
    <template v-if="event.builded">
          thus build it
      </template>
    </span>
    <span v-if="'supremacy'===event.type">
        has won the supremacy and gained  <Token :type="event.gain"/>
    </span>
  </p>
</v-slide-y-transition>
</template>

<script>
export default {
  props: {
    events: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      viewed: 15
    };
  },
  computed: {
    viewedEvents() {
      const events = [...this.events];
      while (events.length > this.viewed)
        events.pop();
      return events;
    }
  }
}
</script>
