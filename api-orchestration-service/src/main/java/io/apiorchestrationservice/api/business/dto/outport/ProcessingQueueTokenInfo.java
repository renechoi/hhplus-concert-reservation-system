package io.apiorchestrationservice.api.business.dto.outport;

import java.time.LocalDateTime;

import io.apiorchestrationservice.api.business.domainmodel.QueueStatus;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public record ProcessingQueueTokenInfo(
	 Long processingQueueTokenId,
	 String userId,
	 String tokenValue,
	 Long position,
	 LocalDateTime validUntil,
	QueueStatus status
) {
	public boolean hasEqual(String tokenValue) {
		return tokenValue.equals(this.tokenValue);
	}
}
