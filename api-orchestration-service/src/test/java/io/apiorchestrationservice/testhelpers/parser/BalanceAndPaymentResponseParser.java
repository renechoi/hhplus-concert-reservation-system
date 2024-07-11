package io.apiorchestrationservice.testhelpers.parser;

import io.apiorchestrationservice.api.application.dto.response.PaymentResponse;
import io.apiorchestrationservice.api.application.dto.response.UserBalanceChargeResponse;
import io.apiorchestrationservice.api.application.dto.response.UserBalanceSearchResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */

public class BalanceAndPaymentResponseParser {

	public static UserBalanceSearchResponse parseUserBalanceSearchResponse(ExtractableResponse<Response> response) {
		return response.as(UserBalanceSearchResponse.class);
	}

	public static UserBalanceChargeResponse parseUserBalanceChargeResponse(ExtractableResponse<Response> response) {
		return response.as(UserBalanceChargeResponse.class);
	}

	public static PaymentResponse parsePaymentResponse(ExtractableResponse<Response> response) {
		return response.as(PaymentResponse.class);
	}
}