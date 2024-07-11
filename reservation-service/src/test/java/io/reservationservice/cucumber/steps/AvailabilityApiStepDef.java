package io.reservationservice.cucumber.steps;

import static io.reservationservice.testhelpers.apiexecutor.AvailabilityApiExecutor.*;
import static io.reservationservice.testhelpers.contextholder.AvailabilityContextHolder.*;
import static io.reservationservice.testhelpers.fieldmatcher.ResponseMatcher.*;
import static io.reservationservice.testhelpers.parser.AvailabilityResponseParser.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.reservationservice.api.application.dto.response.AvailableDatesResponses;
import io.reservationservice.api.application.dto.response.AvailableSeatsResponses;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */

public class AvailabilityApiStepDef implements En {

	public AvailabilityApiStepDef() {

		When("다음과 같은 콘서트 예약 가능 날짜 조회 요청을 보내고 성공 응답을 받는다", this::getAvailableDatesWithSuccessResponse);
		Then("조회한 예약 가능 날짜 응답은 다음과 같이 확인되어야 한다", this::verifyAvailableDatesResponse);

		When("다음과 같은 콘서트 예약 가능 좌석 조회 요청을 보내고 성공 응답을 받는다", this::getAvailableSeatsWithSuccessResponse);
		Then("조회한 예약 가능 좌석 조회 응답은 다음과 같이 확인되어야 한다", this::verifyAvailableSeatsResponse);
		And("총 예약 가능 좌석 개수는 {int}개로 확인되어야 한다", this::verifyTotalAvailableSeatsCount);
	}


	private void getAvailableDatesWithSuccessResponse(DataTable dataTable) {
		Map<String, String> requestData = dataTable.asMaps().get(0);
		Long concertId = Long.parseLong(requestData.get("concertId"));

		putAvailableDates(concertId, parseAvailableDatesResponse(getAvailableDatesWithOk(concertId)));
	}

	private void verifyAvailableDatesResponse(DataTable dataTable) {
		List<Map<String, String>> expectedResponses = dataTable.asMaps(String.class, String.class);
		AvailableDatesResponses actualResponses = getMostRecentAvailableDatesResponse();
		assertNotNull(actualResponses, "조회한 예약 가능 날짜 응답이 존재하지 않습니다.");

		boolean allMatch = expectedResponses.stream().allMatch(expectedResponse ->
			actualResponses.availableDateResponses().stream().anyMatch(actualResponse ->
				matchResponse(expectedResponse, actualResponse)
			)
		);

		assertTrue(allMatch, "기대한 예약 가능 날짜 응답 정보가 일치하지 않습니다.");
	}

	private void getAvailableSeatsWithSuccessResponse(DataTable dataTable) {
		Map<String, String> requestData = dataTable.asMaps().get(0);
		Long concertOptionId = Long.parseLong(requestData.get("concertOptionId"));
		Long requestAt = requestData.get("requestAt").equals("now") ? Instant.now().toEpochMilli() : Long.parseLong(requestData.get("requestAt"));

		putAvailableSeats(concertOptionId, parseAvailableSeatsResponse(getAvailableSeatsWithOk(concertOptionId, requestAt)));
	}

	private void verifyAvailableSeatsResponse(DataTable dataTable) {
		List<Map<String, String>> expectedResponses = dataTable.asMaps(String.class, String.class);
		AvailableSeatsResponses actualResponses = getMostRecentAvailableSeatsResponse();
		assertNotNull(actualResponses, "조회한 예약 가능 좌석 조회 응답이 존재하지 않습니다.");

		boolean allMatch = expectedResponses.stream().allMatch(expectedResponse ->
			actualResponses.availableSeatsResponses().stream().anyMatch(actualResponse ->
				matchResponse(expectedResponse, actualResponse)
			)
		);

		assertTrue(allMatch, "기대한 예약 가능 좌석 조회 응답 정보가 일치하지 않습니다.");
	}

	private void verifyTotalAvailableSeatsCount(int expectedCount) {
		AvailableSeatsResponses actualResponses = getMostRecentAvailableSeatsResponse();
		assertNotNull(actualResponses, "조회한 예약 가능 좌석 조회 응답이 존재하지 않습니다.");
		assertEquals(expectedCount, actualResponses.availableSeatsResponses().stream().filter(item->!item.occupied()).toList().size(), "총 예약 가능 좌석 개수가 일치하지 않습니다.");
	}
}