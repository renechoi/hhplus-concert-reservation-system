package io.apiorchestrationservice.config;

import java.util.Collections;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * @author : Rene Choi
 * @since : 2024/07/05
 */
@Configuration
public class SwaggerConfig {

	@Bean
	public GroupedOpenApi publicApi() {
		return GroupedOpenApi.builder()
			.group("api-orchestration")
			.packagesToScan("io.apiorchestrationservice")
			.pathsToMatch("/**")
			.build();
	}

	@Bean
	public OpenAPI customOpenAPI() {

		SecurityScheme securityScheme = new SecurityScheme()
			.type(SecurityScheme.Type.APIKEY)
			.in(SecurityScheme.In.HEADER)
			.name("X-Queue-Token");

		SecurityRequirement securityRequirement = new SecurityRequirement().addList("X-Queue-Token");

		return new OpenAPI()
			.components(new Components().addSecuritySchemes("QueueToken", securityScheme))
			.security(Collections.singletonList(securityRequirement))
			.info(new Info()
				.title("API 오케스트레이션 API 문서")
				.description("hhplus-concert-reservation Api-Orchestration REST API")
				.version("v1"));
	}
}