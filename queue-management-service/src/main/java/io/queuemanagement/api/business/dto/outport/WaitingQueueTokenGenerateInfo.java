package io.queuemanagement.api.business.dto.outport;

import java.time.LocalDateTime;

import io.queuemanagement.api.business.domainmodel.ProcessingQueueToken;
import io.queuemanagement.api.business.domainmodel.QueueStatus;
import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import io.queuemanagement.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/05
 */
public record WaitingQueueTokenGenerateInfo(
	String userId,
	String tokenValue,
	LocalDateTime remainingTime,
	int position,
	LocalDateTime validUntil,
	QueueStatus status
) {
	public static WaitingQueueTokenGenerateInfo from(WaitingQueueToken savedToken) {
		return ObjectMapperBasedVoMapper.convert(savedToken, WaitingQueueTokenGenerateInfo.class);
	}

	public static WaitingQueueTokenGenerateInfo from(ProcessingQueueToken savedToken) {
		return ObjectMapperBasedVoMapper.convert(savedToken, WaitingQueueTokenGenerateInfo.class);
	}
}
