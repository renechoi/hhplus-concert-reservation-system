package io.queuemanagement.api.business.service.impl;

import static io.queuemanagement.api.business.dto.inport.ProcessingQueueTokenSearchCommand.*;
import static io.queuemanagement.api.business.dto.inport.WaitingQueueTokenSearchCommand.*;
import static io.queuemanagement.common.token.QueueTokenGenerator.*;
import static java.time.LocalDateTime.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.queuemanagement.api.business.domainmodel.AvailableSlots;
import io.queuemanagement.api.business.dto.inport.CompletedTokenHandlingCommand;
import io.queuemanagement.api.business.dto.inport.ExpiredTokenHandlingCommand;
import io.queuemanagement.api.business.persistence.ProcessingQueueRepository;
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
	private final ProcessingQueueRepository processingQueueRepository;
	private final WaitingQueueTokenCounterCrudRepository waitingQueueCounterRepository;

	// @Transactional
	@Override
	public void processQueueTransfer() {
		// AvailableSlots availableSlots = processingQueueRepository.countAvailableSlots();
		// if (availableSlots.isSlotLimited()) {
		// 	return;
		// }
		// waitingQueueRepository.findAllByCondition(onTopWaitingToken()).stream()
		// 	.toList()
		// 	.forEach(waitingToken -> {
		// 		processingQueueRepository.enqueue(waitingToken.toProcessing());
		// 		waitingQueueManagementRepository.updateStatus(waitingToken.proccessed());
		// 	});
		// waitingQueueCounterRepository.save(waitingQueueCounterRepository.getCounter().decrease(availableSlots.getAvailableSlots()));



		// // 1. Redis에서 counter 값을 조회
		AvailableSlots availableSlots = processingQueueRepository.countAvailableSlots();

		if(availableSlots.isSlotLimited()){
			return;
		}

		// 2. 카운터 값이 설정된 max limit보다 작다면 그 차액만큼 처리열로 이관 진행
		waitingQueueRepository.findTopTokens(availableSlots.counts()).stream()
			.toList()
			.forEach(waitingToken -> {
				processingQueueRepository.enqueue(waitingToken.toProcessing());
				waitingQueueManagementRepository.remove(waitingToken);
			});
	}


	@Override
	public void completeTokensByKeys(ExpiredTokenHandlingCommand command) {
		processingQueueRepository.decreaseCounter(command.getSize());
	}

	@Override
	public void completeToken(CompletedTokenHandlingCommand command) {
		processingQueueRepository.dequeue(command.toProcessingQueueToken().withTokenValue(generateToken(command.getUserId())));
	}


	@Transactional
	@Override
	public void expireProcessingQueueTokens() {
		processingQueueRepository
			.findAllByCondition(onProcessingTokensExpiring(now()))
			.forEach(token -> processingQueueRepository.store(token.expire()));
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
		processingQueueRepository.store(
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
