package io.queuemanagement.api.business.dto.inport;

import java.time.LocalDateTime;

import io.queuemanagement.api.business.domainmodel.QueueStatus;
import io.queuemanagement.api.business.dto.common.AbstractCommonRequestInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessingQueueTokenSearchCommand extends AbstractCommonRequestInfo {

	private Long processingQueueTokenId;
	private String userId;
	private String tokenValue;
	private Long position;
	private LocalDateTime validUntil;
	private QueueStatus status;
	private LocalDateTime createdAt;

	public static ProcessingQueueTokenSearchCommand createSearchConditionByTokenAndUserIdAndStatus(String queueToken, String userId, QueueStatus queueStatus) {
		return ProcessingQueueTokenSearchCommand.builder().tokenValue(queueToken).userId(userId).status(queueStatus).build();
	}

	public static ProcessingQueueTokenSearchCommand createSearchConditionByUserIdAndStatus(String userId, QueueStatus queueStatus) {
		return ProcessingQueueTokenSearchCommand.builder().userId(userId).status(queueStatus).build();
	}

	public static ProcessingQueueTokenSearchCommand createSearchConditionByTokenAndStatus(String queueToken,  QueueStatus queueStatus) {
		return ProcessingQueueTokenSearchCommand.builder().tokenValue(queueToken).status(queueStatus).build();
	}
}
