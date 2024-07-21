package io.queuemanagement.api.business.dto.inport;

import static io.queuemanagement.api.business.dto.inport.DateSearchCommand.DateSearchCondition.*;
import static io.queuemanagement.api.business.dto.inport.DateSearchCommand.DateSearchTarget.*;

import java.time.LocalDateTime;
import java.util.List;

import io.queuemanagement.api.business.domainmodel.QueueStatus;
import io.queuemanagement.api.business.dto.common.AbstractCommonRequestInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WaitingQueueTokenSearchCommand extends AbstractCommonRequestInfo implements DateSearchCommand{
	private Long waitingQueueTokenId;
	private String userId;
	private String tokenValue;
	private Long position;
	private LocalDateTime validUntil;
	private QueueStatus status;
	private List<QueueStatus> statuses; // in 쿼리를 위한 복수 상태 파라미터

	private LocalDateTime createdAt;

	private DateSearchCondition dateSearchCondition;
	private DateSearchTarget dateSearchTarget;

	private Integer top;
	private String orderBy;
	private String orderDirection;




	public static WaitingQueueTokenSearchCommandBuilder active() {
		return WaitingQueueTokenSearchCommand.builder().validUntil(LocalDateTime.now()).dateSearchCondition(AFTER).dateSearchTarget(VALID_UNTIL);
	}


	public static WaitingQueueTokenSearchCommand searchByUserIdAndOrderByRequestAtAsc(String userId) {
		return WaitingQueueTokenSearchCommand.builder()
			.userId(userId)
			.orderBy("requestAt")
			.orderDirection("asc")
			.top(1)
			.build();
	}


	public static WaitingQueueTokenSearchCommand searchActiveByUserIdAndOrderByRequestAtAsc(String userId) {
		return active()
			.userId(userId)
			.orderBy("requestAt")
			.orderDirection("asc")
			.top(1)
			.build();
	}


	public static WaitingQueueTokenSearchCommand searchMinTokenId() {
		return WaitingQueueTokenSearchCommand.builder()
			.orderBy("waitingQueueTokenId")
			.orderDirection("asc")
			.top(1)
			.build();
	}

	public static WaitingQueueTokenSearchCommand searchByStatusAndOrderByRequestAtAsc( QueueStatus queueStatus) {
		return WaitingQueueTokenSearchCommand.builder()
			.status(queueStatus)
			.orderBy("requestAt")
			.orderDirection("asc")
			.build();
	}

	public static WaitingQueueTokenSearchCommand searchConditionByUserIdAndStatus(String userId, QueueStatus queueStatus) {
		return WaitingQueueTokenSearchCommand.builder()
			.userId(userId)
			.status(queueStatus)
			.build();
	}


	public static WaitingQueueTokenSearchCommand searchByStatusesAndValidUntil(List<QueueStatus> statuses, LocalDateTime validUntil, DateSearchCondition dateSearchCondition) {
		return WaitingQueueTokenSearchCommand.builder()
			.statuses(statuses)
			.validUntil(validUntil)
			.dateSearchCondition(dateSearchCondition)
			.dateSearchTarget(DateSearchTarget.VALID_UNTIL)
			.build();
	}

	public boolean isStatuesNotEmpty() {
		return !isStatuesEmpty();
	}
	public boolean isStatuesEmpty() {
		return this.statuses == null || this.statuses.isEmpty();
	}
}
