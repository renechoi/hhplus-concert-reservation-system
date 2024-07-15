package io.reservationservice.cucumber.steps;

import static io.reservationservice.testhelpers.apiexecutor.ConcertApiExecutor.*;
import static io.reservationservice.testhelpers.contextholder.ConcertContextHolder.*;
import static io.reservationservice.testhelpers.parser.ConcertResponseParser.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.reservationservice.api.application.dto.request.ConcertCreateRequest;
import io.reservationservice.api.application.dto.response.ConcertCreateResponse;
import io.reservationservice.util.FieldMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */

public class ConcertApiStepDef implements En {

	public ConcertApiStepDef() {

		Given("다음과 같은 콘서트 생성 요청을 보내고 성공 응답을 받는다", this::createConcertWithSuccessResponse);
		Then("콘서트가 성공적으로 생성되었음을 확인한다", this::verifyConcertCreation);
	}

	private void createConcertWithSuccessResponse(DataTable dataTable) {
		Map<String, String> concertData = dataTable.asMaps().get(0);
		ConcertCreateRequest request = FieldMapper.updateFields(new ConcertCreateRequest(), concertData);

		putConcertCreateResponse(parseConcertCreateResponse(createConcertWithCreated(request)));
	}

	private void verifyConcertCreation(DataTable dataTable) {
		List<Map<String, String>> expectedConcerts = dataTable.asMaps(String.class, String.class);
		ConcertCreateResponse actualResponse = getMostRecentConcertCreateResponse();
		assertNotNull(actualResponse, "생성된 콘서트의 응답이 존재하지 않습니다.");

		boolean matchFound = expectedConcerts.stream().anyMatch(expectedConcert ->
			expectedConcert.get("title").equals(actualResponse.title())
		);

		assertTrue(matchFound, "기대한 콘서트 정보가 일치하지 않습니다.");
	}
}
