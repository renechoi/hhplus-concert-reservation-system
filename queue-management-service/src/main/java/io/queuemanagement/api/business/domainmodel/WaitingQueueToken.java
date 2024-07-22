package io.queuemanagement.api.business.domainmodel;

import java.time.LocalDateTime;
import java.util.UUID;

import io.queuemanagement.api.business.dto.inport.WaitingQueueTokenGenerateCommand;
import io.queuemanagement.api.business.dto.outport.WaitingQueueTokenGeneralInfo;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenRetrievalRepository;
import io.queuemanagement.common.annotation.DomainModel;
import io.queuemanagement.common.mapper.ObjectMapperBasedVoMapper;
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

	public  ProcessingQueueToken toProcessingToken(){
		return ObjectMapperBasedVoMapper.convert(this, ProcessingQueueToken.class).withProcessing();
	}

	public WaitingQueueToken withValidUntilAndPositionValue(int seconds, Long position) {
		return withValidUntil(seconds).withPositionValue(position);
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

	public WaitingQueueToken calculatePosition(Long minTokenId) {
		Long position = this.waitingQueueTokenId - minTokenId + 1;
		return this.withPositionValue(position);
	}
}
