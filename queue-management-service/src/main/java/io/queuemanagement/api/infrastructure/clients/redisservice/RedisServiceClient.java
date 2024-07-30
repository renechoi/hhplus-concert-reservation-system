package io.queuemanagement.api.infrastructure.clients.redisservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.queuemanagement.api.infrastructure.clients.redisservice.dto.CacheDomainServiceRequest;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.CacheDomainServiceResponse;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.CounterDomainServiceRequest;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.CounterDomainServiceResponse;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.EvictCacheDomainServiceRequest;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.EvictCacheDomainServiceResponse;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.MemberRankDomainServiceResponse;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.QueueDomainServiceRequest;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.QueueDomainServiceResponse;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.QueueSizeDomainServiceResponse;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.TopNMembersDomainServiceResponse;

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

	@PostMapping("/api/redis-queue/enqueue")
	ResponseEntity<QueueDomainServiceResponse> enqueue(@RequestBody QueueDomainServiceRequest request);

	@PostMapping("/api/redis-queue/dequeue")
	ResponseEntity<QueueDomainServiceResponse> dequeue(@RequestBody QueueDomainServiceRequest request);


	@GetMapping("/api/redis-queue/rank/{queueName}/{member}")
	ResponseEntity<MemberRankDomainServiceResponse> getMemberRank(@PathVariable("queueName") String queueName, @PathVariable("member") String member);

	@GetMapping("/api/redis-queue/size/{queueName}")
	ResponseEntity<QueueSizeDomainServiceResponse> getQueueSize(@PathVariable String queueName);

	@GetMapping("/api/redis-queue/top/{queueName}/{n}")
	ResponseEntity<TopNMembersDomainServiceResponse> getTopNMembers(@PathVariable("queueName") String queueName, @PathVariable("n") int n);


	@PostMapping("/api/redis-counter/increment")
	ResponseEntity<CounterDomainServiceResponse> increment(@RequestBody CounterDomainServiceRequest request);

	@PostMapping("/api/redis-counter/decrement")
	ResponseEntity<CounterDomainServiceResponse> decrement(@RequestBody CounterDomainServiceRequest request);


	@GetMapping("/api/redis-counter/{counterKey}")
	ResponseEntity<CounterDomainServiceResponse> getCounter(@PathVariable("counterKey") String counterKey);





	@PostMapping("/api/redis-cache/cache")
	ResponseEntity<CacheDomainServiceResponse> cache(@RequestBody CacheDomainServiceRequest request);

	@GetMapping("/api/redis-cache/cache/{cacheKey}")
	ResponseEntity<CacheDomainServiceResponse> getCache(@PathVariable String cacheKey);

	@DeleteMapping("/api/redis-cache/cache")
	ResponseEntity<EvictCacheDomainServiceResponse> evictCache(@RequestBody EvictCacheDomainServiceRequest request);



	@DeleteMapping("/api/redis/clear")
	ResponseEntity<Void> clearAllData();


}
