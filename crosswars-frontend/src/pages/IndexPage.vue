<template>
<div class="q-pa-md">
    <div>
      <q-carousel
        v-model="slide"
        transition-prev="scale"
        transition-next="scale"
        swipeable
        animated
        control-text-color="blue-grey-8"
        navigation
        padding
        arrows
        height="300px"
        class="bg-secondary text-blue-grey-8 shadow-1 rounded-borders"
      >
        <q-carousel-slide name="style" class="column no-wrap flex-center" >
          <q-icon name="timer" size="56px" />
          <div class="q-mt-md text-center" >
            Submit your mini crossword time each day
          </div>
        </q-carousel-slide>
        <q-carousel-slide name="tv" class="column no-wrap flex-center">
          <q-icon name="query_stats" size="56px" />
          <div class="q-mt-md text-center">
            View stats about your submitted times
          </div>
        </q-carousel-slide>
        <q-carousel-slide name="layers" class="column no-wrap flex-center">
          <q-icon name="groups" size="56px" />
          <div class="q-mt-md text-center">
           Compete with friends in a group <br/> Wins are recorded daily
          </div>
        </q-carousel-slide>
      </q-carousel>
    </div>
    <login-button />
  </div>
</template>


<script lang="ts">
import LoginButton from 'components/LoginButton.vue'
import { defineComponent, ref } from 'vue'
export default defineComponent({
  name: 'IndexPage',
  components: {
    LoginButton
  },
  setup() {
    return {
      slide: ref('style'),
    }
  }
});
</script>
<style scoped>
</style>
