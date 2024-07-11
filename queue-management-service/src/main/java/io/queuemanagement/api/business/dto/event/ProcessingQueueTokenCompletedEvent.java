package io.queuemanagement.api.business.dto.event;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEvent;

import io.queuemanagement.api.business.domainmodel.QueueStatus;
import lombok.Getter;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@Getter
public class ProcessingQueueTokenCompletedEvent extends ApplicationEvent {
	private Long processingQueueTokenId;
	private String userId;
	private String tokenValue;
	private LocalDateTime validUntil;
	private QueueStatus status;

	public ProcessingQueueTokenCompletedEvent(Object source, Long processingQueueTokenId, String userId, String tokenValue, LocalDateTime validUntil, QueueStatus status) {
		super(source);
		this.processingQueueTokenId = processingQueueTokenId;
		this.userId = userId;
		this.tokenValue = tokenValue;
		this.validUntil = validUntil;
		this.status = status;
	}



	public boolean isNoMoreActive() {
		return this.status!=null && (this.status == QueueStatus.COMPLETED || this.status == QueueStatus.EXPIRED);
	}
}
