package cn.krl.authplatformserver.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author kuang
 * @description 跨域过滤器
 * @date 2021/11/17  16:06
 */
@Configuration
@Order(-200)
@Slf4j
public class CorsFilter implements Filter {

    static final String OPTIONS = "OPTIONS";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {
        log.info("=========开始拦截请求==========");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // 允许指定域访问跨域资源
        response.setHeader("Access-Control-Allow-Origin", "*");
        log.info("允许跨域");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        // 允许所有请求方式
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT");
        response.setHeader(
            "Access-Control-Allow-Headers",
            request.getHeader("Access-Control-Request-Headers"));
        // 有效时间
        // response.setHeader("Access-Control-Max-Age", "3600");
        // 允许的header参数
        // response.setHeader("Access-Control-Allow-Headers", "x-requested-with,satoken");

        // 如果是预检请求，直接返回
        if (OPTIONS.equals(request.getMethod())) {
            log.info("=========OPTIONS预请求==========");
            response.setStatus(HttpStatus.OK.value());
            return;
        }

        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

}