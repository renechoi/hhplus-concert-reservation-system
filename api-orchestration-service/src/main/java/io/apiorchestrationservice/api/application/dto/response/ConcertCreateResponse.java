package io.apiorchestrationservice.api.application.dto.response;

import java.time.LocalDateTime;

import io.apiorchestrationservice.api.business.dto.outport.ConcertCreateInfo;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public record ConcertCreateResponse(
	Long concertId,
	String title,
	LocalDateTime createdAt,
	LocalDateTime requestAt
) {
	public static ConcertCreateResponse from(ConcertCreateInfo info) {
		return ObjectMapperBasedVoMapper.convert(info, ConcertCreateResponse.class);
	}
}
