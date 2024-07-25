package io.apiorchestrationservice.testhelpers.contextholder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.apiorchestrationservice.api.application.dto.request.PaymentProcessRequest;
import io.apiorchestrationservice.api.application.dto.request.UserBalanceChargeRequest;
import io.apiorchestrationservice.api.application.dto.response.PaymentResponse;
import io.apiorchestrationservice.api.application.dto.response.UserBalanceChargeResponse;
import io.apiorchestrationservice.api.application.dto.response.UserBalanceSearchResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */

public class BalanceAndPaymentContextHolder implements TestDtoContextHolder {


	private static final ConcurrentHashMap<Long, UserBalanceChargeRequest> chargeRequestMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, PaymentProcessRequest> paymentRequestMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, UserBalanceSearchResponse> searchResponseMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, UserBalanceChargeResponse> chargeResponseMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, PaymentResponse> paymentResponseMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, UserBalanceSearchResponse> initialBalanceMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, UserBalanceChargeResponse> chargedBalanceMap = new ConcurrentHashMap<>();
	private static final AtomicReference<Long> mostRecentUserId = new AtomicReference<>();

	public static void initFields() {
		chargeRequestMap.clear();
		paymentRequestMap.clear();
		searchResponseMap.clear();
		chargeResponseMap.clear();
		paymentResponseMap.clear();
		initialBalanceMap.clear();
		chargedBalanceMap.clear();
		mostRecentUserId.set(null);
	}





	public static void putChargeRequest(Long userId, UserBalanceChargeRequest request) {
		chargeRequestMap.put(userId, request);
		mostRecentUserId.set(userId);
	}

	public static UserBalanceChargeRequest getChargeRequest(Long userId) {
		return chargeRequestMap.get(userId);
	}

	public static void putPaymentRequest(Long userId, PaymentProcessRequest request) {
		paymentRequestMap.put(userId, request);
		mostRecentUserId.set(userId);
	}

	public static void putPaymentRequest(PaymentProcessRequest request) {
		putPaymentRequest(request.getUserId(), request);
	}

	public static PaymentProcessRequest getPaymentRequest(Long userId) {
		return paymentRequestMap.get(userId);
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

	public static void putPaymentResponse(Long userId, PaymentResponse response) {
		paymentResponseMap.put(userId, response);
		mostRecentUserId.set(userId);
	}

	public static void putPaymentResponse(PaymentResponse response) {
		putPaymentResponse(response.userId(), response);
	}

	public static PaymentResponse getPaymentResponse(Long userId) {
		return paymentResponseMap.get(userId);
	}

	public static Long getMostRecentUserId() {
		return mostRecentUserId.get();
	}

	public static UserBalanceChargeResponse getMostRecentChargeResponse() {
		Long recentUserId = mostRecentUserId.get();
		return recentUserId != null ? getChargeResponse(recentUserId) : null;
	}

	public static PaymentResponse getMostRecentPaymentResponse() {
		Long recentUserId = mostRecentUserId.get();
		return recentUserId != null ? getPaymentResponse(recentUserId) : null;
	}

	public static UserBalanceChargeRequest getMostRecentChargeRequest() {
		Long recentUserId = mostRecentUserId.get();
		return recentUserId != null ? getChargeRequest(recentUserId) : null;
	}

	public static PaymentProcessRequest getMostRecentPaymentRequest() {
		Long recentUserId = mostRecentUserId.get();
		return recentUserId != null ? getPaymentRequest(recentUserId) : null;
	}

	public static void putInitialBalance(Long userId, UserBalanceSearchResponse response) {
		initialBalanceMap.put(userId, response);
		mostRecentUserId.set(userId);
	}

	public static UserBalanceSearchResponse getInitialBalance(Long userId) {
		return initialBalanceMap.get(userId);
	}

	public static void putChargedBalance(Long userId, UserBalanceChargeResponse response) {
		chargedBalanceMap.put(userId, response);
		mostRecentUserId.set(userId);
	}

	public static UserBalanceChargeResponse getChargedBalance(Long userId) {
		return chargedBalanceMap.get(userId);
	}
}