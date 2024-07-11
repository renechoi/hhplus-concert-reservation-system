package io.reservationservice.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * @author : Rene Choi
 * @since : 2024/07/05
 */
@Configuration
public class SwaggerConfig {

	@Bean
	public GroupedOpenApi publicApi() {
		return GroupedOpenApi.builder()
			.group("reservation-domain-service")
			.packagesToScan("io.reservationservice")
			.pathsToMatch("/**")
			.build();
	}

	@Bean
	public OpenAPI customOpenAPI() {


		return new OpenAPI()
			.info(new Info()
				.title("reservation-domain-service API 문서")
				.description("hhplus-concert-reservation reservaion-domain-service REST API")
				.version("v1"));
	}
}