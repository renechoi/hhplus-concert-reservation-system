package io.paymentservice.testhelpers.contextholder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Component;

import io.paymentservice.api.balance.interfaces.dto.request.BalanceChargeRequest;
import io.paymentservice.api.balance.interfaces.dto.request.BalanceUseRequest;
import io.paymentservice.api.balance.interfaces.dto.response.BalanceTransactionResponses;
import io.paymentservice.api.balance.interfaces.dto.response.BalanceChargeResponse;
import io.paymentservice.api.balance.interfaces.dto.response.BalanceSearchResponse;
import io.paymentservice.api.balance.interfaces.dto.response.BalanceUseResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
@Component
public class BalanceContextHolder implements TestDtoContextHolder{
	private static final ConcurrentHashMap<Long, BalanceChargeRequest> chargeRequestMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, BalanceUseRequest> useRequestMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, BalanceSearchResponse> searchResponseMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, BalanceChargeResponse> chargeResponseMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, BalanceUseResponse> useResponseMap = new ConcurrentHashMap<>();
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

	public static void putChargeRequest(Long userId, BalanceChargeRequest request) {
		chargeRequestMap.put(userId, request);
		mostRecentUserId.set(userId);
	}

	public static BalanceChargeRequest getChargeRequest(Long userId) {
		return chargeRequestMap.get(userId);
	}

	public static void putUseRequest(Long userId, BalanceUseRequest request) {
		useRequestMap.put(userId, request);
		mostRecentUserId.set(userId);
	}

	public static BalanceUseRequest getUseRequest(Long userId) {
		return useRequestMap.get(userId);
	}

	public static void putSearchResponse(Long userId, BalanceSearchResponse response) {
		searchResponseMap.put(userId, response);
		mostRecentUserId.set(userId);
	}

	public static BalanceSearchResponse getSearchResponse(Long userId) {
		return searchResponseMap.get(userId);
	}

	public static void putChargeResponse(Long userId, BalanceChargeResponse response) {
		chargeResponseMap.put(userId, response);
		mostRecentUserId.set(userId);
	}

	public static BalanceChargeResponse getChargeResponse(Long userId) {
		return chargeResponseMap.get(userId);
	}

	public static void putUseResponse(Long userId, BalanceUseResponse response) {
		useResponseMap.put(userId, response);
		mostRecentUserId.set(userId);
	}

	public static void putUseResponse(BalanceUseResponse response) {
		useResponseMap.put(response.userId(), response);
		mostRecentUserId.set(response.userId());
	}

	public static BalanceUseResponse getUseResponse(Long userId) {
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

	public static BalanceChargeResponse getMostRecentChargeResponse() {
		Long recentUserId = mostRecentUserId.get();
		return recentUserId != null ? getChargeResponse(recentUserId) : null;
	}

	public static BalanceUseResponse getMostRecentUseResponse() {
		Long recentUserId = mostRecentUserId.get();
		return recentUserId != null ? getUseResponse(recentUserId) : null;
	}
}
