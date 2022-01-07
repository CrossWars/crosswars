<template>
  <router-view :key="$route.fullPath"/>
</template>
<script>
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'App'
})
</script>
