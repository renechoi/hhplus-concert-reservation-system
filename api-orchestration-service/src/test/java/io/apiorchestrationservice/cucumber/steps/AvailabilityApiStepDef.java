package io.apiorchestrationservice.cucumber.steps;

import static io.apiorchestrationservice.testhelpers.apiexecutor.ReservationAndConcertApiExecutor.*;
import static io.apiorchestrationservice.testhelpers.contextholder.QueueTokenContextHolder.*;
import static io.apiorchestrationservice.testhelpers.contextholder.ReservationAndConcertContextHolder.*;
import static io.apiorchestrationservice.testhelpers.fieldmatcher.ResponseMatcher.*;
import static io.apiorchestrationservice.testhelpers.parser.ReservationAndConcertResponseParser.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import io.apiorchestrationservice.api.application.dto.request.AvailableSeatsRetrievalRequest;
import io.apiorchestrationservice.api.application.dto.response.AvailableDatesResponses;
import io.apiorchestrationservice.api.application.dto.response.AvailableSeatsResponses;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */

public class AvailabilityApiStepDef implements En {

	public AvailabilityApiStepDef() {

		When("최근 생성한 콘서트에 대해 다음과 같은 콘서트 예약 가능 날짜 조회 요청을 보내고 성공 응답을 받는다", this::getMostRecentAvailableDatesWithSuccessResponse);
		Then("조회한 예약 가능 날짜 응답은 다음과 같이 확인되어야 한다", this::verifyAvailableDatesResponse);

		When("최근 생성한 콘서트 옵션에 대해 다음과 같은 콘서트 예약 가능 좌석 조회 요청을 보내고 성공 응답을 받는다", this::getMostRecentAvailableSeatsWithSuccessResponse);
		Then("조회한 예약 가능 좌석 조회 응답은 다음과 같이 확인되어야 한다", this::verifyAvailableSeatsResponse);
		And("총 예약 가능 좌석 개수는 {int}개로 확인되어야 한다", this::verifyTotalAvailableSeatsCount);
	}


	private void getMostRecentAvailableDatesWithSuccessResponse() {
		Long concertId = getMostRecentConcertId();

		putAvailableDates(concertId, parseAvailableDatesResponse(getAvailableDatesWithOkWithToken(concertId, getMostRecentToken())));
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

	private void getMostRecentAvailableSeatsWithSuccessResponse() {
		Long concertOptionId = getMostRecentConcertOptionId();
		Long requestAt = Instant.now().toEpochMilli();

		putAvailableSeats(concertOptionId, parseAvailableSeatsResponse(getAvailableSeatsWithOkWithToken(new AvailableSeatsRetrievalRequest(concertOptionId, requestAt), getMostRecentToken())));
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