package hk.ljx.fishoj.common.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * 显式把请求/响应编码固定为 UTF-8。
 *
 * Spring Boot 的 server.servlet.encoding 在 Windows + 默认 GBK 环境下经常被 Tomcat 忽略，
 * Jackson 会拿到 GBK 的字节流去解码 JSON，导致中文字段报 "Invalid UTF-8 start byte"。
 * 这里加一个高优先级的 CharacterEncodingFilter，强制覆盖 request/response 的 charset。
 */
@Configuration
public class EncodingConfig {

    @Bean("fishOjCharacterEncodingFilter")
    public FilterRegistrationBean<CharacterEncodingFilter> characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        FilterRegistrationBean<CharacterEncodingFilter> reg = new FilterRegistrationBean<>(filter);
        reg.addUrlPatterns("/*");
        reg.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return reg;
    }
}