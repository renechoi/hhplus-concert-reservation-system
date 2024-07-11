package io.queuemanagement.api.business.dto.outport;

import java.time.LocalDateTime;

import io.queuemanagement.api.business.domainmodel.ProcessingQueueToken;
import io.queuemanagement.api.business.domainmodel.QueueStatus;
import io.queuemanagement.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public record ProcessingQueueTokenGeneralInfo(
	 Long processingQueueTokenId,
	 String userId,
	 String tokenValue,
	 LocalDateTime validUntil,
	 QueueStatus status

) {
	public static ProcessingQueueTokenGeneralInfo from(ProcessingQueueToken processingQueueToken) {
		return ObjectMapperBasedVoMapper.convert(processingQueueToken, ProcessingQueueTokenGeneralInfo.class);
	}
}
