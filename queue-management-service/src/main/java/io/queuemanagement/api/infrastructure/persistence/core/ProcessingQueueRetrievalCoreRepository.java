package io.queuemanagement.api.infrastructure.persistence.core;

import static io.queuemanagement.api.business.domainmodel.CounterKey.*;
import static io.queuemanagement.api.infrastructure.clients.redisservice.dto.CounterDomainServiceRequest.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import io.queuemanagement.api.business.domainmodel.AvailableSlots;
import io.queuemanagement.api.business.domainmodel.CounterKey;
import io.queuemanagement.api.business.domainmodel.ProcessingQueueToken;
import io.queuemanagement.api.business.domainmodel.QueueStatus;
import io.queuemanagement.api.business.dto.inport.ProcessingQueueTokenSearchCommand;
import io.queuemanagement.api.business.persistence.ProcessingQueueRetrievalRepository;
import io.queuemanagement.api.infrastructure.clients.redisservice.RedisServiceClient;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.CacheDomainServiceResponse;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.CounterDomainServiceRequest;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.CounterDomainServiceResponse;
import io.queuemanagement.api.infrastructure.entity.ProcessingQueueTokenEntity;
import io.queuemanagement.api.infrastructure.persistence.orm.ProcessingQueueTokenJpaRepository;
import io.queuemanagement.common.exception.definitions.ProcessingQueueTokenNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
@Repository
@RequiredArgsConstructor
public class ProcessingQueueRetrievalCoreRepository implements ProcessingQueueRetrievalRepository {
	private final ProcessingQueueTokenJpaRepository processingQueueTokenJpaRepository;
	private final RedisServiceClient redisServiceClient;

	@Override
	public AvailableSlots countAvailableSlots() {
		// 기존 RDB
		// return maxLimit - processingQueueTokenJpaRepository.countByStatus(QueueStatus.PROCESSING);

		CounterDomainServiceResponse counterResponse = redisServiceClient.getCounter(PROCESSING_QUEUE_COUNTER.getValue()).getBody();
		long currentCount = counterResponse != null ? counterResponse.getValue() : 0;
		return AvailableSlots.from(currentCount);
	}

	// key - tokenvalue, value - userId
	@Override
	public ProcessingQueueToken getToken(String tokenValue) {
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
	public void increase(int counts) {
		redisServiceClient.increment(incrementRequest(PROCESSING_QUEUE_COUNTER.getValue(), counts));
	}

	@Override
	public ProcessingQueueToken findSingleByCondition(ProcessingQueueTokenSearchCommand searchCommand) {
		return processingQueueTokenJpaRepository.findSingleByCondition(searchCommand).orElseThrow(ProcessingQueueTokenNotFoundException::new).toDomain();
	}

	@Override
	public Optional<ProcessingQueueToken> findOptionalByCondition(ProcessingQueueTokenSearchCommand searchCommand) {
		return processingQueueTokenJpaRepository.findSingleByCondition(searchCommand).map(ProcessingQueueTokenEntity::toDomain);
	}

	@Override
	public List<ProcessingQueueToken> findAllByCondition(ProcessingQueueTokenSearchCommand searchCommand) {
		return processingQueueTokenJpaRepository.findAllByCondition(searchCommand)
			.stream()
			.map(ProcessingQueueTokenEntity::toDomain)
			.toList();
	}


}
