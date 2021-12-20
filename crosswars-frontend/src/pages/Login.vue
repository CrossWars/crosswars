<template>
    <q-page  class="docs-input row justify-center">
        <div class="col-xl-4 col-lg-6 col-md-6 col-sm-12 col-xs-12 q-pa-xl dark">
          <div class="text-center" style="color:white">
            <p class="q-display-1 text-weight-bolder q-pt-lg">Quasar + Hello.js</p>
            <p class="q-title text-weight-bold q-ma-none">Social Login</p>
          </div>
          <div class="q-mt-xl">
            <q-btn
              class="googleBtn" label="Login with Google"
              size="md" @click="auth('google')" >
            </q-btn>
          </div>
       </div>
    </q-page>
</template>

<script>
import { Cookies } from 'quasar'
export default {
  name: 'Login',
  methods: {
    auth (network) {
      
      this.$hello(network).login({scope: "profile openid email", response_type: "token id_token"})
        .then((res) => {
          console.log(res)
          Cookies.set('id_token', res)
          console.log(Cookies.has('id_token'))
          this.$router.push('/')
        })

    }
  }
}
</script>

<style scoped>
.dark{
  background: #1817309a;
}
.googleBtn {
  display: inline-block;
      background: white;
      color: rgb(36, 46, 102);
      width: 190px;
      border-radius: 5px;
      border: thin solid #888;
      box-shadow: 1px 1px 1px grey;
      white-space: nowrap;

}
</style>
