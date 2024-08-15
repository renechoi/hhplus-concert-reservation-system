package io.reservationservice.testhelpers.parser;

import io.reservationservice.api.application.dto.response.ReservationConfirmResponse;
import io.reservationservice.api.application.dto.response.ReservationStatusResponses;
import io.reservationservice.api.application.dto.response.TemporalReservationCreateResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public class ReservationResponseParser {
	public static TemporalReservationCreateResponse parseReservationCreateResponse(ExtractableResponse<Response> response) {
		return response.as(TemporalReservationCreateResponse.class);
	}


	public static ReservationStatusResponses parseReservationStatusResponse(ExtractableResponse<Response> response) {
		return response.as(ReservationStatusResponses.class);
	}


	public static ReservationConfirmResponse parseReservationConfirmResponse(ExtractableResponse<Response> response) {
		return response.as(ReservationConfirmResponse.class);
	}
}