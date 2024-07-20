package io.paymentservice.testhelpers.parser;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.paymentservice.api.payment.interfaces.dto.response.PaymentResponse;
import io.paymentservice.api.payment.interfaces.dto.response.PaymentResponses;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.SneakyThrows;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public class PaymentResponseParser {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static PaymentResponse parsePaymentResponse(ExtractableResponse<Response> response) {
		return response.as(PaymentResponse.class);
	}

	@SneakyThrows
	public static PaymentResponse parsePaymentResponseAsApiResponse(ExtractableResponse<Response> response) {
		return objectMapper.treeToValue(objectMapper.readTree(response.body().asString()).get("body"), PaymentResponse.class);
	}

	public static PaymentResponses parsePaymentHistoryResponse(ExtractableResponse<Response> response) {
		return response.as(PaymentResponses.class);
	}

}