package io.queuemanagement.api.business.service.impl;

import static io.queuemanagement.api.business.domainmodel.ProcessingQueueToken.*;
import static io.queuemanagement.api.business.domainmodel.QueueStatus.*;
import static io.queuemanagement.api.business.domainmodel.WaitingQueuePositionJson.*;
import static io.queuemanagement.api.business.dto.inport.ProcessingQueueTokenSearchCommand.*;
import static io.queuemanagement.api.business.dto.inport.WaitingQueueTokenSearchCommand.*;
import static io.queuemanagement.common.mapper.ObjectMapperBasedVoMapper.*;
import static io.queuemanagement.util.YmlLoader.*;
import static java.time.LocalDateTime.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.queuemanagement.api.business.domainmodel.ProcessingQueueToken;
import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import io.queuemanagement.api.business.domainmodel.WaitingQueueTokenCounter;
import io.queuemanagement.api.business.persistence.ProcessingQueueEnqueueRepository;
import io.queuemanagement.api.business.persistence.ProcessingQueueRetrievalRepository;
import io.queuemanagement.api.business.persistence.ProcessingQueueStoreRepository;
import io.queuemanagement.api.business.persistence.QueuePositionDocumentRepository;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenCounterCrudRepository;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenManagementRepository;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenRetrievalRepository;
import io.queuemanagement.api.business.service.QueueManagementService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

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
	private final QueuePositionDocumentRepository queuePositionDocumentRepository;

	@Transactional
	@Override
	public void processScheduledQueueTransfer() {
		long availableSlots = getMaxProcessingTokens() - processingQueueRetrievalRepository.countByStatus(PROCESSING);

		if (availableSlots <= 0) {
			return;
		}

		waitingQueueTokenRetrievalRepository.findAllByCondition(searchByStatusAndOrderByRequestAtAsc(WAITING))
			.stream()
			.limit(availableSlots)
			.toList()
			.forEach(token -> {
				processingQueueEnqueueRepository.enqueue(createProcessingQueueTokenByWaitingQueueToken(token));
				waitingQueueTokenManagementRepository.updateStatus(token.withProccessing());
			});

		WaitingQueueTokenCounter counter = waitingQueueTokenCounterCrudRepository.getCounterWithThrows().withDecreasedCount(availableSlots);
		waitingQueueTokenCounterCrudRepository.save(counter);
	}

	@Transactional
	@Override
	public void expireProcessingQueueTokens() {
		List<ProcessingQueueToken> tokensToExpire = processingQueueRetrievalRepository.findAllByCondition(searchByStatusAndValidUntil(PROCESSING,  now(), "before"));
		tokensToExpire.forEach(token -> {
			token = token.withExpired();
			processingQueueStoreRepository.store(token);
		});
	}

	@Override
	@Transactional
	public void expireWaitingQueueTokens() {
		List<WaitingQueueToken> tokensToExpire = waitingQueueTokenRetrievalRepository.findAllByCondition(searchByStatusesAndValidUntil(List.of(WAITING, PROCESSING), now(), "before"));
		tokensToExpire.forEach(token -> {
			token = token.withExpired();
			waitingQueueTokenManagementRepository.updateStatus(token);
		});
	}

	@Transactional
	@Override
	public void completeProcessingQueueToken(String userId) {
		ProcessingQueueToken processingQueueToken = processingQueueRetrievalRepository.findSingleByConditionWithThrows(searchByUserIdAndStatus(userId, PROCESSING));
		processingQueueToken = processingQueueToken.withCompleted();
		processingQueueStoreRepository.store(processingQueueToken);
	}

	@Override
	@Transactional
	public void completeWaitingQueueTokenByUserId(String userId) {
		waitingQueueTokenRetrievalRepository.findAllByCondition(createWaitingTokenSearchConditionByUserIdAndStatus(userId, PROCESSING))
			.forEach(
				item -> waitingQueueTokenManagementRepository.updateStatus(item.withCompleted())
			);
	}

	@Transactional
	@Override
	@Deprecated
	public void recalculateWaitingQueuePositionsBySimpleReOrderingEach() {
		List<WaitingQueueToken> waitingTokens = waitingQueueTokenRetrievalRepository.findAllByCondition(searchByStatusAndOrderByRequestAtAsc(WAITING));

		long currentPosition = 1;
		for (WaitingQueueToken token : waitingTokens) {
			token = token.withPositionValue(currentPosition++);
			waitingQueueTokenManagementRepository.updatePosition(token);
		}
	}

	@SneakyThrows
	@Transactional
	@Override
	@Deprecated
	public void recalculateWaitingQueuePositionsWithJsonStoring() {
		List<WaitingQueueToken> waitingTokens = waitingQueueTokenRetrievalRepository.findAllByCondition(searchByStatusAndOrderByRequestAtAsc(WAITING));

		Map<Long, Long> positionMap = new HashMap<>();
		long currentPosition = 1;
		for (WaitingQueueToken token : waitingTokens) {
			positionMap.put(token.getWaitingQueueTokenId(), currentPosition++);
		}

		queuePositionDocumentRepository.updateQueuePositionJson(createPositionJson(1L, getObjectMapper().writeValueAsString(positionMap)));
	}

}
