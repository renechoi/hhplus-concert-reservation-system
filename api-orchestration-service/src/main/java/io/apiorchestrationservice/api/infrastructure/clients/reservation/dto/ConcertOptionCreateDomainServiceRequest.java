package io.apiorchestrationservice.api.infrastructure.clients.reservation.dto;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import io.apiorchestrationservice.api.business.dto.inport.ConcertOptionCreateCommand;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConcertOptionCreateDomainServiceRequest extends AbstractCommonRequestInfo {
	@NotNull
	private LocalDateTime concertDate;
	@NotNull
	private Duration concertDuration;
	@NotEmpty
	private String title;
	private String description;
	@NotNull
	private BigDecimal price;
	private int maxSeats;

	public static ConcertOptionCreateDomainServiceRequest from(ConcertOptionCreateCommand command) {
		return ObjectMapperBasedVoMapper.convert(command, ConcertOptionCreateDomainServiceRequest.class);
	}
}