package io.queuemanagement.api.business.service.impl;

import static io.queuemanagement.api.business.dto.inport.ProcessingQueueTokenSearchCommand.*;
import static io.queuemanagement.api.business.dto.inport.WaitingQueueTokenSearchCommand.*;
import static io.queuemanagement.util.YmlLoader.*;
import static java.time.LocalDateTime.*;

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
	private final WaitingQueueTokenRetrievalRepository waitingQueueRepository;
	private final WaitingQueueTokenManagementRepository waitingQueueManagementRepository;
	private final ProcessingQueueRetrievalRepository processingQueueRepository;
	private final ProcessingQueueEnqueueRepository processingQueueEnqueueRepository;
	private final ProcessingQueueStoreRepository processingQueueStoreRepository;
	private final WaitingQueueTokenCounterCrudRepository waitingQueueCounterRepository;

	@Transactional
	@Override
	public void processQueueTransfer() {
		long availableSlots = processingQueueRepository.countAvailableSlots(processingTokensMaxLimit());

		if (availableSlots <= 0) {
			return;
		}

		waitingQueueRepository.findAllByCondition(onTopWaitingToken())
			.stream()
			.limit(availableSlots)
			.toList()
			.forEach(waitingToken -> {
				processingQueueEnqueueRepository.enqueue(waitingToken.toProcessing());
				waitingQueueManagementRepository.updateStatus(waitingToken.proccessed());
			});

		waitingQueueCounterRepository.save(waitingQueueCounterRepository.getCounter().decrease(availableSlots));
	}

	@Transactional
	@Override
	public void expireProcessingQueueTokens() {
		processingQueueRepository
			.findAllByCondition(onProcessingTokensExpiring(now()))
			.forEach(token -> processingQueueStoreRepository.store(token.expire()));
	}

	@Override
	@Transactional
	public void expireWaitingQueueTokens() {
		waitingQueueRepository
			.findAllByCondition(onWaitingTokensExpiring(now()))
			.forEach(token -> waitingQueueManagementRepository.updateStatus(token.expire()));
	}

	@Transactional
	@Override
	public void completeProcessingQueueToken(String userId) {
		processingQueueStoreRepository.store(
			processingQueueRepository.findSingleByCondition(onActiveProcessing(userId)).complete());
	}

	@Override
	@Transactional
	public void completeWaitingQueueTokenByUserId(String userId) {
		waitingQueueRepository
			.findAllByCondition(onActiveWaiting(userId))
			.forEach(item -> waitingQueueManagementRepository.updateStatus(item.complete()));
	}
}
