package io.paymentservice.api.payment.interfaces.controller;

import static io.paymentservice.common.model.CommonApiResponse.*;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.paymentservice.api.payment.business.service.PaymentService;
import io.paymentservice.api.payment.interfaces.dto.request.PaymentRequest;
import io.paymentservice.api.payment.interfaces.dto.response.PaymentResponse;
import io.paymentservice.api.payment.interfaces.dto.response.PaymentResponses;
import io.paymentservice.common.model.CommonApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
@RestController
@RequestMapping("/api/user-balance")
@RequiredArgsConstructor
@Tag(name = "결제 API")
public class PaymentController {


	private final PaymentService paymentService;

	/**
	 * 결제 처리하고 결제 내역을 생성하는 API
	 */
	@PostMapping("/payment")
	public CommonApiResponse<PaymentResponse> processPayment(@RequestBody @Validated PaymentRequest paymentRequest) {
		return OK(PaymentResponse.from(paymentService.processPayment(paymentRequest.toCommand())));
	}

	@PostMapping("/payment/cancel/{transactionId}")
	public CommonApiResponse<PaymentResponse> cancelPayment(@PathVariable Long transactionId) {
		return OK(PaymentResponse.from(paymentService.cancelPayment(transactionId)));
	}


	@GetMapping("/payment/history/{userId}")
	public CommonApiResponse<PaymentResponses> retrievePaymentHistory(@PathVariable Long userId) {
		return OK(PaymentResponses.from(paymentService.retrievePaymentHistory(userId)));
	}

}
