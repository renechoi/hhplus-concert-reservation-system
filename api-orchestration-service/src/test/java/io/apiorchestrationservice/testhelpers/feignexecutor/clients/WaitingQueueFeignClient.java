package io.apiorchestrationservice.testhelpers.feignexecutor.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.apiorchestrationservice.testhelpers.feignexecutor.dto.WaitingQueueTokenGenerateRequest;
import io.apiorchestrationservice.testhelpers.feignexecutor.dto.WaitingQueueTokenGenerationResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@FeignClient(name = "waitingQueueService",
	url = "http://localhost:24031/queue-management"
)
public interface WaitingQueueFeignClient {

	@PostMapping("/api/waiting-queue-token")
	WaitingQueueTokenGenerationResponse generateToken(@RequestBody WaitingQueueTokenGenerateRequest request);
}