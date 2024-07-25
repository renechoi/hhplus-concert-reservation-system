package io.reservationservice.api.business.dto.inport;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import io.reservationservice.api.business.domainentity.Concert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConcertOptionSearchCommand implements DateSearchCommand{
	private Long concertOptionId;
	private Concert concert;
	private Long concertId;
	private LocalDateTime concertDate;
	private Duration concertDuration;
	private String title;
	private String description;
	private BigDecimal price;
	private LocalDateTime createdAt;
	private LocalDateTime requestAt;


	private DateSearchCondition dateSearchCondition;
	private DateSearchTarget dateSearchTarget;

	public static ConcertOptionSearchCommand onUpcomingDates(Long concertId) {
		return ConcertOptionSearchCommand.builder()
			.concertId(concertId)
			.concertDate(LocalDateTime.now())
			.dateSearchCondition(DateSearchCondition.AFTER)
			.dateSearchTarget(DateSearchTarget.CONCERT_DATE)
			.build();
	}
}
