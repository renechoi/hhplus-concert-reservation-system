package io.reservationservice.testhelpers.apiexecutor;

import static io.reservationservice.testhelpers.apiexecutor.DynamicPortHolder.*;
import static io.restassured.RestAssured.*;
import static org.springframework.http.MediaType.*;

import io.reservationservice.api.application.dto.request.ConcertCreateRequest;
import io.reservationservice.api.application.dto.request.ConcertOptionCreateRequest;
import io.reservationservice.util.YmlLoader;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */

public class ConcertApiExecutor extends AbstractRequestExecutor {
	private static final String CONTEXT_PATH = YmlLoader.ymlLoader().getContextPath();
	private static final String CONCERT_API_URL_PATH = CONTEXT_PATH + "/api/concerts";

	private static RequestSpecification getRequestSpecification(int port) {
		return given().log().all().port(port).contentType(APPLICATION_JSON_VALUE);
	}

	public static ExtractableResponse<Response> createConcert(ConcertCreateRequest request) {
		return doPost(getRequestSpecification(getPort()), CONCERT_API_URL_PATH, request);
	}

	public static ExtractableResponse<Response> createConcertWithCreated(ConcertCreateRequest request) {
		return doPostWithCreated(getRequestSpecification(getPort()), CONCERT_API_URL_PATH, request);
	}

	public static ExtractableResponse<Response> createConcertOption(Long concertId, ConcertOptionCreateRequest request) {
		return doPost(getRequestSpecification(getPort()), CONCERT_API_URL_PATH + "/" + concertId + "/options", request);
	}

	public static ExtractableResponse<Response> createConcertOptionWithCreated(Long concertId, ConcertOptionCreateRequest request) {
		return doPostWithCreated(getRequestSpecification(getPort()), CONCERT_API_URL_PATH + "/" + concertId + "/options", request);
	}
}