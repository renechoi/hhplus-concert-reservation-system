package io.apiorchestrationservice.api.infrastructure.clients.queuemanagement.dto;

import java.time.LocalDateTime;

import io.apiorchestrationservice.api.business.domainmodel.QueueStatus;
import io.apiorchestrationservice.api.business.dto.outport.ProcessingQueueTokenInfo;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;
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
public class ProcessingQueueTokenDomainServiceResponse {
	private Long processingQueueTokenId;
	private String userId;
	private String tokenValue;
	private Long position;
	private LocalDateTime validUntil;
	private QueueStatus status;

	public ProcessingQueueTokenInfo toInfo() {
		return ObjectMapperBasedVoMapper.convert(this, ProcessingQueueTokenInfo.class);
	}
}
