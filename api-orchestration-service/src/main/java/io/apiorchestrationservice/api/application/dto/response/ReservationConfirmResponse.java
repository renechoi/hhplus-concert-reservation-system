package io.apiorchestrationservice.api.application.dto.response;

import java.time.LocalDateTime;

import io.apiorchestrationservice.api.business.dto.outport.ReservationConfirmInfo;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public record ReservationConfirmResponse(
    Long reservationId,
    Long userId,
    ConcertResponse concertOption,
    SeatResponse seat,
    LocalDateTime reserveAt,
    LocalDateTime createdAt,
    Boolean isConfirmed
) {

    public static ReservationConfirmResponse from(ReservationConfirmInfo reservationConfirmInfo) {
        return ObjectMapperBasedVoMapper.convert(reservationConfirmInfo, ReservationConfirmResponse.class);
    }
}