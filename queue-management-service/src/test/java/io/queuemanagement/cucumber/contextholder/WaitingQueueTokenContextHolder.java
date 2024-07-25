package io.queuemanagement.cucumber.contextholder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import io.queuemanagement.api.application.dto.request.WaitingQueueTokenGenerateRequest;
import io.queuemanagement.api.application.dto.response.WaitingQueueTokenGeneralResponse;
import io.queuemanagement.api.application.dto.response.WaitingQueueTokenGenerationResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */

public class WaitingQueueTokenContextHolder implements TestDtoContextHolder {
	private static final ConcurrentHashMap<String, WaitingQueueTokenGenerateRequest> requestMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, WaitingQueueTokenGenerationResponse> responseMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, WaitingQueueTokenGeneralResponse> generalResponseMap = new ConcurrentHashMap<>();
	private static final AtomicReference<String> mostRecentRequestUserId = new AtomicReference<>();
	private static final AtomicReference<String> mostRecentResponseUserId = new AtomicReference<>();

	public static void initFields() {
		requestMap.clear();
		responseMap.clear();
		generalResponseMap.clear();
		mostRecentRequestUserId.set(null);
		mostRecentResponseUserId.set(null);
	}

	public static void putWaitingQueueTokenGenerateRequest(WaitingQueueTokenGenerateRequest request) {
		requestMap.put(request.getUserId(), request);
		mostRecentRequestUserId.set(request.getUserId());
	}

	public static WaitingQueueTokenGenerateRequest getWaitingQueueTokenGenerateRequest(String userId) {
		return requestMap.get(userId);
	}

	public static void putWaitingQueueTokenGenerationResponse(WaitingQueueTokenGenerationResponse response) {
		responseMap.put(response.userId(), response);
		mostRecentResponseUserId.set(response.userId());
	}

	public static WaitingQueueTokenGenerationResponse getWaitingQueueTokenGenerationResponse(String userId) {
		return responseMap.get(userId);
	}

	public static void putWaitingQueueTokenGeneralResponse(WaitingQueueTokenGeneralResponse response) {
		generalResponseMap.put(response.userId(), response);
		mostRecentResponseUserId.set(response.userId());
	}

	public static WaitingQueueTokenGeneralResponse getWaitingQueueTokenGeneralResponse(String userId) {
		return generalResponseMap.get(userId);
	}

	public static WaitingQueueTokenGenerateRequest getMostRecentGenerateRequest() {
		String recentUserId = mostRecentRequestUserId.get();
		return recentUserId != null ? getWaitingQueueTokenGenerateRequest(recentUserId) : null;
	}

	public static WaitingQueueTokenGenerationResponse getMostRecentGenerationResponse() {
		String recentUserId = mostRecentResponseUserId.get();
		return recentUserId != null ? getWaitingQueueTokenGenerationResponse(recentUserId) : null;
	}

	public static WaitingQueueTokenGeneralResponse getMostRecentGeneralResponse() {
		String recentUserId = mostRecentResponseUserId.get();
		return recentUserId != null ? getWaitingQueueTokenGeneralResponse(recentUserId) : null;
	}

	public static List<WaitingQueueTokenGenerationResponse> getAllGenerationResponses() {
		return new ArrayList<>(responseMap.values());
	}
}