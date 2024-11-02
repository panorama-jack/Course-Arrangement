import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";

Vue.use(VueRouter)
// 解决报错 此方法只针对VueRouter3.0以上
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/Login.vue'),
        meta: {
            title: '登录'
        },
    },
    {
        path: '/404',
        name: '404',
        component: () => import('../views/404.vue')
    },

]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})


// 提供一个重置路由的方法
export const resetRouter = () => {
    router.matcher = new VueRouter({
        mode: 'history',
        base: process.env.BASE_URL,
        routes
    })
}
export const setRoutes = () => {
    const storeUser = localStorage.getItem("user");
    if (storeUser) {
        const currentRoutesNames = router.getRoutes().map(v => v.name)
        if (!currentRoutesNames.includes('Manage')) {
            // 拼装动态路由
            const manageRoute = {                                                                     //登录成功后跳转页面
                path: '/', name: 'Manage', component: () => import('../views/Manage.vue'), redirect: "/home",

                children: [
                    {path: 'person', name: '个人信息', component: () => import('../views/Person.vue')},
                    {path: 'password', name: '修改密码', component: () => import('../views/Password.vue')},
                    {path: 'notificationDetail/:id', name: '通知详情', component: () => import('../views/NotificationDetail.vue')
                    }
                ]
            }
            const menus = JSON.parse(storeUser).menus
            menus.forEach(item => {
                if (item.path) {  // 当且仅当path不为空的时候才去设置路由
                    let itemMenu = {
                        path: item.path.replace("/", ""),
                        name: item.name,
                        component: () => import('../views/' + item.pagePath + '.vue'),
                        meta: {
                            title: item.name
                        },
                    }
                    manageRoute.children.push(itemMenu)
                } else if (item.children.length) {
                    item.children.forEach(item => {
                        if (item.path) {
                            let itemMenu = {
                                path: item.path.replace("/", ""),
                                name: item.name,
                                component: () => import('../views/' + item.pagePath + '.vue'),
                                meta: {
                                    title: item.name
                                },
                            }
                            manageRoute.children.push(itemMenu)
                        }
                    })
                }
            })
            // 动态添加到现在的路由对象中去
            router.addRoute(manageRoute)
        }
    }

}

setRoutes();

// 路由守卫  页面发生跳转生效
router.beforeEach((to, from, next) => {
    localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称，为了在Header组件中去使用
    store.commit("setPath")  // 触发store的数据更新
    // 未找到路由的情况
    if (to.meta.title) {
        document.title = to.meta.title
    } else {
        document.title = '默认标题' //此处写默认的title
    }
    next()

    if (!to.matched.length) {
        const storeMenus = localStorage.getItem("menus")
        if (storeMenus) {
            next("/404")
        } else {
            // 跳回登录页面
            next("/login")
        }
    }
    // 其他的情况都放行
    next()  // 放行路由*/
})

export default router
