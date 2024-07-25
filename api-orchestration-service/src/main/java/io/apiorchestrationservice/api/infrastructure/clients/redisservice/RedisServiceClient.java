package io.apiorchestrationservice.api.infrastructure.clients.redisservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.apiorchestrationservice.api.infrastructure.clients.redisservice.dto.LockDomainServiceRequest;
import io.apiorchestrationservice.api.infrastructure.clients.redisservice.dto.LockDomainServiceResponse;
import io.apiorchestrationservice.api.infrastructure.clients.redisservice.dto.UnlockDomainServiceRequest;
import io.apiorchestrationservice.api.infrastructure.clients.redisservice.dto.UnlockDomainServiceResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/25
 */
@FeignClient(
	name = "redis-service",
	url= "${custom.feign.direct.host:}",
	path = "/redis-service"
)
public interface RedisServiceClient {

	@PostMapping("/api/redis-lock/lock")
	ResponseEntity<LockDomainServiceResponse> lock(@RequestBody LockDomainServiceRequest request);

	@PostMapping("/api/redis-lock/unlock")
	ResponseEntity<UnlockDomainServiceResponse> unlock(@RequestBody UnlockDomainServiceRequest request);
}
