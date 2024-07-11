package io.reservationservice.testhelpers.parser;

import io.reservationservice.api.application.dto.response.AvailableDatesResponses;
import io.reservationservice.api.application.dto.response.AvailableSeatsResponses;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */

public class AvailabilityResponseParser {
	public static AvailableDatesResponses parseAvailableDatesResponse(ExtractableResponse<Response> response) {
		return response.as(AvailableDatesResponses.class);
	}

	public static AvailableSeatsResponses parseAvailableSeatsResponse(ExtractableResponse<Response> response) {
		return response.as(AvailableSeatsResponses.class);
	}
}