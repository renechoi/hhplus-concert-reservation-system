package io.apiorchestrationservice.testhelpers.apiexecutor;

import static io.apiorchestrationservice.testhelpers.apiexecutor.DynamicPortHolder.*;
import static io.restassured.RestAssured.*;
import static org.springframework.http.MediaType.*;

import io.apiorchestrationservice.api.application.dto.request.AvailableSeatsRetrievalRequest;
import io.apiorchestrationservice.api.application.dto.request.ConcertCreateRequest;
import io.apiorchestrationservice.api.application.dto.request.ConcertOptionCreateRequest;
import io.apiorchestrationservice.api.application.dto.request.ReservationCreateRequest;
import io.apiorchestrationservice.util.YmlLoader;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public class ReservationAndConcertApiExecutor extends AbstractRequestExecutor {
	private static final String CONTEXT_PATH = YmlLoader.ymlLoader().getContextPath();
	private static final String RESERVATION_API_URL_PATH = CONTEXT_PATH + "/api/reservations";

	private static RequestSpecification getRequestSpecification(int port) {
		return given().log().all().port(port).contentType(APPLICATION_JSON_VALUE);
	}

	public static ExtractableResponse<Response> createConcert(ConcertCreateRequest request) {
		String url = RESERVATION_API_URL_PATH + "/concerts";
		return doPost(getRequestSpecification(getPort()), url, request);
	}

	public static ExtractableResponse<Response> createConcertWithCreated(ConcertCreateRequest request) {
		String url = RESERVATION_API_URL_PATH + "/concerts";
		return doPostWithCreated(getRequestSpecification(getPort()), url, request);
	}

	public static ExtractableResponse<Response> createConcertOption(Long concertId, ConcertOptionCreateRequest request) {
		String url = RESERVATION_API_URL_PATH + "/concerts/" + concertId + "/options";
		return doPost(getRequestSpecification(getPort()), url, request);
	}

	public static ExtractableResponse<Response> createConcertOptionWithCreated(Long concertId, ConcertOptionCreateRequest request) {
		String url = RESERVATION_API_URL_PATH + "/concerts/" + concertId + "/options";
		return doPostWithCreated(getRequestSpecification(getPort()), url, request);
	}


	public static ExtractableResponse<Response> getAvailableDates(Long concertOptionId) {
		String url = RESERVATION_API_URL_PATH + "/available-dates/" + concertOptionId;
		return doGet(getRequestSpecification(getPort()), url);
	}


	public static ExtractableResponse<Response> getAvailableDatesWithOk(Long concertOptionId) {
		String url = RESERVATION_API_URL_PATH + "/available-dates/" + concertOptionId;
		return doGetWithOk(getRequestSpecification(getPort()), url);
	}

	public static ExtractableResponse<Response> getAvailableDatesWithOkWithToken(Long concertOptionId, String token) {
		String url = RESERVATION_API_URL_PATH + "/available-dates/" + concertOptionId;
		return doGetWithOkWithToken(getRequestSpecification(getPort()), url, token);
	}

	public static ExtractableResponse<Response> getAvailableSeats(AvailableSeatsRetrievalRequest request) {
		String url = RESERVATION_API_URL_PATH + "/available-seats";
		return doGet(getRequestSpecification(getPort()).params(convertDtoToMap(request)), url);
	}

	public static ExtractableResponse<Response> getAvailableSeatsWithOk(AvailableSeatsRetrievalRequest request) {
		String url = RESERVATION_API_URL_PATH + "/available-seats";
		return doGetWithOk(getRequestSpecification(getPort()).params(convertDtoToMap(request)), url);
	}

	public static ExtractableResponse<Response> getAvailableSeatsWithOkWithToken(AvailableSeatsRetrievalRequest request, String token) {
		String url = RESERVATION_API_URL_PATH + "/available-seats";
		return doGetWithOkWithToken(getRequestSpecification(getPort()).params(convertDtoToMap(request)), url, token);
	}

	public static ExtractableResponse<Response> createTemporalReservation(ReservationCreateRequest request) {
		String url = RESERVATION_API_URL_PATH;
		return doPost(getRequestSpecification(getPort()), url, request);
	}

	public static ExtractableResponse<Response> createTemporalReservationWithOk(ReservationCreateRequest request) {
		String url = RESERVATION_API_URL_PATH;
		return doPostWithOk(getRequestSpecification(getPort()), url, request);
	}

	public static ExtractableResponse<Response> createTemporalReservationWithTokenWithOk(ReservationCreateRequest request, String token) {
		String url = RESERVATION_API_URL_PATH;
		return doPostWithOkWithToken(getRequestSpecification(getPort()), url, request, token);
	}



	public static ExtractableResponse<Response> getReservationStatus(Long userId, Long concertOptionId) {
		String url = RESERVATION_API_URL_PATH + "/status" + "/" + userId + "/" + concertOptionId;
		return doGet(getRequestSpecification(getPort()), url);
	}


	public static ExtractableResponse<Response> getReservationStatusWithOk(Long userId, Long concertOptionId) {
		String url = RESERVATION_API_URL_PATH + "/status" + "/" + userId + "/" + concertOptionId;
		return doGetWithOk(getRequestSpecification(getPort()), url);
	}

}
