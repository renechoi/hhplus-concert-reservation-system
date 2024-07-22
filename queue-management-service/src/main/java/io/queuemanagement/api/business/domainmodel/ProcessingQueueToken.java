package io.queuemanagement.api.business.domainmodel;

import java.time.LocalDateTime;

import io.queuemanagement.common.annotation.DomainModel;
import io.queuemanagement.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@DomainModel
@Slf4j
public class ProcessingQueueToken  {
	private Long processingQueueTokenId;
	private String userId;
	private String tokenValue;
	private LocalDateTime validUntil;
	private QueueStatus status;

	public static ProcessingQueueToken createProcessingQueueTokenByWaitingQueueToken(WaitingQueueToken token) {
		return ObjectMapperBasedVoMapper.convert(token, ProcessingQueueToken.class).withProcessing();
	}

	public ProcessingQueueToken withProcessing() {
		this.status = QueueStatus.PROCESSING;
		return this;
	}

	public ProcessingQueueToken withCompleted() {
		this.status = QueueStatus.COMPLETED;
		return this;
	}

	public ProcessingQueueToken withExpired() {
		this.status = QueueStatus.EXPIRED;
		return this;
	}

	public boolean isNoMoreActive() {
		return this.status!=null && (this.status == QueueStatus.COMPLETED || this.status == QueueStatus.EXPIRED);
	}
}
