package io.queuemanagement.api.business.operators;

import org.springframework.stereotype.Component;

import io.queuemanagement.api.business.domainmodel.WaitingQueueTokenCounter;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenCounterCrudRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/22
 */
@Component
@RequiredArgsConstructor
public class WaitingQueueTokenCounterManager {
	private final WaitingQueueTokenCounterCrudRepository waitingQueueRepository;

	public WaitingQueueTokenCounter getWithLockAndIncreaseCounter(long maxWaitingTokens) {
		WaitingQueueTokenCounter counter = waitingQueueRepository.getWithLockOrInitializeCounter();
		waitingQueueRepository.save(counter.increase(maxWaitingTokens));
		return counter;
	}

	/**
	 * 동시성 제어가 되지 었지 않은 코드
	 */
	public WaitingQueueTokenCounter getIncreaseCounter(long maxWaitingTokens) {
		WaitingQueueTokenCounter counter = waitingQueueRepository.getOrInitializeCounter();
		waitingQueueRepository.save(counter.increase(maxWaitingTokens));
		return counter;
	}
}
