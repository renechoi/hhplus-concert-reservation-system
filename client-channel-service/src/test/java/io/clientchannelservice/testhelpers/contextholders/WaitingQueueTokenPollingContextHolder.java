package io.clientchannelservice.testhelpers.contextholders;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.clientchannelservice.api.application.dto.response.WaitingQueueTokenPollingResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */

public class WaitingQueueTokenPollingContextHolder implements TestDtoContextHolder {
	private static final ConcurrentHashMap<String, WaitingQueueTokenPollingResponse> pollingResponseMap = new ConcurrentHashMap<>();
	private static final AtomicReference<String> mostRecentRequestUserId = new AtomicReference<>();

	public static void initFields() {
		pollingResponseMap.clear();
		mostRecentRequestUserId.set(null);
	}

	public static void putWaitingQueueTokenPollingResponse(WaitingQueueTokenPollingResponse response) {
		pollingResponseMap.put(response.userId(), response);
		mostRecentRequestUserId.set(response.userId());
	}

	public static WaitingQueueTokenPollingResponse getWaitingQueueTokenPollingResponse(String userId) {
		return pollingResponseMap.get(userId);
	}

	public static WaitingQueueTokenPollingResponse getMostRecentPollingResponse() {
		String recentUserId = mostRecentRequestUserId.get();
		return recentUserId != null ? getWaitingQueueTokenPollingResponse(recentUserId) : null;
	}
}