package io.apiorchestrationservice.api.business.service;

import io.apiorchestrationservice.api.business.dto.inport.ReservationConfirmCommand;
import io.apiorchestrationservice.api.business.dto.inport.TemporaryReservationCreateCommand;
import io.apiorchestrationservice.api.business.dto.outport.ReservationConfirmInfo;
import io.apiorchestrationservice.api.business.dto.outport.ReservationStatusInfos;
import io.apiorchestrationservice.api.business.dto.outport.TemporaryReservationCreateInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
public interface ReservationService {
	TemporaryReservationCreateInfo createTemporaryReservation(TemporaryReservationCreateCommand command);

	ReservationConfirmInfo confirmReservation(ReservationConfirmCommand confirmCommand);

	ReservationStatusInfos getReservationStatus(Long userId, Long ConcertOptionId);
}
