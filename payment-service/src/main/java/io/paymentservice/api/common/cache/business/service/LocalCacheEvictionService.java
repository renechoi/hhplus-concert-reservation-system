package io.paymentservice.api.common.cache.business.service;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */
@Service
@RequiredArgsConstructor
public class LocalCacheEvictionService {
	private final CacheManager cacheManager;

	public void evictCache(String cacheName, String key) {
		if (cacheManager.getCache(cacheName) != null) {
			cacheManager.getCache(cacheName).evict(key);
		}
	}

	public void clearCache(String cacheName) {
		if (cacheManager.getCache(cacheName) != null) {
			cacheManager.getCache(cacheName).clear();
		}
	}

	public void clearAllCaches() {
		cacheManager.getCacheNames().forEach(cacheName -> {
			if (cacheManager.getCache(cacheName) != null) {
				cacheManager.getCache(cacheName).clear();
			}
		});
	}
}