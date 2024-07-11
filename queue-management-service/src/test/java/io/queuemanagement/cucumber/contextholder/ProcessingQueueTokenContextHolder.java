package io.queuemanagement.cucumber.contextholder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.queuemanagement.api.application.dto.response.ProcessingQueueTokenGeneralResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */

public class ProcessingQueueTokenContextHolder implements TestDtoContextHolder {
	private static final ConcurrentHashMap<String, String> tokenMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, ProcessingQueueTokenGeneralResponse> responseMap = new ConcurrentHashMap<>();
	private static final AtomicReference<String> mostRecentUserId = new AtomicReference<>();

	public static void initFields() {
		tokenMap.clear();
		responseMap.clear();
		mostRecentUserId.set(null);
	}

	public static void putToken(String userId, String tokenValue) {
		tokenMap.put(userId, tokenValue);
		mostRecentUserId.set(userId);
	}

	public static String getToken(String userId) {
		return tokenMap.get(userId);
	}

	public static void putResponse(String userId, ProcessingQueueTokenGeneralResponse response) {
		responseMap.put(userId, response);
		mostRecentUserId.set(userId);
	}

	public static ProcessingQueueTokenGeneralResponse getResponse(String userId) {
		return responseMap.get(userId);
	}

	public static String getMostRecentUserId() {
		return mostRecentUserId.get();
	}

	public static String getMostRecentToken() {
		String recentUserId = mostRecentUserId.get();
		return recentUserId != null ? getToken(recentUserId) : null;
	}

	public static ProcessingQueueTokenGeneralResponse getMostRecentResponse() {
		String recentUserId = mostRecentUserId.get();
		return recentUserId != null ? getResponse(recentUserId) : null;
	}
}
