package io.apiorchestrationservice.api.infrastructure.clients.reservation.dto;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import io.apiorchestrationservice.api.business.dto.outport.ConcertOptionCreateInfo;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public record ConcertOptionCreateDomainServiceResponse(

	Long concertOptionId,
	ConcertCreateDomainServiceResponse concert,
	LocalDateTime concertDate,
	Duration concertDuration,
	String title,
	String description,
	BigDecimal price,
	LocalDateTime createdAt,
	LocalDateTime requestAt
) {
	public ConcertOptionCreateInfo toConcertOptionCreateInfo() {
		return ObjectMapperBasedVoMapper.convert(this, ConcertOptionCreateInfo.class);
	}
}
