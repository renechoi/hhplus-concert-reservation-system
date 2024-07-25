package io.queuemanagement.api.business.operators;

import static io.queuemanagement.api.business.dto.inport.ProcessingQueueTokenSearchCommand.*;
import static io.queuemanagement.api.business.dto.inport.WaitingQueueTokenSearchCommand.*;

import java.util.Optional;

import org.springframework.stereotype.Component;

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

	private final WaitingQueueTokenRetrievalRepository waitingQueueRepository;
	private final ProcessingQueueRetrievalRepository processingQueueRepository;

	/**
	 * <p>
	 * 사용자 ID를 기반으로 대기열 및 처리열에 중복된 토큰이 있는지 확인한다.
	 * </p>
	 * <p>
	 * 대기열 토큰 중복 체크 -> 정책에 따라서 대기열에 이미 존재하는 경우, 처리열에 이미 존재하는 경우 어떻게 할지를 결정한다
	 * 현재의 구현은 대기열 혹은 처리열에 이미 존재하는 경우, 해당 토큰을 반환하도록 구현되어 있다
	 * </p>
	 *
	 * <p>
	 * 동시성 이슈가 발생 가능한 코드이다.
	 * 두 스레드가 동시에 중복 체크를 통과하여 각각 새로운 토큰을 생성할 때 중복 토큰 생성 문제가 발생할 수 있다.
	 * 이를 해결하기 위한 대안 중 하나는 비관적 락을 사용하여 중복 체크와 토큰 생성 로직을 동시에 실행하지 못하게 하는 것이다.
	 * </p>
	 *
	 * @param userId 중복 체크를 수행할 사용자 ID
	 * @return 중복된 토큰이 존재하는 경우 해당 토큰 정보를 반환하고, 그렇지 않은 경우 빈 값을 반환
	 */
	public Optional<WaitingQueueTokenGenerateInfo> check(String userId) {
		return waitingQueueRepository.findOptionalByConditionWithLock(onActiveWaiting(userId))
			.map(WaitingQueueTokenGenerateInfo::from)
			.or(() -> processingQueueRepository.findOptionalByCondition(onActiveProcessing(userId))
				.map(WaitingQueueTokenGenerateInfo::from));

	}
}
