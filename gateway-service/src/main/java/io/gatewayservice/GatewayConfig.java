package io.gatewayservice;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Rene Choi
 * @since : 2024/07/03
 */

@Configuration
public class GatewayConfig {

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
			.route("queue-management-service", r -> r.path("/queue-management/**")
				.filters(this::commonFilters)
				.uri("lb://QUEUE-MANAGEMENT-SERVICE"))
			.route("api-orchestration-service", r -> r.path("/api-orchestration/**")
				.filters(this::commonFilters)
				.uri("lb://API-ORCHESTRATION-SERVICE"))
			.route("reservation-service", r -> r.path("/reservation/**")
				.filters(this::commonFilters)
				.uri("lb://RESERVATION-SERVICE"))
			.route("payment-service", r -> r.path("/payment/**")
				.filters(this::commonFilters)
				.uri("lb://PAYMENT-SERVICE"))
			.route("message-service", r -> r.path("/message-service/**")
				.filters(this::commonFilters)
				.uri("lb://MESSAGE-SERVICE"))
			.route("redis-service", r -> r.path("/redis-service/**")
				.filters(this::commonFilters)
				.uri("lb://REDIS_SERVICE"))
			.build();
	}

	private GatewayFilterSpec commonFilters(GatewayFilterSpec filterSpec) {
		return filterSpec.rewritePath("/(?<segment>.*)", "/${segment}");
	}
}