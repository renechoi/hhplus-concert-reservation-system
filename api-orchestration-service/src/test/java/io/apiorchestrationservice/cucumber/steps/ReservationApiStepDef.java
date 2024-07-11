package io.apiorchestrationservice.cucumber.steps;

import static io.apiorchestrationservice.testhelpers.apiexecutor.ReservationAndConcertApiExecutor.*;
import static io.apiorchestrationservice.testhelpers.contextholder.QueueTokenContextHolder.*;
import static io.apiorchestrationservice.testhelpers.contextholder.ReservationAndConcertContextHolder.*;
import static io.apiorchestrationservice.testhelpers.parser.ReservationAndConcertResponseParser.*;
import static io.apiorchestrationservice.util.CustomFieldMapper.*;
import static io.apiorchestrationservice.util.FieldMapper.*;
import static java.time.LocalDateTime.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import io.apiorchestrationservice.api.application.dto.request.ReservationCreateRequest;
import io.apiorchestrationservice.api.application.dto.response.AvailableDatesResponse;
import io.apiorchestrationservice.api.application.dto.response.ReservationCreateResponse;
import io.apiorchestrationservice.api.application.dto.response.ReservationStatusResponses;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public class ReservationApiStepDef implements En {
	public ReservationApiStepDef() {
		Given("최근 생성한 콘서트 옵션에 대해 가장 빠른 날짜와 가장 앞의 좌석에 예약 생성 요청을 보내고 성공 응답을 받는다", this::createWithMostRecentConcertOptionReservationWithSuccessResponse);
		Then("예약이 성공적으로 생성되었음을 확인한다", this::verifyReservationCreation);


		And("예약 상태를 확인하면 임시 예약이 아니라 확정 예약으로 확인되어야 한다", this::verifyReservationIsConvertedToConfirmed);
	}



	private void createWithMostRecentConcertOptionReservationWithSuccessResponse(DataTable dataTable) {
		Map<String, String> reservationData = dataTable.asMaps().get(0);

		ReservationCreateRequest request = applyCustomMappings(updateFields(new ReservationCreateRequest(), reservationData), reservationData);
		request.setRequestAt(now());
		request.setSeatNumber(getMostRecentAvailableSeatsResponse().availableSeatsResponses().get(0).seatNumber());
		Long concertOptionIdOfAvailableDates = getMostRecentAvailableDatesResponse().availableDateResponses().stream()
			.min(Comparator.comparing(AvailableDatesResponse::concertDate))
			.map(AvailableDatesResponse::concertOptionId)
			.orElseThrow(() -> new RuntimeException("No available dates found"));
		request.setConcertOptionId(concertOptionIdOfAvailableDates);

		ReservationCreateResponse response = parseReservationCreateResponse(createTemporaryReservationWithTokenWithOk(request, getMostRecentToken()));
		putReservation(response);
	}

	private void verifyReservationCreation(DataTable dataTable) {
		List<Map<String, String>> expectedReservations = dataTable.asMaps(String.class, String.class);
		ReservationCreateResponse actualResponse = getMostRecentReservationCreateResponse();
		assertNotNull(actualResponse, "생성된 예약의 응답이 존재하지 않습니다.");

		boolean matchFound = expectedReservations.stream().anyMatch(expectedReservation ->
			expectedReservation.get("userId").equals(String.valueOf(actualResponse.userId()))
		);

		assertTrue(matchFound, "기대한 예약 정보가 일치하지 않습니다.");
	}



	private void verifyReservationIsConvertedToConfirmed() {
		ReservationStatusResponses responses = parseReservationStatusResponse(getReservationStatusWithOk(getMostRecentReservationCreateResponse().userId(), getMostRecentReservationCreateResponse().concertOption().concertOptionId()));

		assertFalse(responses.reservationStatusResponses().isEmpty());
		responses.reservationStatusResponses().stream()
			.filter(reservationStatusResponse -> reservationStatusResponse.userId().equals(getMostRecentReservationCreateResponse().userId()))
			.filter(reservationStatusResponse -> !reservationStatusResponse.isTemporary())
			.forEach(reservationStatusResponse -> assertTrue(reservationStatusResponse.isConfirmed()));

	}
}
