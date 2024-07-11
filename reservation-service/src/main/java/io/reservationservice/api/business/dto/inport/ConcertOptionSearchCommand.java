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
public class ConcertOptionSearchCommand {
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


	private String dateSearchCondition;

	// 이 메서드를 사용하여 concertId로 검색하고 concertDate가 현재 시점 이후인 것만 필터링합니다.
	public static ConcertOptionSearchCommand searchByConcertIdWithFutureConcertDates(Long concertId) {
		return ConcertOptionSearchCommand.builder()
			.concertId(concertId)
			.concertDate(LocalDateTime.now())
			.dateSearchCondition("after")
			.build();
	}
}
