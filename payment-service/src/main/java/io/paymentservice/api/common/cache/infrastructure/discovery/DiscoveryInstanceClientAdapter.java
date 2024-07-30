package io.paymentservice.api.common.cache.infrastructure.discovery;

import static io.paymentservice.util.YmlLoader.*;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */

@Component
@RequiredArgsConstructor
public class DiscoveryInstanceClientAdapter {
	private final DiscoveryClient discoveryClient;
	private final RestTemplate restTemplate;

	public void getRequestToAllInstances(String commandPath) {
		discoveryClient.getInstances(getConfigMap("spring.application.name"))
			.forEach(serviceInstance -> restTemplate.getForEntity(parseUri(commandPath, serviceInstance), Void.class));
	}

	private String parseUri(String commandPath, ServiceInstance serviceInstance) {
		return serviceInstance.getUri().toString() + "/" + getConfigMap("server.servlet.context-path") + commandPath;
	}
}
