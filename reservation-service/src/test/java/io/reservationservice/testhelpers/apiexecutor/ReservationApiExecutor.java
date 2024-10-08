package io.reservationservice.testhelpers.apiexecutor;

import static io.reservationservice.testhelpers.apiexecutor.DynamicPortHolder.*;
import static io.restassured.RestAssured.*;
import static org.springframework.http.MediaType.*;

import io.reservationservice.api.application.dto.request.ReservationConfirmRequest;
import io.reservationservice.api.application.dto.request.ReservationCreateRequest;
import io.reservationservice.util.YmlLoader;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public class ReservationApiExecutor extends AbstractRequestExecutor {
	private static final String CONTEXT_PATH = YmlLoader.ymlLoader().getContextPath();
	private static final String CONCERT_API_URL_PATH = CONTEXT_PATH + "/api/reservations";
	private static final String RESERVATION_STATUS_API_URL_PATH = CONCERT_API_URL_PATH + "/status";
	private static final String RESERVATION_CONFIRM_API_URL_PATH = CONCERT_API_URL_PATH + "/confirm";


	private static RequestSpecification getRequestSpecification(int port) {
		return given().log().all().port(port).contentType(APPLICATION_JSON_VALUE);
	}

	public static ExtractableResponse<Response> createReservation(ReservationCreateRequest request) {
		return doPost(getRequestSpecification(getPort()), CONCERT_API_URL_PATH, request);
	}

	public static ExtractableResponse<Response> createReservationWithCreated(ReservationCreateRequest request) {
		return doPostWithCreated(getRequestSpecification(getPort()), CONCERT_API_URL_PATH, request);
	}


	public static ExtractableResponse<Response> getReservationStatus(Long userId, Long concertOptionId) {
		return doGet(getRequestSpecification(getPort()), String.format("%s/%d/%d", RESERVATION_STATUS_API_URL_PATH, userId, concertOptionId));
	}


	public static ExtractableResponse<Response> getReservationStatusWithOk(Long userId, Long concertOptionId) {
		return doGetWithOk(getRequestSpecification(getPort()), String.format("%s/%d/%d", RESERVATION_STATUS_API_URL_PATH, userId, concertOptionId));
	}


	public static ExtractableResponse<Response> confirmReservation(ReservationConfirmRequest request) {
		return doPost(getRequestSpecification(getPort()), RESERVATION_CONFIRM_API_URL_PATH, request);
	}

	public static ExtractableResponse<Response> confirmReservationWithOk(ReservationConfirmRequest request) {
		return doPostWithOk(getRequestSpecification(getPort()), RESERVATION_CONFIRM_API_URL_PATH, request);
	}
}