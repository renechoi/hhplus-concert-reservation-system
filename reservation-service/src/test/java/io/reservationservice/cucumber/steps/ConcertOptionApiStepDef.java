package io.reservationservice.cucumber.steps;

import static io.reservationservice.testhelpers.apiexecutor.ConcertApiExecutor.*;
import static io.reservationservice.testhelpers.contextholder.ConcertContextHolder.*;
import static io.reservationservice.testhelpers.parser.ConcertResponseParser.*;
import static io.reservationservice.util.FieldMapper.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.reservationservice.api.application.dto.request.ConcertOptionCreateRequest;
import io.reservationservice.api.application.dto.response.ConcertOptionCreateResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */

public class ConcertOptionApiStepDef implements En {

	public ConcertOptionApiStepDef() {
		Given("다음과 같은 콘서트 옵션 생성 요청을 보내고 성공 응답을 받는다", this::createConcertOptionWithSuccessResponse);
		Then("콘서트 옵션이 성공적으로 생성되었음을 확인한다", this::verifyConcertOptionCreation);
	}

	private void createConcertOptionWithSuccessResponse(DataTable dataTable) {
		Map<String, String> concertOptionData = dataTable.asMaps().get(0);

		ConcertOptionCreateRequest request = updateFields(new ConcertOptionCreateRequest(), concertOptionData);

		Long concertId = getMostRecentConcertCreateResponse().concertId();
		ConcertOptionCreateResponse response = parseConcertOptionCreateResponse(createConcertOptionWithCreated(concertId, request));
		putConcertOptionCreateResponse(response);
	}

	private void verifyConcertOptionCreation(DataTable dataTable) {
		List<Map<String, String>> expectedConcertOptions = dataTable.asMaps(String.class, String.class);
		ConcertOptionCreateResponse actualResponse = getMostRecentConcertOptionCreateResponse();
		assertNotNull(actualResponse, "생성된 콘서트 옵션의 응답이 존재하지 않습니다.");

		boolean matchFound = expectedConcertOptions.stream().anyMatch(expectedConcertOption ->
			expectedConcertOption.get("title").equals(actualResponse.title())
		);

		assertTrue(matchFound, "기대한 콘서트 옵션 정보가 일치하지 않습니다.");
	}
}