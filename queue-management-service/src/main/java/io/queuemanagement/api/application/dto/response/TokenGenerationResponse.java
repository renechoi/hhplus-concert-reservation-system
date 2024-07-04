package io.queuemanagement.api.application.dto.response;

import java.time.LocalDateTime;

import io.queuemanagement.api.business.domainmodel.WaitingQueueStatus;
import io.queuemanagement.api.infrastructure.entity.WaitingQueueTokenEntity;
import io.queuemanagement.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/03
 */
public record TokenGenerationResponse(
	String userId,
	String tokenValue,
	LocalDateTime remainingTime,
	int position,
	LocalDateTime validUntil,
	WaitingQueueStatus status
) {

	public static TokenGenerationResponse from(WaitingQueueTokenEntity token) {
		return ObjectMapperBasedVoMapper.convert(token, TokenGenerationResponse.class);
	}
}
