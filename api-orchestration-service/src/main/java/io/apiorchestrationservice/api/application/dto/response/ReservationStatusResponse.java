package io.apiorchestrationservice.api.application.dto.response;

import java.time.LocalDateTime;

import io.apiorchestrationservice.api.business.dto.outport.ReservationStatusInfo;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public record ReservationStatusResponse(
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

	public static ReservationStatusResponse from(ReservationStatusInfo reservationStatusInfo) {
		return ObjectMapperBasedVoMapper.convert(reservationStatusInfo, ReservationStatusResponse.class);
	}
}