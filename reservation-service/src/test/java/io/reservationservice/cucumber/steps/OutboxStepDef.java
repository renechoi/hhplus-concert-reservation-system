package io.reservationservice.cucumber.steps;

import static io.reservationservice.common.mapper.ObjectMapperBasedVoMapper.*;
import static io.reservationservice.testhelpers.contextholder.ReservationContextHolder.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java8.En;
import io.reservationservice.api.application.dto.response.ReservationConfirmResponse;
import io.reservationservice.api.business.domainentity.OutboxEvent;
import io.reservationservice.api.business.domainentity.OutboxStatus;
import io.reservationservice.api.business.dto.event.ReservationConfirmEvent;
import io.reservationservice.api.infrastructure.persistence.orm.OutboxJpaRepository;
import lombok.SneakyThrows;

/**
 * @author : Rene Choi
 * @since : 2024/08/11
 */
public class OutboxStepDef implements En {

	private OutboxJpaRepository outboxJpaRepository;

	@Autowired
	public OutboxStepDef(
		OutboxJpaRepository outboxJpaRepository
	) {
		this.outboxJpaRepository = outboxJpaRepository;
		Then("Outbox 테이블에 이벤트가 저장되어 있어야 한다", this::verifyOutboxEventStored);
		Then("Outbox 테이블의 상태는 SENT로 업데이트되어 있어야 한다", this::verifyOutboxStatusUpdatedToSent);
	}

	@SneakyThrows
	private void verifyOutboxEventStored() {
		List<OutboxEvent> outboxEvents = outboxJpaRepository.findAll();

		assertFalse(outboxEvents.isEmpty(), "Outbox 테이블에 저장된 이벤트가 없습니다.");

		ReservationConfirmResponse reservationResponse = getMostRecentReservationConfirmResponse();
		OutboxEvent latestOutboxEvent = outboxEvents.get(outboxEvents.size() - 1);

		assertEquals(reservationResponse.reservationId(), getObjectMapper().readValue(latestOutboxEvent.getPayload(), ReservationConfirmEvent.class).getReservationId(),
			"Outbox 테이블에 저장된 이벤트의 Reservation ID가 일치하지 않습니다.");
	}

	private void verifyOutboxStatusUpdatedToSent() {
		List<OutboxEvent> outboxEvents = outboxJpaRepository.findAll();

		assertFalse(outboxEvents.isEmpty(), "Outbox 테이블에 저장된 이벤트가 없습니다.");

		OutboxEvent latestOutboxEvent = outboxEvents.get(outboxEvents.size() - 1);
		assertEquals(OutboxStatus.SENT, latestOutboxEvent.getStatus(), "Outbox 이벤트 상태가 SENT로 업데이트되지 않았습니다.");
	}

}