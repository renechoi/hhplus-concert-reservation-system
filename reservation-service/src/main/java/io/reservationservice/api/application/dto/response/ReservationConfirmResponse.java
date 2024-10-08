package io.reservationservice.api.application.dto.response;

import java.time.LocalDateTime;

import io.reservationservice.api.business.dto.outport.ReservationConfirmInfo;
import io.reservationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public record ReservationConfirmResponse(
    Long reservationId,
    Long userId,
    ConcertOptionResponse concertOption,
    SeatResponse seat,
    LocalDateTime reserveAt,
    LocalDateTime createdAt,
    Boolean isConfirmed
) {

    public static ReservationConfirmResponse from(ReservationConfirmInfo reservationConfirmInfo) {
        return ObjectMapperBasedVoMapper.convert(reservationConfirmInfo, ReservationConfirmResponse.class);
    }
}