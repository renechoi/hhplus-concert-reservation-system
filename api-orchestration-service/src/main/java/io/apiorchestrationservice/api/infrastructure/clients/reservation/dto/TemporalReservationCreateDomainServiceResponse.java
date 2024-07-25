package io.apiorchestrationservice.api.infrastructure.clients.reservation.dto;

import java.time.LocalDateTime;

import io.apiorchestrationservice.api.business.dto.outport.TemporalReservationCreateInfo;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public record TemporalReservationCreateDomainServiceResponse(
	Long temporalReservationId,
	Long userId,
	ConcertOptionCreateDomainServiceResponse concertOption,
	SeatCreateDomainServiceResponse seat,
	Boolean isConfirmed,
	LocalDateTime reserveAt,
	LocalDateTime createdAt,
	LocalDateTime requestAt
) {
	public TemporalReservationCreateInfo toTemporalReservationCreateInfo() {
		return ObjectMapperBasedVoMapper.convert(this, TemporalReservationCreateInfo.class);
	}
}
