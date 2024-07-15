package io.queuemanagement.api.application.dto.response;

import java.time.LocalDateTime;

import io.queuemanagement.api.business.domainmodel.QueueStatus;
import io.queuemanagement.api.business.dto.outport.WaitingQueueTokenGeneralInfo;
import io.queuemanagement.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public record WaitingQueueTokenGeneralResponse(
	Long waitingQueueTokenId,
	String userId,
	String tokenValue,
	Long position,
	LocalDateTime validUntil,
	QueueStatus status, // status가 Processing이면 처리열에 존재하는 것으로 판단
	LocalDateTime requestAt
) {

	public static WaitingQueueTokenGeneralResponse from(WaitingQueueTokenGeneralInfo info) {
		return ObjectMapperBasedVoMapper.convert(info, WaitingQueueTokenGeneralResponse.class);
	}
}
