package io.apiorchestrationservice.api.business.service.impl;

import org.springframework.stereotype.Service;

import io.apiorchestrationservice.api.business.client.ReservationServiceClientAdapter;
import io.apiorchestrationservice.api.business.dto.inport.ReservationConfirmCommand;
import io.apiorchestrationservice.api.business.dto.inport.TemporaryReservationCreateCommand;
import io.apiorchestrationservice.api.business.dto.outport.ReservationConfirmInfo;
import io.apiorchestrationservice.api.business.dto.outport.ReservationStatusInfos;
import io.apiorchestrationservice.api.business.dto.outport.TemporaryReservationCreateInfo;
import io.apiorchestrationservice.api.business.service.ReservationService;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Service
@RequiredArgsConstructor
public class SimpleReservationService implements ReservationService {
	private final ReservationServiceClientAdapter reservationServiceClientAdapter;

	@Override
	public TemporaryReservationCreateInfo createTemporaryReservation(TemporaryReservationCreateCommand command) {
		return reservationServiceClientAdapter.createTemporaryReservation(command);
	}

	@Override
	public ReservationConfirmInfo confirmReservation(ReservationConfirmCommand confirmCommand) {
		return reservationServiceClientAdapter.confirmReservation(confirmCommand);
	}

	@Override
	public ReservationStatusInfos getReservationStatus(Long userId, Long ConcertOptionId) {
		return reservationServiceClientAdapter.getReservationStatus(userId, ConcertOptionId);
	}
}
