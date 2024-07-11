package io.clientchannelservice.api.business.dto.outport;

import java.time.LocalDateTime;

import io.clientchannelservice.api.business.domainmodel.QueueStatus;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public record WaitingQueueTokenPollingInfo(
	Long waitingQueueTokenId,
	String userId,
	String tokenValue,
	Long position,
	LocalDateTime validUntil,
	QueueStatus status,
	LocalDateTime requestAt
) {
}
