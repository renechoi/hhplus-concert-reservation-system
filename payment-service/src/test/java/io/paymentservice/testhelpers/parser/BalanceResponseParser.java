package io.paymentservice.testhelpers.parser;

import io.paymentservice.api.balance.interfaces.dto.response.BalanceTransactionResponses;
import io.paymentservice.api.balance.interfaces.dto.response.BalanceChargeResponse;
import io.paymentservice.api.balance.interfaces.dto.response.BalanceSearchResponse;
import io.paymentservice.api.balance.interfaces.dto.response.BalanceUseResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
public class BalanceResponseParser {
	public static BalanceSearchResponse parseBalanceSearchResponse(ExtractableResponse<Response> response) {
		return response.as(BalanceSearchResponse.class);
	}

	public static BalanceChargeResponse parseBalanceChargeResponse(ExtractableResponse<Response> response) {
		return response.as(BalanceChargeResponse.class);
	}

	public static BalanceUseResponse parseBalanceUseResponse(ExtractableResponse<Response> response) {
		return response.as(BalanceUseResponse.class);
	}

	public static BalanceTransactionResponses parseBalanceTransactionResponses(ExtractableResponse<Response> response) {
		return response.as(BalanceTransactionResponses.class);
	}
}
