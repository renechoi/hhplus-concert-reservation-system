package io.reservationservice.testhelpers.apiexecutor;

import static io.reservationservice.testhelpers.apiexecutor.DynamicPortHolder.*;
import static io.restassured.RestAssured.*;
import static org.springframework.http.MediaType.*;

import io.reservationservice.util.YmlLoader;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */

public class AvailabilityApiExecutor extends AbstractRequestExecutor {
	private static final String CONTEXT_PATH = YmlLoader.ymlLoader().getContextPath();
	private static final String CONCERT_API_URL_PATH = CONTEXT_PATH + "/api/availability";

	private static RequestSpecification getRequestSpecification(int port) {
		return given().log().all().port(port).contentType(APPLICATION_JSON_VALUE);
	}

	public static ExtractableResponse<Response> getAvailableDates(Long concertId) {
		String url = CONCERT_API_URL_PATH + "/dates/" + concertId;
		return doGet(getRequestSpecification(getPort()), url);
	}

	public static ExtractableResponse<Response> getAvailableDatesWithOk(Long concertId) {
		String url = CONCERT_API_URL_PATH + "/dates/" + concertId;
		return doGetWithOk(getRequestSpecification(getPort()), url);
	}

	public static ExtractableResponse<Response> getAvailableSeats(Long concertOptionId, Long requestAt) {
		String url = CONCERT_API_URL_PATH + "/seats/" + concertOptionId + "/" + requestAt;
		return doGet(getRequestSpecification(getPort()), url);
	}

	public static ExtractableResponse<Response> getAvailableSeatsWithOk(Long concertOptionId, Long requestAt) {
		String url = CONCERT_API_URL_PATH + "/seats/" + concertOptionId + "/" + requestAt;
		return doGetWithOk(getRequestSpecification(getPort()), url);
	}
}