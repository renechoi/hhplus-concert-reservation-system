package io.reservationservice.api.business.domainentity;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EntityListeners({AuditingEntityListener.class})
public class ConcertOption implements EntityRecordable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long concertOptionId;

	@ManyToOne
	@JoinColumn(name = "concert_id")
	private Concert concert;

	private LocalDateTime concertDate;
	private Duration concertDuration;
	private String title;
	private String description;
	private BigDecimal price;

	@CreatedDate
	@Column(updatable = false)
	@Setter
	private LocalDateTime createdAt;

	@Setter
	private LocalDateTime requestAt;

	public ConcertOption withConcert(Concert concert) {
		this.concert = concert;
		return this;
	}

	public boolean isConcertDateAfter(LocalDateTime date) {
		return concertDate.isAfter(date);
	}
}
