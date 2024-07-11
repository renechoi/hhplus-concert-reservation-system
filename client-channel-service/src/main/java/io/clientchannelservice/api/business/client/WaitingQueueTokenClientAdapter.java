package io.clientchannelservice.api.business.client;

import java.util.Optional;

import io.clientchannelservice.api.business.dto.outport.WaitingQueueTokenPollingInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface WaitingQueueTokenClientAdapter {
	Optional<WaitingQueueTokenPollingInfo> retrieveToken(String userId);
}
