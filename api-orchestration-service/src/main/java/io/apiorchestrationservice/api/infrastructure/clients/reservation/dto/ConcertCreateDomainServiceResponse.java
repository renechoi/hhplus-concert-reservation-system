package io.apiorchestrationservice.api.infrastructure.clients.reservation.dto;

import java.time.LocalDateTime;

import io.apiorchestrationservice.api.business.dto.outport.ConcertCreateInfo;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public record ConcertCreateDomainServiceResponse(
	Long concertId,
	String title,
	LocalDateTime createdAt,
	LocalDateTime requestAt
) {

	public ConcertCreateInfo toConcertCreateInfo() {
		return ObjectMapperBasedVoMapper.convert(this, ConcertCreateInfo.class);
	}
}
