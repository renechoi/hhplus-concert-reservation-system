package io.reservationservice.api.business.dto.inport;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import io.reservationservice.api.business.domainentity.Concert;
import io.reservationservice.api.business.domainentity.ConcertOption;
import io.reservationservice.api.business.dto.common.AbstractCommonRequestInfo;
import io.reservationservice.common.mapper.ObjectMapperBasedVoMapper;
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
public class ConcertOptionCreateCommand extends AbstractCommonRequestInfo {
	private LocalDateTime concertDate;
	private Duration concertDuration;
	private String title;
	private String description;
	private BigDecimal price;
	private int maxSeats;
	public ConcertOption toEntityWithConcert(Concert concert) {
		return ObjectMapperBasedVoMapper.convert(this, ConcertOption.class).withConcert(concert);
	}
}