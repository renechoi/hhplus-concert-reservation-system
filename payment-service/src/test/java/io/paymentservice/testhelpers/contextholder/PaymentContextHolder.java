package io.paymentservice.testhelpers.contextholder;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Component;

import io.paymentservice.api.payment.interfaces.dto.request.PaymentRequest;
import io.paymentservice.api.payment.interfaces.dto.response.PaymentResponse;
import io.paymentservice.api.payment.interfaces.dto.response.PaymentResponses;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
@Component
public class PaymentContextHolder implements TestDtoContextHolder {

	private static final ConcurrentHashMap<Long, PaymentRequest> paymentRequestMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, PaymentResponse> paymentResponseMap = new ConcurrentHashMap<>();
	private static final AtomicReference<Long> mostRecentTransactionId = new AtomicReference<>();
	private static final ConcurrentHashMap<Long, PaymentResponses> paymentHistoryMap = new ConcurrentHashMap<>();
	private static final AtomicReference<List<PaymentResponse>> concurrentPaymentResponses = new AtomicReference<>();

	public static void initFields() {
		paymentRequestMap.clear();
		paymentResponseMap.clear();
		mostRecentTransactionId.set(null);
		paymentHistoryMap.clear();
		concurrentPaymentResponses.set(null);
	}

	public static void putPaymentRequest(Long userId, PaymentRequest request) {
		paymentRequestMap.put(userId, request);
		mostRecentTransactionId.set(userId);
	}

	public static void putPaymentRequest(PaymentRequest request) {
		putPaymentRequest(request.getUserId(), request);
	}

	public static PaymentRequest getPaymentRequest(Long transactionId) {
		return paymentRequestMap.get(transactionId);
	}

	public static void putPaymentResponse(Long transactionId, PaymentResponse response) {
		paymentResponseMap.put(transactionId, response);
		mostRecentTransactionId.set(transactionId);
	}

	public static void putPaymentResponse(PaymentResponse response) {
		putPaymentResponse(response.transactionId() == null ? response.userId() : response.transactionId(), response);
	}

	public static PaymentResponse getPaymentResponse(Long transactionId) {
		return paymentResponseMap.get(transactionId);
	}

	public static Long getMostRecentTransactionId() {
		return mostRecentTransactionId.get();
	}

	public static PaymentResponse getMostRecentPaymentResponse() {
		Long recentTransactionId = mostRecentTransactionId.get();
		return recentTransactionId != null ? getPaymentResponse(recentTransactionId) : null;
	}

	public static PaymentRequest getMostRecentPaymentRequest() {
		Long userId = mostRecentTransactionId.get();
		return userId != null ? getPaymentRequest(userId) : null;
	}

	public static void putPaymentHistory(Long userId, PaymentResponses paymentResponses) {
		paymentHistoryMap.put(userId, paymentResponses);
		mostRecentTransactionId.set(userId);
	}

	public static PaymentResponses getPaymentHistory(Long userId) {
		return paymentHistoryMap.get(userId);
	}

	public static PaymentResponses getMostRecentPaymentHistory() {
		Long userId = mostRecentTransactionId.get();
		return userId != null ? getPaymentHistory(userId) : null;
	}

	public static void setConcurrentPaymentResponses(List<PaymentResponse> responses) {
		concurrentPaymentResponses.set(responses);
	}

	public static List<PaymentResponse> getConcurrentPaymentResponses() {
		return concurrentPaymentResponses.get();
	}
}
