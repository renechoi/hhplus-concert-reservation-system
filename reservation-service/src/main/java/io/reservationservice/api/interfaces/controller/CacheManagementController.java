package io.reservationservice.api.interfaces.controller;

/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */

import io.reservationservice.api.business.service.impl.LocalCacheEvictionService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cache")
@Tag(name = "Cache Management Controller")
public class CacheManagementController {

	private final LocalCacheEvictionService localCacheEvictionService;

	@Schema(description = "Clear all caches on this instance")
	@GetMapping("/clear-all")
	public void clearAllCaches() {
		localCacheEvictionService.clearAllCaches();
	}

	@Schema(description = "Clear specific cache on this instance")
	@GetMapping("/clear/{cacheName}")
	public void clearCache(@PathVariable String cacheName) {
		localCacheEvictionService.clearCache(cacheName);
	}

	@Schema(description = "Evict specific cache entry on this instance")
	@GetMapping("/evict/{cacheName}/{key}")
	public void evictCache(@PathVariable String cacheName, @PathVariable String key) {
		localCacheEvictionService.evictCache(cacheName, key);
	}

}