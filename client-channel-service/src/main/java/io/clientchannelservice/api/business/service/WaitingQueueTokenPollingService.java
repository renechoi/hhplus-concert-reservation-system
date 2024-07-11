package io.clientchannelservice.api.business.service;

import io.clientchannelservice.api.business.dto.outport.WaitingQueueTokenPollingInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface WaitingQueueTokenPollingService {
	WaitingQueueTokenPollingInfo retrieveToken(String userId);
}
