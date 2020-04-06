package org.xuenan.itook.core.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.xuenan.itook.core.utils.ListUtils;
import org.xuenan.itook.core.web.interceptor.LoggerInterceptor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Configuration
@EnableConfigurationProperties({WebProperties.class})
public class WebMvcConfig {
    private static final Logger log = LoggerFactory.getLogger(WebMvcConfig.class);
    @Autowired
    private WebProperties corsProperties;
    @Autowired
    private LoggerInterceptor loggerInterceptor;

    public WebMvcConfig() {
        log.debug("WebMvcConfig 开始初始化....");
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.loggerInterceptor).addPathPatterns(new String[]{"/**"});
    }

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addStatusController("/health", HttpStatus.OK);
    }

    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    }

    @Bean
    public FilterRegistrationBean corsFilter() {
        Map<String, CorsConfiguration> corsConfigurations = new HashMap();
        ListUtils.n(this.corsProperties.getCors()).each((cors) -> {
            corsConfigurations.put(cors.getPathPattern(), this.corsProperties(cors));
        });
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.setAlwaysUseFullPath(true);
        configSource.setCorsConfigurations(corsConfigurations);
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean(new CorsFilter(configSource), new ServletRegistrationBean[0]);
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns(new String[]{"/*"});
        return filterRegistration;
    }

    private CorsConfiguration corsProperties(WebProperties.Cors cors) {
        CorsConfiguration corsConfiguration = this.corsConfiguration();
        corsConfiguration.setAllowCredentials(cors.getAllowCredentials());
        corsConfiguration.setAllowedOrigins(cors.getAllowedOrigins());
        corsConfiguration.setAllowedHeaders(cors.getAllowedHeaders());
        corsConfiguration.setAllowedMethods(ListUtils.n(cors.getAllowedMethods()).list(Enum::name).to());
        corsConfiguration.setExposedHeaders(cors.getExposedHeaders());
        corsConfiguration.setMaxAge(cors.getMaxAge());
        return corsConfiguration;
    }

    private CorsConfiguration corsConfiguration() {
        return new CorsConfiguration() {
            public String checkOrigin(String requestOrigin) {
                String origin = super.checkOrigin(requestOrigin);
                if (origin != null) {
                    return requestOrigin;
                } else {
                    List<String> origins = this.getAllowedOrigins();
                    Iterator var4 = origins.iterator();

                    String pattern;
                    do {
                        if (!var4.hasNext()) {
                            return null;
                        }

                        pattern = (String)var4.next();
                    } while(!requestOrigin.contains(pattern) && !pattern.matches(requestOrigin));

                    return requestOrigin;
                }
            }
        };
    }
}
