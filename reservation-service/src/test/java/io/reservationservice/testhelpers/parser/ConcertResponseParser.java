package io.reservationservice.testhelpers.parser;

import io.reservationservice.api.application.dto.response.ConcertCreateResponse;
import io.reservationservice.api.application.dto.response.ConcertOptionCreateResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public class ConcertResponseParser {
	public static ConcertCreateResponse parseConcertCreateResponse(ExtractableResponse<Response> response) {
		return response.as(ConcertCreateResponse.class);
	}

	public static ConcertOptionCreateResponse parseConcertOptionCreateResponse(ExtractableResponse<Response> response) {
		return response.as(ConcertOptionCreateResponse.class);
	}
}