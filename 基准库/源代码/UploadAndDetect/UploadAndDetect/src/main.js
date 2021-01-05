// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import App from './App'
import router from './router'
import common from './components/common'

var axios = require('axios')
Vue.config.productionTip = false
Vue.use(ElementUI)
// 引用axios 并设置基础URL
// axios.default.baseURL = 'https://localhost:8088'
// 将API方法绑定到全局
Vue.prototype.$axios = axios
Vue.prototype.common = common //挂载到Vue实例上




/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
});