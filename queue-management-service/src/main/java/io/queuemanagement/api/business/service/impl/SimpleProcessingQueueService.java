package io.queuemanagement.api.business.service.impl;

import static io.queuemanagement.common.token.QueueTokenGenerator.*;

import org.springframework.stereotype.Service;

import io.queuemanagement.api.business.domainmodel.AvailableSlots;
import io.queuemanagement.api.business.dto.inport.CompletedTokenHandlingCommand;
import io.queuemanagement.api.business.dto.inport.ExpiredTokenHandlingCommand;
import io.queuemanagement.api.business.dto.outport.ProcessingQueueTokenGeneralInfo;
import io.queuemanagement.api.business.persistence.ProcessingQueueRepository;
import io.queuemanagement.api.business.persistence.WaitingQueueRepository;
import io.queuemanagement.api.business.service.ProcessingQueueService;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Service
@RequiredArgsConstructor
public class SimpleProcessingQueueService implements ProcessingQueueService {

	private final WaitingQueueRepository waitingQueueRepository;
	private final ProcessingQueueRepository processingQueueRepository;



	@Override
	public ProcessingQueueTokenGeneralInfo checkAvailability(String tokenValue, String userId) {
		return ProcessingQueueTokenGeneralInfo.from(processingQueueRepository.retrieveToken(tokenValue)).withProcessingAndUserInfo(userId);
	}

	@Override
	public ProcessingQueueTokenGeneralInfo checkAvailability(String tokenValue) {
		return checkAvailability(tokenValue, null);
	}

	@Override
	public ProcessingQueueTokenGeneralInfo retrieve(String userId) {
		return ProcessingQueueTokenGeneralInfo.from(processingQueueRepository.retrieveToken(generateToken(userId)));
	}



	@Override
	public void processQueueTransfer() {
		//  1. Redis에서 counter 값을 조회
		AvailableSlots availableSlots = processingQueueRepository.countAvailableSlots();

		if (availableSlots.isSlotLimited()) {
			return;
		}

		// 2. 카운터 값이 설정된 max limit보다 작다면 그 차액만큼 처리열로 이관 진행
		waitingQueueRepository.retrieveTopTokens(availableSlots.counts()).stream()
			.toList()
			.forEach(waitingToken -> {
				processingQueueRepository.enqueue(waitingToken.toProcessing());

				waitingQueueRepository.remove(waitingToken);// todo -> 결합도 해제 - 이벤트
			});
	}

	@Override
	public void completeTokens(ExpiredTokenHandlingCommand command) {
		processingQueueRepository.decreaseCounter(command.getSize());
	}

	@Override
	public void completeToken(CompletedTokenHandlingCommand command) {
		processingQueueRepository.dequeue(command.toProcessingQueueToken().withTokenValue(generateToken(command.getUserId())));
	}
}
