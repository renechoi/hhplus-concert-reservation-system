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

	public WaitingQueueTokenCounter getAndIncreaseCounter(long maxWaitingTokens) {
		WaitingQueueTokenCounter counter = waitingQueueRepository.getOrInitializeCounter();
		waitingQueueRepository.save(counter.increase(maxWaitingTokens));
		return counter;
	}
}
