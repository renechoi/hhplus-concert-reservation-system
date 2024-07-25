package io.apiorchestrationservice.api.business.service;

import io.apiorchestrationservice.api.business.dto.inport.ReservationConfirmCommand;
import io.apiorchestrationservice.api.business.dto.inport.TemporalReservationCreateCommand;
import io.apiorchestrationservice.api.business.dto.outport.ReservationConfirmInfo;
import io.apiorchestrationservice.api.business.dto.outport.ReservationStatusInfos;
import io.apiorchestrationservice.api.business.dto.outport.TemporalReservationCreateInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
public interface ReservationService {
	TemporalReservationCreateInfo createTemporalReservation(TemporalReservationCreateCommand command);

	ReservationConfirmInfo confirmReservation(ReservationConfirmCommand confirmCommand);

	ReservationStatusInfos getReservationStatus(Long userId, Long ConcertOptionId);
}
