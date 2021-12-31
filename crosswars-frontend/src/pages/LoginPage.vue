<template>
  <div>
    <login-button />
  </div>
</template>


<script lang="ts">
import LoginButton from 'components/LoginButton.vue'
import { defineComponent } from 'vue'
export default defineComponent({
  name: 'LoginPage',
  components: {
    LoginButton
  },
});
</script>
<style scoped>
</style>
