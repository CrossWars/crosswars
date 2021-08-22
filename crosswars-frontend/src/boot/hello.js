import hello from 'hellojs'
import { boot } from 'quasar/wrappers'

hello.init({
    google: '474023833184-4odp7ummj7puclc850bo80brkcj10k4j'
  })
export default boot(({ app }) => {
  app.config.globalProperties.$hello = hello
})