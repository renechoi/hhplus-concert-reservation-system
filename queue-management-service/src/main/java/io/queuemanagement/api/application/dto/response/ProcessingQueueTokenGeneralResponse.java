package io.queuemanagement.api.application.dto.response;

import java.time.LocalDateTime;

import io.queuemanagement.api.business.domainmodel.QueueStatus;
import io.queuemanagement.api.business.dto.outport.ProcessingQueueTokenGeneralInfo;
import io.queuemanagement.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public record ProcessingQueueTokenGeneralResponse(
	Long processingQueueTokenId,
	String userId,
	String tokenValue,
	LocalDateTime validUntil,
	QueueStatus status
) {

	public static ProcessingQueueTokenGeneralResponse from(ProcessingQueueTokenGeneralInfo info) {
		return ObjectMapperBasedVoMapper.convert(info, ProcessingQueueTokenGeneralResponse.class);
	}
}
