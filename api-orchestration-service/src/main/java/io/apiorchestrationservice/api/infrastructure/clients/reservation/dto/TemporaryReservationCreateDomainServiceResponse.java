package io.apiorchestrationservice.api.infrastructure.clients.reservation.dto;

import java.time.LocalDateTime;

import io.apiorchestrationservice.api.business.dto.outport.TemporaryReservationCreateInfo;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public record TemporaryReservationCreateDomainServiceResponse(
	Long temporaryReservationId,
	Long userId,
	ConcertOptionCreateDomainServiceResponse concertOption,
	SeatCreateDomainServiceResponse seat,
	Boolean isConfirmed,
	LocalDateTime reserveAt,
	LocalDateTime createdAt,
	LocalDateTime requestAt
) {
	public TemporaryReservationCreateInfo toTemporaryReservationCreateInfo() {
		return ObjectMapperBasedVoMapper.convert(this, TemporaryReservationCreateInfo.class);
	}
}
