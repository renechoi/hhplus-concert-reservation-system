package io.clientchannelservice.api.infrastructure.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.clientchannelservice.api.infrastructure.clients.dto.WaitingQueueTokenDomainServiceResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@FeignClient(
	name = "queue-management-service",
	url= "${custom.feign.direct.host:}",
	path = "/queue-management-service",
	value = "queue-management-service"
)
public interface WaitingQueueTokenClient {

	@GetMapping("/api/waiting-queue-token/{userId}")
	ResponseEntity<WaitingQueueTokenDomainServiceResponse> retrieveWaitingQueueToken(@PathVariable("userId") String userId);
}