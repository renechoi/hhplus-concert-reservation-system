package io.paymentservice.api.common.cache.infrastructure.redisservice;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

import io.paymentservice.api.common.cache.business.client.RedisClientAdapter;
import io.paymentservice.api.common.cache.business.dto.command.CacheCommand;
import io.paymentservice.api.common.cache.business.dto.command.EvictCacheCommand;
import io.paymentservice.api.common.cache.business.dto.info.CacheInfo;
import io.paymentservice.api.common.cache.business.dto.info.EvictCacheInfo;
import io.paymentservice.api.common.cache.infrastructure.redisservice.dto.CacheDomainServiceRequest;
import io.paymentservice.api.common.cache.infrastructure.redisservice.dto.CacheDomainServiceResponse;
import io.paymentservice.api.common.cache.infrastructure.redisservice.dto.EvictCacheDomainServiceRequest;
import io.paymentservice.api.common.cache.infrastructure.redisservice.dto.EvictCacheDomainServiceResponse;
import io.paymentservice.api.common.cache.infrastructure.validator.FeignResponseValidator;
import io.paymentservice.common.annotation.FeignAdapter;
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
