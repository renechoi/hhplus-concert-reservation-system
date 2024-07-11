package io.apiorchestrationservice.api.infrastructure.clients.reservation.dto;

import java.time.LocalDateTime;

import io.apiorchestrationservice.api.business.dto.outport.AvailableSeatInfo;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;

public record AvailableSeatsDomainServiceResponse(
	Long seatId,
	ConcertOptionCreateDomainServiceResponse concertOption,
	Long seatNumber,
	Boolean occupied,
	LocalDateTime createdAt
) {
	public static AvailableSeatsDomainServiceResponse from(AvailableSeatsDomainServiceInfo info) {
		return ObjectMapperBasedVoMapper.convert(info, AvailableSeatsDomainServiceResponse.class);
	}

	public AvailableSeatInfo toAvailableSeatsInfo() {
		return ObjectMapperBasedVoMapper.convert(this, AvailableSeatInfo.class);
	}
}
