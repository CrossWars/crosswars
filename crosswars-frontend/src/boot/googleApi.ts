import { boot } from 'quasar/wrappers'

const api_key = '' //Enter API key here
export default boot(({ app }) => {
  app.config.globalProperties.$api_key = api_key
})

export { api_key }
