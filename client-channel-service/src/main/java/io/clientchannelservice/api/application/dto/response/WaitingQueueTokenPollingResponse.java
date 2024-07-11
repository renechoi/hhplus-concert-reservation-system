package io.clientchannelservice.api.application.dto.response;

import java.time.LocalDateTime;

import io.clientchannelservice.api.business.domainmodel.QueueStatus;
import io.clientchannelservice.api.business.dto.outport.WaitingQueueTokenPollingInfo;
import io.clientchannelservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public record WaitingQueueTokenPollingResponse(
	Long waitingQueueTokenId,
	String userId,
	String tokenValue,
	Long position,
	LocalDateTime validUntil,
	QueueStatus status,
	LocalDateTime requestAt
) {
	public static WaitingQueueTokenPollingResponse from(WaitingQueueTokenPollingInfo info) {
		return ObjectMapperBasedVoMapper.convert(info, WaitingQueueTokenPollingResponse.class);
	}
}
