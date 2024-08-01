package io.queuemanagement.api.business.domainmodel;

import static io.queuemanagement.common.token.QueueTokenGenerator.*;
import static io.queuemanagement.util.YmlLoader.*;

import java.time.LocalDateTime;

import io.queuemanagement.api.business.dto.inport.WaitingQueueTokenGenerateCommand;
import io.queuemanagement.common.annotation.DomainModel;
import io.queuemanagement.common.mapper.ObjectMapperBasedVoMapper;
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
	private Boolean isEmpty;

	public static WaitingQueueToken valueOf(String tokenValue) {
		return WaitingQueueToken.builder().tokenValue(tokenValue).build();
	}

	public static WaitingQueueToken empty() {
		return WaitingQueueToken.builder().isEmpty(true).build();
	}

	public WaitingQueueToken init(WaitingQueueTokenCounter waitingQueueTokenCounter) {
		return withValidUntil(getWaitingTokenExpiryInSeconds()).withPositionValue(waitingQueueTokenCounter.getCount());
	}


	public static WaitingQueueToken createToken(WaitingQueueTokenGenerateCommand command) {
		return WaitingQueueToken.builder()
			.userId(command.getUserId())
			.tokenValue(generateToken(command.getUserId()))
			.status(QueueStatus.WAITING)
			.requestAt(command.getRequestAt())
			.build();
	}


	public static WaitingQueueToken tokenWithPosition(String userId, long position) {
		return WaitingQueueToken.builder()
			.userId(userId)
			.tokenValue(generateToken(userId))
			.position(position)
			.status(QueueStatus.WAITING)
			.build();
	}



	public static WaitingQueueToken fail(WaitingQueueToken originalToken) {
		return WaitingQueueToken.builder()
			.waitingQueueTokenId(originalToken.getWaitingQueueTokenId())
			.userId(originalToken.getUserId())
			.tokenValue(originalToken.getTokenValue())
			.status(QueueStatus.FAIL)
			.requestAt(originalToken.getRequestAt())
			.build();
	}

	public  ProcessingQueueToken toProcessing(){
		return ObjectMapperBasedVoMapper.convert(this, ProcessingQueueToken.class).withProcessing();
	}


	public WaitingQueueToken withValidUntil(int seconds) {
		this.validUntil = LocalDateTime.now().plusSeconds(seconds);
		return this;
	}


	public WaitingQueueToken withPositionValue(Long position) {
		this.position= position;
		return this;
	}

	public WaitingQueueToken proccessed() {
		this.status = QueueStatus.PROCESSING;
		return this;
	}

	public WaitingQueueToken complete() {
		this.status = QueueStatus.COMPLETED;
		return this;
	}

	public WaitingQueueToken expire() {
		this.status = QueueStatus.EXPIRED;
		return this;
	}

	public WaitingQueueToken calculatePosition(Long minTokenId) {
		Long position = this.waitingQueueTokenId - minTokenId + 1;
		return this.withPositionValue(position);
	}

	
}
