import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/',
            redirect: '/home'
        },
        {
            path: '/',
            component: resolve => require(['../components/common/Body.vue'], resolve),
            meta: {title: '自述文件'},
            children: [
                {
                    path: '/home',
                    component: resolve => require(['../components/page/Home.vue'], resolve),
                    meta: {title: '系统首页'}
                },
                {
                    path: '/monitor',
                    component: resolve => require(['../components/page/Monitor.vue'], resolve),
                    meta: {title: '监控'}
                },
                {
                    path: '/trace',
                    component: resolve => require(['../components/page/Trace.vue'], resolve),
                    meta: {title: '调用链'}
                },
                {
                    path: '/404',
                    component: resolve => require(['../components/page/404.vue'], resolve),
                    meta: {title: '404'}
                },
                {
                    path: '/403',
                    component: resolve => require(['../components/page/403.vue'], resolve),
                    meta: {title: '403'}
                }
            ]
        },
        {
            path: '/login',
            component: resolve => require(['../components/page/Login.vue'], resolve)
        },
        {
            path: '*',
            redirect: '/404'
        }
    ]
})
