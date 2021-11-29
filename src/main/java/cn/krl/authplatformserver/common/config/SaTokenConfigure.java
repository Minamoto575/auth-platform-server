package cn.krl.authplatformserver.common.config;

import cn.dev33.satoken.interceptor.SaAnnotationInterceptor;
import cn.dev33.satoken.jwt.StpLogicJwtForStyle;
import cn.dev33.satoken.stp.StpLogic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author kuang
 * @description Sa-Token的注解拦截器
 * @date 2021/11/24 11:08
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    // 注册Sa-Token的注解拦截器，打开注解式鉴权功能
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册注解拦截器，并排除不需要注解鉴权的接口地址 (与登录拦截器无关)
        registry.addInterceptor(new SaAnnotationInterceptor()).addPathPatterns("/**");
    }

    // Sa-Token 整合 jwt (Style模式)
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForStyle();
    }
}
