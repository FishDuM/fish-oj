package hk.ljx.fishoj.common.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        // 注意: Sa-Token 拦截器看的是 request.getRequestURI(),
                        // 该值已包含 context-path (/api), 所以排除路径不能写 /api 前缀
                        "/user/register",
                        "/user/login",
                        "/tag/list"
                );
    }
}