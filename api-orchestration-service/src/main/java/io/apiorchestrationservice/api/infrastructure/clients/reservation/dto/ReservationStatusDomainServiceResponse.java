package io.apiorchestrationservice.api.infrastructure.clients.reservation.dto;

import java.time.LocalDateTime;

import io.apiorchestrationservice.api.business.dto.outport.ReservationStatusInfo;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public record ReservationStatusDomainServiceResponse(
	Long reservationId,
	Long userId,
	Long concertOptionId,
	Long seatId,
	LocalDateTime reserveAt,
	LocalDateTime createdAt,
	Boolean isConfirmed,
	Boolean isCanceled,
	Boolean isTemporary
) {
	public ReservationStatusInfo toReservationStatusInfo() {
		return ObjectMapperBasedVoMapper.convert(this, ReservationStatusInfo.class);
	}
}