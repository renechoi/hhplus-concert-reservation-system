package io.apiorchestrationservice.api.application.dto.response;

import java.math.BigDecimal;

import io.apiorchestrationservice.api.business.domainmodel.PaymentStatus;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
public record PaymentResponse(
	Long paymentId,
	Long userId,
	Long reservationId,
	BigDecimal amount,
	PaymentStatus status,
	String message
) {

	// Mock 성공 응답 생성 메서드
	public static PaymentResponse createMockSuccessPaymentResponse() {
		return new PaymentResponse(
			1L,
			1L,
			1L,
			new BigDecimal("100.00"),
			PaymentStatus.CONFIRMED,
			"결제가 정상 처리되었고, 좌석도 예약 완료"
		);
	}

	// Mock 실패 응답 생성 메서드
	public static PaymentResponse createMockFailPaymentResponse() {
		return new PaymentResponse(
			null,
			1L,
			1L,
			new BigDecimal("100.00"),
			PaymentStatus.CANCELLED,
			"잔액이 충분하지 않습니다."
		);
	}
}