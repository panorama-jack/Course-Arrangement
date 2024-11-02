package com.chen.coursearrangement.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    // 注册 Sa-Token 拦截器，打开注解式鉴权功能
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册路由拦截器，自定义认证规则
        registry.addInterceptor(new SaInterceptor(handler -> {

            // 登录校验 -- 拦截所有路由，并排除/user/login 用于开放登录
            SaRouter.match("/**", "/user/login", r -> StpUtil.checkLogin());

            // 角色校验 -- 拦截以 classroom开头的路由，必须具备 ROLE_ADMINISTRATOR 角色
            SaRouter.match("/classroom/**", r -> StpUtil.checkRole("ROLE_ADMINISTRATOR"));
            SaRouter.match("/echarts/**", r -> StpUtil.checkRole("ROLE_ADMINISTRATOR"));
            SaRouter.match("/menu/**", r -> StpUtil.checkRole("ROLE_ADMINISTRATOR"));
            SaRouter.match("/role/**", r -> StpUtil.checkRole("ROLE_ADMINISTRATOR"));
            SaRouter.match("/role-menu/**", r -> StpUtil.checkRole("ROLE_ADMINISTRATOR"));


            SaRouter.match("/classInfo/**", r -> StpUtil.checkRoleOr("ROLE_ADMINISTRATOR","ROLE_ADMIN"));
            SaRouter.match("/courseInfo/**", r -> StpUtil.checkRoleOr("ROLE_ADMINISTRATOR","ROLE_ADMIN"));
            SaRouter.match("/coursePlan/**", r -> StpUtil.checkRoleOr("ROLE_ADMINISTRATOR","ROLE_ADMIN"));

            // 甚至你可以随意的写一个打印语句
            SaRouter.match("/**", r -> System.out.println("----啦啦啦----"));

            // 连缀写法
            SaRouter.match("/**").check(r -> System.out.println("----啦啦啦----"));

        })).addPathPatterns("/**");
    }
}
