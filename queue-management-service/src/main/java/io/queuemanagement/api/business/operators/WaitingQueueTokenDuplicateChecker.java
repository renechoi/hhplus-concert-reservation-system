package io.queuemanagement.api.business.operators;

import static io.queuemanagement.api.business.domainmodel.QueueStatus.*;
import static io.queuemanagement.api.business.dto.inport.ProcessingQueueTokenSearchCommand.*;
import static io.queuemanagement.api.business.dto.inport.WaitingQueueTokenSearchCommand.*;

import java.util.Optional;

import org.springframework.stereotype.Component;

import io.queuemanagement.api.business.domainmodel.ProcessingQueueToken;
import io.queuemanagement.api.business.domainmodel.QueueStatus;
import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import io.queuemanagement.api.business.dto.outport.WaitingQueueTokenGenerateInfo;
import io.queuemanagement.api.business.persistence.ProcessingQueueRetrievalRepository;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenRetrievalRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */

@Component
@RequiredArgsConstructor
public class WaitingQueueTokenDuplicateChecker {

	private final WaitingQueueTokenRetrievalRepository waitingQueueTokenRetrievalRepository;
	private final ProcessingQueueRetrievalRepository processingQueueRetrievalRepository;

	/**
	 * 대기열 토큰 중복 체크 -> 정책에 따라서 대기열에 이미 존재하는 경우, 처리열에 이미 존재하는 경우 어떻게 할지를 결정한다
	 * 현재의 구현은 대기열 혹은 처리열에 이미 존재하는 경우, 해당 토큰을 반환하도록 구현되어 있다
	 */
	public Optional<WaitingQueueTokenGenerateInfo> checkForDuplicate(String userId) {
		Optional<WaitingQueueToken> waitingQueueTokenByUserOptional = waitingQueueTokenRetrievalRepository.findSingleByConditionOptional(createWaitingTokenSearchConditionByUserIdAndStatus(userId, QueueStatus.WAITING));
		if (waitingQueueTokenByUserOptional.isPresent()) {
			return Optional.ofNullable(WaitingQueueTokenGenerateInfo.from(waitingQueueTokenByUserOptional.get()));
		}

		Optional<ProcessingQueueToken> processingQueueTokenOptional = processingQueueRetrievalRepository.findSingleByConditionOptional(searchByUserIdAndStatus(userId, PROCESSING));
		return processingQueueTokenOptional.map(WaitingQueueTokenGenerateInfo::from);

	}
}
