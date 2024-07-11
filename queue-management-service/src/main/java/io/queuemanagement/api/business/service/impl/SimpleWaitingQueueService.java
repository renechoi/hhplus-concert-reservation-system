package io.queuemanagement.api.business.service.impl;

import static io.queuemanagement.api.business.domainmodel.WaitingQueueToken.*;
import static io.queuemanagement.common.mapper.ObjectMapperBasedVoMapper.*;
import static io.queuemanagement.util.YmlLoader.*;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.queuemanagement.api.business.domainmodel.WaitingQueuePositionJson;
import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import io.queuemanagement.api.business.domainmodel.WaitingQueueTokenCounter;
import io.queuemanagement.api.business.dto.inport.WaitingQueueTokenGenerateCommand;
import io.queuemanagement.api.business.dto.outport.WaitingQueueTokenGeneralInfo;
import io.queuemanagement.api.business.dto.outport.WaitingQueueTokenGenerateInfo;
import io.queuemanagement.api.business.operators.WaitingQueueTokenDuplicateChecker;
import io.queuemanagement.api.business.persistence.QueuePositionDocumentRepository;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenCounterCrudRepository;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenEnqueueRepository;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenRetrievalRepository;
import io.queuemanagement.api.business.service.WaitingQueueService;
import io.queuemanagement.common.exception.WaitingQueueMaxLimitExceededException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Service
@RequiredArgsConstructor
public class SimpleWaitingQueueService implements WaitingQueueService {
	private final WaitingQueueTokenEnqueueRepository waitingQueueTokenEnqueueRepository;
	private final WaitingQueueTokenCounterCrudRepository waitingQueueTokenCounterCrudRepository;
	private final WaitingQueueTokenRetrievalRepository waitingQueueTokenRetrievalRepository;
	private final WaitingQueueTokenDuplicateChecker waitingQueueTokenDuplicateChecker;
	private final QueuePositionDocumentRepository queuePositionDocumentRepository;

	@Override
	@Transactional
	public WaitingQueueTokenGenerateInfo generateAndEnqueue(WaitingQueueTokenGenerateCommand command) {
		Optional<WaitingQueueTokenGenerateInfo> checkForDuplicate = waitingQueueTokenDuplicateChecker.checkForDuplicate(command.getUserId());
		if (checkForDuplicate.isPresent()) {
			return checkForDuplicate.get();
		}

		WaitingQueueTokenCounter counter = waitingQueueTokenCounterCrudRepository.getOrInitializeCounter();

		if (counter.isNotAvailable(getMaxWaitingTokens())) { // 대기열에 인입 불가능한 경우
			throw new WaitingQueueMaxLimitExceededException();
		}

		waitingQueueTokenCounterCrudRepository.save(counter.withIncreasedCount());

		return WaitingQueueTokenGenerateInfo.from(waitingQueueTokenEnqueueRepository.enqueue(createToken(command).withPositionValue(counter.getCount())));
	}

	@SneakyThrows
	@Override
	@Transactional(readOnly = true)
	public WaitingQueueTokenGeneralInfo retrieve(String userId) {
		WaitingQueueToken token = waitingQueueTokenRetrievalRepository.findTokenByUserIdWithThrows(userId);

		WaitingQueuePositionJson positionDocument = queuePositionDocumentRepository.findDocumentByIdOrElseDefault(1L);
		Map<Long, Long> positionMap = getObjectMapper().readValue(positionDocument.getPositionJson(), Map.class);

		if(positionMap.isEmpty()){
			return WaitingQueueTokenGeneralInfo.from(token.withPositionValue(0L));
		}

		return WaitingQueueTokenGeneralInfo.from(
			token.withPositionValue(positionMap.get(token.getWaitingQueueTokenId()) != null ? positionMap.get(token.getWaitingQueueTokenId()) : token.getPosition()));
	}

}
