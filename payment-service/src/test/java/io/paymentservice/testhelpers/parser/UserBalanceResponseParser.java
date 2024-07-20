package io.paymentservice.testhelpers.parser;

import io.paymentservice.api.balance.interfaces.dto.response.BalanceTransactionResponses;
import io.paymentservice.api.balance.interfaces.dto.response.UserBalanceChargeResponse;
import io.paymentservice.api.balance.interfaces.dto.response.UserBalanceSearchResponse;
import io.paymentservice.api.balance.interfaces.dto.response.UserBalanceUseResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
public class UserBalanceResponseParser {
	public static UserBalanceSearchResponse parseUserBalanceSearchResponse(ExtractableResponse<Response> response) {
		return response.as(UserBalanceSearchResponse.class);
	}

	public static UserBalanceChargeResponse parseUserBalanceChargeResponse(ExtractableResponse<Response> response) {
		return response.as(UserBalanceChargeResponse.class);
	}

	public static UserBalanceUseResponse parseUserBalanceUseResponse(ExtractableResponse<Response> response) {
		return response.as(UserBalanceUseResponse.class);
	}

	public static BalanceTransactionResponses parseBalanceTransactionResponses(ExtractableResponse<Response> response) {
		return response.as(BalanceTransactionResponses.class);
	}
}
