package io.reservationservice.api.infrastructure.clients.redisservice;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

import io.reservationservice.api.business.dto.inport.CacheCommand;
import io.reservationservice.api.business.dto.inport.EvictCacheCommand;
import io.reservationservice.api.business.dto.outport.CacheInfo;
import io.reservationservice.api.business.dto.outport.EvictCacheInfo;
import io.reservationservice.api.infrastructure.clients.redisservice.dto.CacheDomainServiceRequest;
import io.reservationservice.api.infrastructure.clients.redisservice.dto.CacheDomainServiceResponse;
import io.reservationservice.api.infrastructure.clients.redisservice.dto.EvictCacheDomainServiceRequest;
import io.reservationservice.api.infrastructure.clients.redisservice.dto.EvictCacheDomainServiceResponse;
import io.reservationservice.api.infrastructure.clients.validator.FeignResponseValidator;
import io.reservationservice.common.annotation.FeignAdapter;
import io.reservationservice.api.business.client.RedisClientAdapter;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/25
 */
@FeignAdapter
@RequiredArgsConstructor
public class RedisClientFeignAdapter implements RedisClientAdapter {
	private final RedisServiceClient redisServiceClient;
	private final FeignResponseValidator responseValidator;



	@Override
	public CacheInfo cache(CacheCommand command) {
		ResponseEntity<CacheDomainServiceResponse> responseEntity = redisServiceClient.cache(CacheDomainServiceRequest.from(command));
		responseValidator.validate(responseEntity);
		return Optional.ofNullable(responseEntity.getBody()).map(CacheDomainServiceResponse::toCacheInfo).orElseThrow();
	}

	@Override
	public Optional<CacheInfo> getCache(String cacheKey) {
		ResponseEntity<CacheDomainServiceResponse> responseEntity = redisServiceClient.getCache(cacheKey);
		responseValidator.validate(responseEntity);
		return Optional.ofNullable(responseEntity.getBody()).map(CacheDomainServiceResponse::toCacheInfo);
	}

	@Override
	public EvictCacheInfo evictCache(EvictCacheCommand command) {
		ResponseEntity<EvictCacheDomainServiceResponse> responseEntity = redisServiceClient.evictCache(EvictCacheDomainServiceRequest.from(command));
		responseValidator.validate(responseEntity);
		return Optional.ofNullable(responseEntity.getBody()).map(EvictCacheDomainServiceResponse::toEvictCacheInfo).orElseThrow();
	}

}
