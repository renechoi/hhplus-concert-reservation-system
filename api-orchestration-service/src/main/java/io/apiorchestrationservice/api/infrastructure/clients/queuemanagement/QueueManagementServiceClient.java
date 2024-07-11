package io.apiorchestrationservice.api.infrastructure.clients.queuemanagement;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import io.apiorchestrationservice.api.infrastructure.clients.queuemanagement.dto.ProcessingQueueTokenDomainServiceResponse;
import io.apiorchestrationservice.api.infrastructure.clients.queuemanagement.dto.WaitingQueueTokenDomainServiceResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@FeignClient(
	name = "queue-management-service",
	url= "${custom.feign.direct.host:}",
	path = "/queue-management"
)
public interface QueueManagementServiceClient {

	@GetMapping("/api/waiting-queue-token/{userId}")
	ResponseEntity<WaitingQueueTokenDomainServiceResponse> retrieveWaitingQueueToken(@PathVariable("userId") String userId);

	@GetMapping("/api/processing-queue-token/check-availability/{userId}")
	ResponseEntity<ProcessingQueueTokenDomainServiceResponse> checkProcessingQueueTokenAvailabilityWithUserId(
		@RequestHeader("X-Queue-Token") String tokenValue,
		@PathVariable("userId") String userId
	);

	@GetMapping("/api/processing-queue-token/check-availability")
	ResponseEntity<ProcessingQueueTokenDomainServiceResponse> checkProcessingQueueTokenAvailabilityWithoutUserId(
		@RequestHeader("X-Queue-Token") String tokenValue
	);
}