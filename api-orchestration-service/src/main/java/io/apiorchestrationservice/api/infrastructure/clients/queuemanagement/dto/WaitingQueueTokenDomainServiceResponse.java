package io.apiorchestrationservice.api.infrastructure.clients.queuemanagement.dto;

import java.time.LocalDateTime;

import io.apiorchestrationservice.api.business.domainmodel.QueueStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaitingQueueTokenDomainServiceResponse {

	private Long waitingQueueTokenId;
	private String userId;
	private String tokenValue;
	private Long position;
	private LocalDateTime validUntil;
	private QueueStatus status;
	private LocalDateTime requestAt;


}
