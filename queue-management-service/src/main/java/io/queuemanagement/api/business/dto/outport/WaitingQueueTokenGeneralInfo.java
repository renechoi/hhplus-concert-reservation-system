package io.queuemanagement.api.business.dto.outport;

import java.time.LocalDateTime;

import io.queuemanagement.api.business.domainmodel.QueueStatus;
import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import io.queuemanagement.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public record WaitingQueueTokenGeneralInfo(
	Long waitingQueueTokenId,
	String userId,
	String tokenValue,
	Long position,
	LocalDateTime validUntil,
	QueueStatus status,
	LocalDateTime requestAt,
	Boolean isEmpty
) {

	public static WaitingQueueTokenGeneralInfo from(WaitingQueueToken waitingQueueToken) {
		return ObjectMapperBasedVoMapper.convert(waitingQueueToken, WaitingQueueTokenGeneralInfo.class);
	}


	public boolean empty() {
		return Boolean.TRUE.equals(isEmpty);
	}
}
