package io.apiorchestrationservice.testhelpers.parser;

import io.apiorchestrationservice.api.application.dto.response.AvailableDatesResponses;
import io.apiorchestrationservice.api.application.dto.response.AvailableSeatsResponses;
import io.apiorchestrationservice.api.application.dto.response.ConcertCreateResponse;
import io.apiorchestrationservice.api.application.dto.response.ConcertOptionCreateResponse;
import io.apiorchestrationservice.api.application.dto.response.ReservationCreateResponse;
import io.apiorchestrationservice.api.application.dto.response.ReservationStatusResponses;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */

public class ReservationAndConcertResponseParser {
	public static ConcertCreateResponse parseConcertCreateResponse(ExtractableResponse<Response> response) {
		return response.as(ConcertCreateResponse.class);
	}

	public static ConcertOptionCreateResponse parseConcertOptionCreateResponse(ExtractableResponse<Response> response) {
		return response.as(ConcertOptionCreateResponse.class);
	}

	public static AvailableDatesResponses parseAvailableDatesResponse(ExtractableResponse<Response> response) {
		return response.as(AvailableDatesResponses.class);
	}

	public static AvailableSeatsResponses parseAvailableSeatsResponse(ExtractableResponse<Response> response) {
		return response.as(AvailableSeatsResponses.class);
	}

	public static ReservationCreateResponse parseReservationCreateResponse(ExtractableResponse<Response> response) {
		return response.as(ReservationCreateResponse.class);
	}

	public static ReservationStatusResponses parseReservationStatusResponse(ExtractableResponse<Response> response) {
		return response.as(ReservationStatusResponses.class);
	}
}
