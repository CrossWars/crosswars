import { boot } from 'quasar/wrappers'
import axios, { AxiosInstance, AxiosStatic } from 'axios'

// Be careful when using SSR for cross-request state pollution
// due to creating a Singleton instance here;
// If any client changes this (global) instance, it might be a
// good idea to move this instance creation inside of the
// "export default () => {}" function below (which runs individually
// for each client)

//TODO: Find a way to automatically switch these URLS (could use docker env variable eventually)
//For production:
const api = axios.create({ baseURL: 'https://crosswars.xyz/crosswars/api' })
//For testing in browser:
//const api = axios.create({ baseURL: 'http://localhost:8080/crosswars/api' })
//For testing on phone over Wifi:
//const api = axios.create({ baseURL: 'http://192.168.1.79:8080/crosswars/api' })
//const api = axios.create({ baseURL: 'http://192.168.86.90:8080/crosswars/api' })


declare module '@vue/runtime-core' {
  interface ComponentCustomProperties {
    $axios: AxiosStatic;
    $api: AxiosInstance;
  }
}
export default boot(({ app }) => {
  // for use inside Vue files (Options API) through this.$axios and this.$api

  app.config.globalProperties.$axios = axios
  // ^ ^ ^ this will allow you to                       use this.$axios (for Vue Options API form)
  //       so you won't necessarily have to import axios in each vue file

  app.config.globalProperties.$api = api
  // ^ ^ ^ this will allow you to use this.$api (for Vue Options API form)
  //       so you can easily perform requests against your app's API
})

export { api }
