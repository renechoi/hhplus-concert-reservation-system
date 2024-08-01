package io.paymentservice.api.common.cache.infrastructure.redisservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.paymentservice.api.common.cache.infrastructure.redisservice.dto.CacheDomainServiceRequest;
import io.paymentservice.api.common.cache.infrastructure.redisservice.dto.CacheDomainServiceResponse;
import io.paymentservice.api.common.cache.infrastructure.redisservice.dto.EvictCacheDomainServiceRequest;
import io.paymentservice.api.common.cache.infrastructure.redisservice.dto.EvictCacheDomainServiceResponse;

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



	@PostMapping("/api/redis-cache/cache")
	ResponseEntity<CacheDomainServiceResponse> cache(@RequestBody CacheDomainServiceRequest request);

	@GetMapping("/api/redis-cache/cache/{cacheKey}")
	ResponseEntity<CacheDomainServiceResponse> getCache(@PathVariable String cacheKey);

	@DeleteMapping("/api/redis-cache/cache")
	ResponseEntity<EvictCacheDomainServiceResponse> evictCache(@RequestBody EvictCacheDomainServiceRequest request);

}
