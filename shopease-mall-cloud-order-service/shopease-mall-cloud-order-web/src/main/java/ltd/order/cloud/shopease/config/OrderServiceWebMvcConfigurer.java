 
package ltd.order.cloud.shopease.config;

import com.alibaba.cloud.sentinel.SentinelProperties;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.SentinelWebInterceptor;
import ltd.order.cloud.shopease.config.handler.TokenToAdminUserMethodArgumentResolver;
import ltd.order.cloud.shopease.config.handler.TokenToMallUserMethodArgumentResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;
import java.util.Optional;

@Configuration
public class OrderServiceWebMvcConfigurer extends WebMvcConfigurationSupport {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceWebMvcConfigurer.class);

    @Autowired
    private SentinelProperties sentinelProperties;
    @Autowired
    private Optional<SentinelWebInterceptor> sentinelWebInterceptorOptional;

    @Autowired
    @Lazy
    private TokenToAdminUserMethodArgumentResolver tokenToAdminUserMethodArgumentResolver;

    @Autowired
    @Lazy
    private TokenToMallUserMethodArgumentResolver tokenToMallUserMethodArgumentResolver;

    /**
     * @param argumentResolvers
     * @tip @TokenToAdminUser 注解处理方法
     */
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(tokenToAdminUserMethodArgumentResolver);
        argumentResolvers.add(tokenToMallUserMethodArgumentResolver);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.
                addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);
    }

    public void addInterceptors(InterceptorRegistry registry) {
        if (this.sentinelWebInterceptorOptional.isPresent()) {
            SentinelProperties.Filter filterConfig = this.sentinelProperties.getFilter();
            registry.addInterceptor((HandlerInterceptor) this.sentinelWebInterceptorOptional.get()).order(filterConfig.getOrder()).addPathPatterns(filterConfig.getUrlPatterns());
            log.info("[Sentinel Starter] register SentinelWebInterceptor with urlPatterns: {}.", filterConfig.getUrlPatterns());
        }
    }
}
