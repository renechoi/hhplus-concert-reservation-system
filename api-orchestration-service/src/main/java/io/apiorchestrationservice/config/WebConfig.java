package io.apiorchestrationservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.apiorchestrationservice.api.presentation.interceptor.TokenInterceptor;

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
}