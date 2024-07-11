package io.reservationservice.cucumber.steps;

import static io.reservationservice.testhelpers.apiexecutor.ReservationApiExecutor.*;
import static io.reservationservice.testhelpers.contextholder.ReservationContextHolder.*;
import static io.reservationservice.testhelpers.parser.ReservationResponseParser.*;
import static io.reservationservice.util.FieldMapper.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.reservationservice.api.application.dto.request.ReservationCreateRequest;
import io.reservationservice.api.application.dto.response.TemporaryReservationCreateResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */

public class ReservationApiStepDef implements En {

	public ReservationApiStepDef() {
		Given("다음과 같은 예약 생성 요청을 보내고 성공 응답을 받는다", this::createReservationWithSuccessResponse);
		Then("예약이 성공적으로 생성되었음을 확인한다", this::verifyReservationCreation);
	}

	private void createReservationWithSuccessResponse(DataTable dataTable) {
		Map<String, String> reservationData = dataTable.asMaps().get(0);

		ReservationCreateRequest request = updateFields(new ReservationCreateRequest(), reservationData);

		TemporaryReservationCreateResponse response = parseReservationCreateResponse(createReservationWithCreated(request));
		putReservationCreateResponse(response);
	}

	private void verifyReservationCreation(DataTable dataTable) {
		List<Map<String, String>> expectedReservations = dataTable.asMaps(String.class, String.class);
		TemporaryReservationCreateResponse actualResponse = getMostRecentReservationCreateResponse();
		assertNotNull(actualResponse, "생성된 예약의 응답이 존재하지 않습니다.");

		boolean matchFound = expectedReservations.stream().anyMatch(expectedReservation ->
			expectedReservation.get("userId").equals(String.valueOf(actualResponse.userId()))
		);

		assertTrue(matchFound, "기대한 예약 정보가 일치하지 않습니다.");
	}
}