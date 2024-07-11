package io.paymentservice.testhelpers.contextholder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.paymentservice.api.balance.presentation.dto.request.UserBalanceChargeRequest;
import io.paymentservice.api.balance.presentation.dto.request.UserBalanceUseRequest;
import io.paymentservice.api.balance.presentation.dto.response.BalanceTransactionResponses;
import io.paymentservice.api.balance.presentation.dto.response.UserBalanceChargeResponse;
import io.paymentservice.api.balance.presentation.dto.response.UserBalanceSearchResponse;
import io.paymentservice.api.balance.presentation.dto.response.UserBalanceUseResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
public class UserBalanceContextHolder {
	private static final ConcurrentHashMap<Long, UserBalanceChargeRequest> chargeRequestMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, UserBalanceUseRequest> useRequestMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, UserBalanceSearchResponse> searchResponseMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, UserBalanceChargeResponse> chargeResponseMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, UserBalanceUseResponse> useResponseMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, BalanceTransactionResponses> transactionResponsesMap = new ConcurrentHashMap<>();
	private static final AtomicReference<Long> mostRecentUserId = new AtomicReference<>();

	public static void initFields() {
		chargeRequestMap.clear();
		useRequestMap.clear();
		searchResponseMap.clear();
		chargeResponseMap.clear();
		useResponseMap.clear();
		transactionResponsesMap.clear();
		mostRecentUserId.set(null);
	}

	public static void putChargeRequest(Long userId, UserBalanceChargeRequest request) {
		chargeRequestMap.put(userId, request);
		mostRecentUserId.set(userId);
	}

	public static UserBalanceChargeRequest getChargeRequest(Long userId) {
		return chargeRequestMap.get(userId);
	}

	public static void putUseRequest(Long userId, UserBalanceUseRequest request) {
		useRequestMap.put(userId, request);
		mostRecentUserId.set(userId);
	}

	public static UserBalanceUseRequest getUseRequest(Long userId) {
		return useRequestMap.get(userId);
	}

	public static void putSearchResponse(Long userId, UserBalanceSearchResponse response) {
		searchResponseMap.put(userId, response);
		mostRecentUserId.set(userId);
	}

	public static UserBalanceSearchResponse getSearchResponse(Long userId) {
		return searchResponseMap.get(userId);
	}

	public static void putChargeResponse(Long userId, UserBalanceChargeResponse response) {
		chargeResponseMap.put(userId, response);
		mostRecentUserId.set(userId);
	}

	public static UserBalanceChargeResponse getChargeResponse(Long userId) {
		return chargeResponseMap.get(userId);
	}

	public static void putUseResponse(Long userId, UserBalanceUseResponse response) {
		useResponseMap.put(userId, response);
		mostRecentUserId.set(userId);
	}

	public static void putUseResponse(UserBalanceUseResponse response) {
		useResponseMap.put(response.userId(), response);
		mostRecentUserId.set(response.userId());
	}

	public static UserBalanceUseResponse getUseResponse(Long userId) {
		return useResponseMap.get(userId);
	}

	public static void putTransactionResponses(Long userId, BalanceTransactionResponses response) {
		transactionResponsesMap.put(userId, response);
		mostRecentUserId.set(userId);
	}

	public static BalanceTransactionResponses getTransactionResponses(Long userId) {
		return transactionResponsesMap.get(userId);
	}

	public static Long getMostRecentUserId() {
		return mostRecentUserId.get();
	}

	public static UserBalanceChargeResponse getMostRecentChargeResponse() {
		Long recentUserId = mostRecentUserId.get();
		return recentUserId != null ? getChargeResponse(recentUserId) : null;
	}

	public static UserBalanceUseResponse getMostRecentUseResponse() {
		Long recentUserId = mostRecentUserId.get();
		return recentUserId != null ? getUseResponse(recentUserId) : null;
	}
}
