package io.apiorchestrationservice.testhelpers.feignexecutor.dto;

import java.time.LocalDateTime;

import io.apiorchestrationservice.api.business.domainmodel.QueueStatus;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public record ProcessingQueueTokenGeneralResponse(
	Long processingQueueTokenId,
	String userId,
	String tokenValue,
	Long position,
	LocalDateTime validUntil,
	QueueStatus status
) {


}
