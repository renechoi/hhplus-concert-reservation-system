package io.queuemanagement.api.infrastructure.persistence.core;

import static io.queuemanagement.api.business.domainmodel.CounterKey.*;
import static io.queuemanagement.api.infrastructure.clients.redisservice.dto.CacheDomainServiceRequest.*;
import static io.queuemanagement.api.infrastructure.clients.redisservice.dto.CounterDomainServiceRequest.*;

import org.springframework.stereotype.Repository;

import io.queuemanagement.api.business.domainmodel.AvailableSlots;
import io.queuemanagement.api.business.domainmodel.ProcessingQueueToken;
import io.queuemanagement.api.business.persistence.ProcessingQueueRepository;
import io.queuemanagement.api.infrastructure.clients.redisservice.RedisServiceClient;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.CacheDomainServiceResponse;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.CounterDomainServiceResponse;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.EvictCacheDomainServiceRequest;
import io.queuemanagement.common.exception.definitions.ProcessingQueueTokenNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
@Repository
@RequiredArgsConstructor
public class ProcessingQueueCoreRepository implements ProcessingQueueRepository {

	private final RedisServiceClient redisServiceClient;

	@Override
	public ProcessingQueueToken enqueue(ProcessingQueueToken processingQueueToken) {
		redisServiceClient.cache(processingTokenQueue(processingQueueToken));
		redisServiceClient.increment(incrementRequest(PROCESSING_QUEUE_COUNTER.getValue(), 1));
		return processingQueueToken;
	}

	@Override
	public ProcessingQueueToken dequeue(ProcessingQueueToken processingQueueToken) {
		redisServiceClient.evictCache(EvictCacheDomainServiceRequest.processingTokenQueue(processingQueueToken));
		redisServiceClient.decrement(decrementRequest(PROCESSING_QUEUE_COUNTER.getValue(), 1));
		return processingQueueToken;
	}

	@Override
	public AvailableSlots countAvailableSlots() {
		CounterDomainServiceResponse counterResponse = redisServiceClient.getCounter(PROCESSING_QUEUE_COUNTER.getValue()).getBody();
		long currentCount = counterResponse != null ? counterResponse.getValue() : 0;
		return AvailableSlots.from(currentCount);
	}

	@Override
	public ProcessingQueueToken retrieveToken(String tokenValue) {
		CacheDomainServiceResponse response = redisServiceClient.getCache(tokenValue).getBody();
		if (response == null) {
			throw new ProcessingQueueTokenNotFoundException();
		}
		return ProcessingQueueToken.builder()
			.tokenValue(tokenValue)
			.userId(String.valueOf(response.getCacheValue()))
			.build();
	}

	@Override
	public void increaseCounter(int counts) {
		redisServiceClient.increment(incrementRequest(PROCESSING_QUEUE_COUNTER.getValue(), counts));
	}

	@Override
	public void decreaseCounter(int counts) {
		redisServiceClient.decrement(incrementRequest(PROCESSING_QUEUE_COUNTER.getValue(), counts));
	}

}
