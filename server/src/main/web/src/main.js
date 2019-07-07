import Vue from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css'; // 默认主题
// import '../static/css/theme-green/index.css';       // 浅绿色主题
import './assets/css/icon.css';
import './components/common/directives';
import "babel-polyfill";
import echarts from 'echarts'

Vue.config.productionTip = false
Vue.use(ElementUI, {
    size: 'small'
});
Vue.prototype.$axios = axios;
Vue.prototype.$echarts = echarts


//使用钩子函数对路由进行权限跳转
router.beforeEach((to, from, next) => {
    const role = localStorage.getItem('ms_username');
    if (!role && to.path !== '/login') {
        next('/login');
    } else if (to.meta.permission) {
        // 如果是管理员权限则可进入，这里只是简单的模拟管理员权限而已
        role === 'admin' ? next() : next('/403');
    } else {
        next();
    }
})


new Vue({
    router,
    render: h => h(App)
}).$mount('#app')