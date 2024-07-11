package io.queuemanagement.api.application.dto.response;

import java.time.LocalDateTime;

import io.queuemanagement.api.business.domainmodel.QueueStatus;
import io.queuemanagement.api.business.dto.outport.WaitingQueueTokenGenerateInfo;
import io.queuemanagement.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/03
 */
public record WaitingQueueTokenGenerationResponse(
	String userId,
	String tokenValue,
	int position,
	LocalDateTime validUntil,
	QueueStatus status
) {

	public static WaitingQueueTokenGenerationResponse from(WaitingQueueTokenGenerateInfo token) {
		return ObjectMapperBasedVoMapper.convert(token, WaitingQueueTokenGenerationResponse.class);
	}
}
