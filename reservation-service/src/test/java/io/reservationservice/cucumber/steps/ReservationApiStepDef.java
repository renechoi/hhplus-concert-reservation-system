package io.reservationservice.cucumber.steps;

import static io.reservationservice.testhelpers.apiexecutor.ReservationApiExecutor.*;
import static io.reservationservice.testhelpers.contextholder.ReservationContextHolder.*;
import static io.reservationservice.testhelpers.parser.ReservationResponseParser.*;
import static io.reservationservice.util.FieldMapper.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.reservationservice.api.application.dto.request.ReservationConfirmRequest;
import io.reservationservice.api.application.dto.request.ReservationCreateRequest;
import io.reservationservice.api.application.dto.response.ReservationConfirmResponse;
import io.reservationservice.api.application.dto.response.ReservationStatusResponses;
import io.reservationservice.api.application.dto.response.SeatResponse;
import io.reservationservice.api.application.dto.response.TemporalReservationCreateResponse;
import io.reservationservice.api.business.domainentity.TemporalReservation;
import io.reservationservice.api.infrastructure.persistence.orm.TemporalReservationJpaRepository;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */

public class ReservationApiStepDef implements En {

	private TemporalReservationJpaRepository temporalReservationRepository;

	@Autowired
	public ReservationApiStepDef(
		TemporalReservationJpaRepository temporalReservationRepository
	) {
		this.temporalReservationRepository = temporalReservationRepository;

		Given("다음과 같은 예약 생성 요청을 보내고 성공 응답을 받는다", this::createReservationWithSuccessResponse);
		Then("예약이 성공적으로 생성되었음을 확인한다", this::verifyReservationCreation);

		When("예약 상태 조회 요청을 보내고 성공 응답을 받는다", this::getReservationStatusWithSuccessResponse);

		And("조회한 예약에 연결된 좌석은 다음과 같은 상태여야 한다", this::verifySeatStatus);
		Then("예약이 만료되어 취소되었음을 확인한다", this::verifyReservationExpiration);

		Then("전체 예약을 repository에서 조회하여 다음과 같은 1개의 예약이 생성되었음을 확인한다", this::verifyReservationCountByRepository);


		When("다음과 같은 예약 확정 요청을 보내고 성공 응답을 받는다", this::confirmReservationWithSuccessResponse);
		Then("예약이 성공적으로 확정되었음을 확인한다", this::verifyReservationConfirmation);

	}

	private void verifyReservationCountByRepository(DataTable dataTable) {
		List<Map<String, String>> expectedReservations = dataTable.asMaps(String.class, String.class);
		List<TemporalReservation> actualReservations = temporalReservationRepository.findAll();

		assertEquals(expectedReservations.size(), actualReservations.size(), "예약 개수가 일치하지 않습니다.");

		for (Map<String, String> expectedReservation : expectedReservations) {
			boolean matchFound = actualReservations.stream().anyMatch(actualReservation ->
				expectedReservation.get("temporalReservationId").equals(String.valueOf(actualReservation.getTemporalReservationId()))
			);
			assertTrue(matchFound, "기대한 예약 정보가 일치하지 않습니다.");
		}
	}

	private void createReservationWithSuccessResponse(DataTable dataTable) {
		Map<String, String> reservationData = dataTable.asMaps().get(0);

		ReservationCreateRequest request = updateFields(new ReservationCreateRequest(), reservationData);

		TemporalReservationCreateResponse response = parseReservationCreateResponse(createReservationWithCreated(request));
		putReservationCreateResponse(response);
	}

	private void getReservationStatusWithSuccessResponse(DataTable dataTable) {
		Map<String, String> requestData = dataTable.asMaps().get(0);
		Long userId = Long.parseLong(requestData.get("userId"));
		Long concertOptionId = Long.parseLong(requestData.get("concertOptionId"));

		ReservationStatusResponses response = parseReservationStatusResponse(getReservationStatus(userId, concertOptionId));
		putReservationStatusResponse(userId, concertOptionId, response);
	}

	private void verifyReservationCreation(DataTable dataTable) {
		List<Map<String, String>> expectedReservations = dataTable.asMaps(String.class, String.class);
		TemporalReservationCreateResponse actualResponse = getMostRecentReservationCreateResponse();
		assertNotNull(actualResponse, "생성된 예약의 응답이 존재하지 않습니다.");

		boolean matchFound = expectedReservations.stream().anyMatch(expectedReservation ->
			expectedReservation.get("userId").equals(String.valueOf(actualResponse.userId()))
		);

		assertTrue(matchFound, "기대한 예약 정보가 일치하지 않습니다.");
	}

	private void verifySeatStatus(DataTable dataTable) {
		Map<String, String> expectedSeatStatus = dataTable.asMaps().get(0);
		boolean expectedOccupied = Boolean.parseBoolean(expectedSeatStatus.get("occupied"));

		ReservationStatusResponses response = getMostRecentReservationStatusResponse();
		assertNotNull(response, "생성된 예약의 응답이 존재하지 않습니다.");

		SeatResponse seat = response.reservationStatusResponses().get(0).seat();
		assertNotNull(seat, "좌석이 존재하지 않습니다.");
		assertEquals(expectedOccupied, seat.occupied(), "좌석 점유 상태가 일치하지 않습니다.");
	}

	private void verifyReservationExpiration() {
		ReservationStatusResponses response = getMostRecentReservationStatusResponse();
		assertTrue(response.reservationStatusResponses().get(0).isCanceled());
	}


	// 예약 확정 요청을 보내고 성공 응답을 받는 메서드
	private void confirmReservationWithSuccessResponse(DataTable dataTable) {
		Map<String, String> requestData = dataTable.asMaps().get(0);
		ReservationConfirmRequest request = updateFields(new ReservationConfirmRequest(), requestData);
		ReservationConfirmResponse response = parseReservationConfirmResponse(confirmReservationWithOk(request));
		putReservationConfirmResponse(response);
	}

	// 예약이 성공적으로 확정되었음을 확인하는 메서드
	private void verifyReservationConfirmation(DataTable dataTable) {
		Map<String, String> expectedData = dataTable.asMaps().get(0);
		ReservationConfirmResponse actualResponse = getMostRecentReservationConfirmResponse();

		assertNotNull(actualResponse, "예약 확정 응답이 존재하지 않습니다.");

		assertEquals(Long.parseLong(expectedData.get("userId")), actualResponse.userId(), "userId가 일치하지 않습니다.");
		assertEquals(Long.parseLong(expectedData.get("concertOptionId")), actualResponse.concertOption().concertOptionId(), "concertOptionId가 일치하지 않습니다.");
	}
}