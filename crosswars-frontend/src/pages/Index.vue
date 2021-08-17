<template>
  <q-page class="flex flex-center">
    <div class="q-mt-xl">
            <q-btn
              label="Login with Google"
              size="md" @click="auth('google')" >
            </q-btn>
          </div>
  </q-page>
</template>


<script>
import { Cookies } from 'quasar'
export default {
  name: 'PageIndex',
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
