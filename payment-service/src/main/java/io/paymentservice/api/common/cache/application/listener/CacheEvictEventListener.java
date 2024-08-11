package io.paymentservice.api.common.cache.application.listener;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import io.paymentservice.api.common.cache.business.dto.command.PropagateCacheEvictionCommand;
import io.paymentservice.api.common.cache.business.dto.event.LocalCacheEvictEvent;
import io.paymentservice.api.common.cache.business.service.CacheEvictionPropagationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CacheEvictEventListener {

	private final CacheEvictionPropagationService cacheEvictionPropagationService;

	@TransactionalEventListener
	@Async
	public void handleLocalCacheEvictEvent(LocalCacheEvictEvent event) {
		try {
			cacheEvictionPropagationService.propagateCacheEviction(PropagateCacheEvictionCommand.of(event.getCacheName(), event.getKey()));
		} catch (Exception e) {
			log.error("Failed to evict cache for LocalCacheEvictEvent: {} - {} - {}", event.getCacheName(),event.getKey(), e.getMessage());
		}
	}
}
