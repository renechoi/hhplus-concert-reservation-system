package io.reservationservice.api.business.domainentity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.reservationservice.api.business.dto.event.ReservationChangedEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EntityListeners({AuditingEntityListener.class})
public class Reservation extends AbstractAggregateRoot<Reservation> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reservationId;

	private Long userId;

	@ManyToOne
	@JoinColumn(name = "concert_option_id")
	private ConcertOption concertOption;

	@ManyToOne
	@JoinColumn(name = "seat_id")
	private Seat seat;



	private Boolean isCanceled;



	private LocalDateTime temporalReservationReserveAt;
	private LocalDateTime reserveAt;

	@CreatedDate
	@Column(updatable = false)
	@Setter
	private LocalDateTime createdAt;

	@Setter
	private LocalDateTime requestAt;

	@PostPersist
	@PostUpdate
	@PostRemove
	private void publishReservationChangedEvent() {
		registerEvent(new ReservationChangedEvent(this.userId, this.concertOption.getConcertOptionId()));
	}

}
