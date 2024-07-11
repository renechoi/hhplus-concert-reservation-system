package io.apiorchestrationservice.api.infrastructure.clients.reservation.dto;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import io.apiorchestrationservice.api.business.dto.outport.AvailableDateInfo;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public record AvailableDateDomainServiceResponse(
	Long concertOptionId,
	LocalDateTime concertDate,
	Duration concertDuration,
	String title,
	String description,
	BigDecimal price
) {

	public static AvailableDateDomainServiceResponse from(AvailableDateDomainServiceInfo availableDateDomainServiceInfo) {
		return ObjectMapperBasedVoMapper.convert(availableDateDomainServiceInfo, AvailableDateDomainServiceResponse.class);
	}

	public AvailableDateInfo toAvailableDateInfo() {
		return ObjectMapperBasedVoMapper.convert(this, AvailableDateInfo.class);
	}
}
