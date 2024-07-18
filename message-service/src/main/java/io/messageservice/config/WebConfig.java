package io.messageservice.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.messageservice.common.logtrace.filter.LoggingFilter;
import io.messageservice.common.logtrace.filter.RequestResponseLogger;

/**
 * @author : Rene Choi
 * @since : 2024/07/17
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
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
