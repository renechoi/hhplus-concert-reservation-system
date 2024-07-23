package io.queuemanagement.api.business.dto.inport;

import static io.queuemanagement.api.business.domainmodel.QueueStatus.*;
import static io.queuemanagement.api.business.dto.inport.DateSearchCommand.DateSearchCondition.*;

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
public class ProcessingQueueTokenSearchCommand extends AbstractCommonRequestInfo implements DateSearchCommand{

	private Long processingQueueTokenId;
	private String userId;
	private String tokenValue;
	private Long position;
	private LocalDateTime validUntil;
	private QueueStatus status;
	private LocalDateTime createdAt;

	private DateSearchCondition dateSearchCondition;
	private DateSearchTarget dateSearchTarget;


	public static ProcessingQueueTokenSearchCommand onProcessing(String queueToken, String userId) {
		return ProcessingQueueTokenSearchCommand.builder().tokenValue(queueToken).userId(userId).status(PROCESSING).build();
	}

	public static ProcessingQueueTokenSearchCommand onActiveProcessing(String userId) {
		return ProcessingQueueTokenSearchCommand.builder().userId(userId).status(PROCESSING).build();
	}

	public static ProcessingQueueTokenSearchCommand onProcessing(String queueToken) {
		return ProcessingQueueTokenSearchCommand.builder().tokenValue(queueToken).status(PROCESSING).build();
	}

	public static ProcessingQueueTokenSearchCommand onProcessingTokensExpiring(LocalDateTime valiadUntil) {
		return ProcessingQueueTokenSearchCommand.builder().status(PROCESSING).validUntil(valiadUntil).dateSearchTarget(DateSearchTarget.VALID_UNTIL).dateSearchCondition(BEFORE).build();
	}
}


