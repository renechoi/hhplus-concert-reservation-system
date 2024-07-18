package io.queuemanagement.api.business.domainmodel;

import java.time.LocalDateTime;
import java.util.UUID;

import io.queuemanagement.api.business.dto.inport.WaitingQueueTokenGenerateCommand;
import io.queuemanagement.common.annotation.DomainModel;
import io.queuemanagement.util.YmlLoader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@DomainModel
public class WaitingQueueToken implements DomainRecordable{
	private Long waitingQueueTokenId;
	private String userId;
	private String tokenValue;
	private Long position;
	private LocalDateTime validUntil;
	private QueueStatus status;

	private LocalDateTime requestAt;

	public static WaitingQueueToken createToken(WaitingQueueTokenGenerateCommand command) {
		return WaitingQueueToken.builder()
			.userId(command.getUserId())
			.tokenValue(UUID.randomUUID().toString())
			.status(QueueStatus.WAITING)
			.requestAt(command.getRequestAt())
			.build();
	}

	public WaitingQueueToken withValidUntil(int seconds) {
		this.validUntil = LocalDateTime.now().plusSeconds(seconds);
		return this;
	}


	public WaitingQueueToken withPositionValue(Long position) {
		this.position= position;
		return this;
	}

	public WaitingQueueToken withProccessing() {
		this.status = QueueStatus.PROCESSING;
		return this;
	}

	public WaitingQueueToken withCompleted() {
		this.status = QueueStatus.COMPLETED;
		return this;
	}

	public WaitingQueueToken withExpired() {
		this.status = QueueStatus.EXPIRED;
		return this;
	}
}
