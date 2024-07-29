package io.redisservice.api.interfaces.controller;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.redisservice.api.application.dto.request.CacheRequest;
import io.redisservice.api.application.dto.response.CacheResponse;
import io.redisservice.api.application.dto.response.EvictCacheResponse;
import io.redisservice.api.application.facade.RedisCacheFacade;
import io.redisservice.common.model.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/23
 */
@RestController
@RequestMapping("/api/redis-cache")
@RequiredArgsConstructor
public class RedisCacheController {

	private final RedisCacheFacade redisCacheFacade;

	@PostMapping("/cache")
	@Operation(summary = "Redis Cache API")
	public CommonApiResponse<CacheResponse> cache(@RequestBody @Validated CacheRequest request) {
		return CommonApiResponse.OK(redisCacheFacade.cache(request));
	}

	@GetMapping(value = "/cache/{cacheKey}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get Cache API")
	public CommonApiResponse<Object> getCache(@PathVariable String cacheKey) {
		return CommonApiResponse.OK(redisCacheFacade.getCache(cacheKey));
	}

	@DeleteMapping("/cache/{cacheKey}")
	@Operation(summary = "Evict Cache API")
	public CommonApiResponse<EvictCacheResponse> evictCache(@PathVariable String cacheKey) {
		return CommonApiResponse.OK(redisCacheFacade.evictCache(cacheKey));
	}
}