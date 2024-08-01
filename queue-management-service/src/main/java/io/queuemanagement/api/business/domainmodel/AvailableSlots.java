package io.queuemanagement.api.business.domainmodel;

import static io.queuemanagement.util.YmlLoader.*;

import io.queuemanagement.common.annotation.DomainModel;
import io.queuemanagement.common.exception.definitions.ProcessingQueueMaxLimitReachedException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@DomainModel
@Slf4j
public class AvailableSlots {
	private long availableSlots;

	private boolean isAvailable;



	public static AvailableSlots from(long currentCount) {
		if(isNotAvailable(currentCount)) {
			log.info("AvailableSlots is not available");
			return AvailableSlots.builder()
				.availableSlots(0)
				.isAvailable(false)
				.build();
		}

		return AvailableSlots.builder()
			.availableSlots(processingTokensMaxLimit() - currentCount)
			.build();
	}

	private static boolean isNotAvailable(long availableSlots) {
		return availableSlots >= processingTokensMaxLimit();
	}

	public boolean isSlotLimited(){
		return availableSlots <= 0;
	}

	public int counts() {

		if(isSlotLimited()){
			throw new ProcessingQueueMaxLimitReachedException();
		}

		return (int)this.availableSlots;
	}
}
