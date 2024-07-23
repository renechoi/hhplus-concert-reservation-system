package io.queuemanagement.api.business.service.impl;

import static io.queuemanagement.api.business.domainmodel.QueueStatus.*;
import static io.queuemanagement.api.business.dto.inport.DateSearchCommand.DateSearchCondition.*;
import static io.queuemanagement.api.business.dto.inport.ProcessingQueueTokenSearchCommand.*;
import static io.queuemanagement.api.business.dto.inport.WaitingQueueTokenSearchCommand.*;
import static io.queuemanagement.util.YmlLoader.*;
import static java.time.LocalDateTime.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.queuemanagement.api.business.persistence.ProcessingQueueEnqueueRepository;
import io.queuemanagement.api.business.persistence.ProcessingQueueRetrievalRepository;
import io.queuemanagement.api.business.persistence.ProcessingQueueStoreRepository;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenCounterCrudRepository;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenManagementRepository;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenRetrievalRepository;
import io.queuemanagement.api.business.service.QueueManagementService;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
@Service
@RequiredArgsConstructor
public class SimpleQueueManagementService implements QueueManagementService {
	private final WaitingQueueTokenRetrievalRepository waitingQueueTokenRetrievalRepository;
	private final WaitingQueueTokenManagementRepository waitingQueueTokenManagementRepository;
	private final ProcessingQueueRetrievalRepository processingQueueRetrievalRepository;
	private final ProcessingQueueEnqueueRepository processingQueueEnqueueRepository;
	private final ProcessingQueueStoreRepository processingQueueStoreRepository;
	private final WaitingQueueTokenCounterCrudRepository waitingQueueTokenCounterCrudRepository;

	@Transactional
	@Override
	public void processQueueTransfer() {
		long availableSlots = processingTokensMaxLimit() - processingQueueRetrievalRepository.countByStatus(PROCESSING);

		if (availableSlots <= 0) {
			return;
		}

		waitingQueueTokenRetrievalRepository
			.findAllByCondition(statusAndOrderByRequestAtAsc(WAITING))
			.stream()
			.limit(availableSlots)
			.toList()
			.forEach(waitingQueueToken -> {
				processingQueueEnqueueRepository.enqueue(waitingQueueToken.toProcessingToken());
				waitingQueueTokenManagementRepository.updateStatus(waitingQueueToken.withProccessing());
			});

		waitingQueueTokenCounterCrudRepository.save(waitingQueueTokenCounterCrudRepository.getCounter().withDecreasedCount(availableSlots));
	}

	@Transactional
	@Override
	public void expireProcessingQueueTokens() {
		processingQueueRetrievalRepository
			.findAllByCondition(statusAndValidUntil(PROCESSING,  now(), BEFORE))
			.forEach(token -> processingQueueStoreRepository.store(token.withExpired()));
	}

	@Override
	@Transactional
	public void expireWaitingQueueTokens() {
		waitingQueueTokenRetrievalRepository
			.findAllByCondition(statusesAndValidUntil(List.of(WAITING, PROCESSING), now(), BEFORE))
			.forEach(token -> waitingQueueTokenManagementRepository.updateStatus(token.withExpired()));
	}

	@Transactional
	@Override
	public void completeProcessingQueueToken(String userId) {
		processingQueueStoreRepository.store(
			processingQueueRetrievalRepository.findSingleByCondition(userIdAndStatus(userId, PROCESSING)).withCompleted());
	}

	@Override
	@Transactional
	public void completeWaitingQueueTokenByUserId(String userId) {
		waitingQueueTokenRetrievalRepository
			.findAllByCondition(conditionOnUserIdAndStatus(userId, PROCESSING))
			.forEach(item -> waitingQueueTokenManagementRepository.updateStatus(item.withCompleted()));
	}
}
