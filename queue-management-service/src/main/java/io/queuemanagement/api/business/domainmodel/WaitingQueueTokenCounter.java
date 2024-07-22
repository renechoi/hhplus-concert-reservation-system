package io.queuemanagement.api.business.domainmodel;

import io.queuemanagement.common.annotation.DomainModel;
import io.queuemanagement.common.exception.definitions.WaitingQueueMaxLimitExceededException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/05
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@DomainModel
public class WaitingQueueTokenCounter {
	private Long waitingQueueTokenCounterId;
	private Long count;

	public static WaitingQueueTokenCounter createInitializingCounter() {
		return WaitingQueueTokenCounter.builder().count(0L).build();
	}

	public WaitingQueueTokenCounter withIncreasedCount() {
		this.count = (this.count == null) ? 1L : this.count + 1;
		return this;
	}

	public static WaitingQueueTokenCounter createDefault() {
		return WaitingQueueTokenCounter.builder().count(0L).build();
	}


	public boolean isNotAvailable(long maxWaitingTokens){
		return !this.isAvailable(maxWaitingTokens);
	}
	public boolean isAvailable(long maxWaitingTokens) {
		return this.count < maxWaitingTokens;
	}

	public WaitingQueueTokenCounter withDecreasedCount(long decreasedCount) {
		this.count = Math.max(this.count - decreasedCount, 0);
		return this;
	}

	public WaitingQueueTokenCounter withAvailabilityVerified(long maxWaitingTokens) {
		if (isNotAvailable(maxWaitingTokens)) {
			throw new WaitingQueueMaxLimitExceededException();
		}
		return this;
	}
}
