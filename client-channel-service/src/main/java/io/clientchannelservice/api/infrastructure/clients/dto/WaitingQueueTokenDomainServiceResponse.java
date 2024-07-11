package io.clientchannelservice.api.infrastructure.clients.dto;

import java.time.LocalDateTime;

import io.clientchannelservice.api.business.domainmodel.QueueStatus;
import io.clientchannelservice.api.business.dto.outport.WaitingQueueTokenPollingInfo;
import io.clientchannelservice.common.mapper.ObjectMapperBasedVoMapper;
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

	public WaitingQueueTokenPollingInfo toInfo() {
		return ObjectMapperBasedVoMapper.convert(this, WaitingQueueTokenPollingInfo.class);
	}
}
