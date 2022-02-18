<template>
  <!-- taken from https://codepen.io/stefanjs98/pen/ambVgK -->
  <q-page class="q-pa-xl">
    <link
      rel="stylesheet"
      type="text/css"
      href="//fonts.googleapis.com/css?family=Open+Sans"
    />
    <div style="margin: 0 auto" class="google-btn" @click="auth('google')">
      <div class="google-icon-wrapper">
        <img
          class="google-icon"
          src="https://upload.wikimedia.org/wikipedia/commons/5/53/Google_%22G%22_Logo.svg"
        />
      </div>
      <p class="btn-text"><b>Sign in with Google</b></p>
    </div>
  </q-page>
</template>

<script lang="ts">
import { Cookies } from 'quasar';
import { defineComponent } from 'vue';
import { HelloJSLoginOptions } from 'hellojs';
import { getUserByJWT, postNewUser } from 'src/models/Users/users.api';
import jwt_decode from 'jwt-decode';
import { User } from 'src/models/Users/users';
export default defineComponent({
  name: 'LoginButton',
  methods: {
    async auth(network: string) {
      const loginOptions: HelloJSLoginOptions = {
        scope: 'profile openid email',
        response_type: 'id_token token',
      };
      const res = await this.$hello(network).login(loginOptions);
      if (res.authResponse?.id_token != null) {
        Cookies.set('id_token', res.authResponse?.id_token);
        localStorage.setItem('jwt', res.authResponse?.id_token);
        this.getUser();
      }
    },
    async getUser() {
      let user: User;
      try {
        user = await getUserByJWT();
        localStorage.setItem('user', JSON.stringify(user));
        this.goToNextPage();
      } catch (e) {
        // create user on backend if not found
        if ((e as Error).message == 'User not found') {
          const jwt = localStorage.getItem('jwt')!;
          const decoded: any = jwt_decode(jwt);
          user = {
            name: decoded.given_name,
            id: decoded.sub,
            email: decoded.email,
          };
          user = await postNewUser();
          localStorage.setItem('user', JSON.stringify(user));
          this.goToNextPage();
        }
        else {
          throw e
        }
      }
    },
    goToNextPage() {
      if (this.$route.query.nextUrl != null) {
        this.$router.push(this.$route.query.nextUrl as string);
      } else {
        this.$router.push('Home');
      }
    },
  },
});
</script>
<style lang="scss" scoped>
$white: #fff;
$google-blue: #4285f4;
$button-active-blue: #1669f2;

.google-btn {
  width: 184px;
  height: 42px;
  background-color: $google-blue;
  border-radius: 2px;
  box-shadow: 0 3px 4px 0 rgba(0, 0, 0, 0.25);
  .google-icon-wrapper {
    position: absolute;
    margin-top: 1px;
    margin-left: 1px;
    width: 40px;
    height: 40px;
    border-radius: 2px;
    background-color: $white;
  }
  .google-icon {
    position: absolute;
    margin-top: 11px;
    margin-left: 11px;
    width: 18px;
    height: 18px;
  }
  .btn-text {
    float: right;
    margin: 11px 11px 0 0;
    color: $white;
    font-size: 14px;
    letter-spacing: 0.2px;
    font-family: "Roboto";
  }
  &:hover {
    box-shadow: 0 0 6px $google-blue;
  }
  &:active {
    background: $button-active-blue;
  }
}

@import url(https://fonts.googleapis.com/css?family=Roboto:500);
</style>
