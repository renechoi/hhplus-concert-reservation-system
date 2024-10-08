package io.apiorchestrationservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.apiorchestrationservice.api.interfaces.presentation.interceptor.TokenInterceptor;
import io.apiorchestrationservice.common.logtrace.filter.LoggingFilter;
import io.apiorchestrationservice.common.logtrace.filter.RequestResponseLogger;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final TokenInterceptor tokenInterceptor;

	@Autowired
	public WebConfig(@Lazy TokenInterceptor tokenInterceptor) {
		this.tokenInterceptor = tokenInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(tokenInterceptor).addPathPatterns("/api/**");
	}

	@Bean
	public FilterRegistrationBean<LoggingFilter> loggingFilter(RequestResponseLogger logger) {
		FilterRegistrationBean<LoggingFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new LoggingFilter(logger));
		registrationBean.addUrlPatterns("/*");
		registrationBean.setOrder(1);
		return registrationBean;
	}

	@Bean
	public RequestResponseLogger requestResponseLogger() {
		return new RequestResponseLogger();
	}

}