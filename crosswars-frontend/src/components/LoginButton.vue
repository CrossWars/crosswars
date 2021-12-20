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

<script>
import { Cookies } from 'quasar'
export default {
  name: 'LoginButton',
  methods: {
    auth (network) {
      
      this.$hello(network).login({scope: "profile openid email", response_type: "token id_token"})
        .then((res) => {
          console.log(res)
          Cookies.set('id_token', res)
          console.log(Cookies.has('id_token'))
          this.$router.push('Home')
        })

    }
  }
}
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
