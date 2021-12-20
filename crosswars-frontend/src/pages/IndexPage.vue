<template>
    <login-button />
</template>


<script lang="ts">
import LoginButton from 'components/LoginButton.vue'
import { defineComponent } from 'vue'
export default defineComponent({
  name: 'IndexPage',
  components: {
    LoginButton
  },
});
</script>
<style scoped>
</style>
