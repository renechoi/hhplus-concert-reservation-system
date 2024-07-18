package io.reservationservice.testhelpers.parser;

import io.reservationservice.api.application.dto.response.ReservationStatusResponses;
import io.reservationservice.api.application.dto.response.TemporaryReservationCreateResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public class ReservationResponseParser {
	public static TemporaryReservationCreateResponse parseReservationCreateResponse(ExtractableResponse<Response> response) {
		return response.as(TemporaryReservationCreateResponse.class);
	}


	public static ReservationStatusResponses parseReservationStatusResponse(ExtractableResponse<Response> response) {
		return response.as(ReservationStatusResponses.class);
	}
}