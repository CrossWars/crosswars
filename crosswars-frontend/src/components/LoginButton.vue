<template>
  <q-page class="flex flex-center">
    <div class="q-mt-xl">
            <q-btn
              label="Login with Google"
              size="md" @click="auth('google')" class="googleBtn">
            </q-btn>
          </div>
  </q-page>
</template>

<script lang="ts">
import { Cookies } from 'quasar'
import { defineComponent } from 'vue'
import { HelloJSLoginOptions } from 'hellojs';
export default defineComponent({
  name: 'LoginButton',
  methods: {
    auth (network: string) {
      const loginOptions: HelloJSLoginOptions = {
        scope: 'profile openid email',
        response_type: 'id_token token'
      }
      void this.$hello(network).login(loginOptions)
        .then((res) => {
          if(res.authResponse?.id_token != null) {
           Cookies.set('id_token', res.authResponse?.id_token);
          }
          void this.$router.push('Home')
        });
    }
  }
})
</script>
<style scoped>
.googleBtn {
  display: inline-block;
      background: white;
      color: rgb(43, 46, 59);
      width: 190px;
      border-radius: 5px;
      border: thin solid #888;
      box-shadow: 1px 1px 1px grey;
      white-space: nowrap;

}
</style>
