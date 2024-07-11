package io.apiorchestrationservice.testhelpers.feignexecutor.dto;

import java.time.LocalDateTime;

import io.apiorchestrationservice.api.business.domainmodel.QueueStatus;

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


}
