package io.apiorchestrationservice.api.infrastructure.clients.reservation.dto;

import java.time.LocalDateTime;

import io.apiorchestrationservice.api.business.dto.outport.ReservationConfirmInfo;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public record ReservationConfirmDomainServiceResponse(
    Long reservationId,
    Long userId,
    ConcertOptionCreateDomainServiceResponse concertOption,
    SeatCreateDomainServiceResponse seat,
    LocalDateTime reserveAt,
    LocalDateTime createdAt,
    Boolean isConfirmed
) {

    public static ReservationConfirmDomainServiceResponse from(ReservationConfirmInfo reservationConfirmInfo) {
        return ObjectMapperBasedVoMapper.convert(reservationConfirmInfo, ReservationConfirmDomainServiceResponse.class);
    }

    public ReservationConfirmInfo toReservationConfirmInfo() {
        return ObjectMapperBasedVoMapper.convert(this, ReservationConfirmInfo.class);
    }
}