package io.reservationservice.api.business.domainentity;

import static io.reservationservice.util.YmlLoader.*;
import static java.time.LocalDateTime.*;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.reservationservice.api.business.dto.event.ReservationChangedEvent;
import io.reservationservice.api.business.dto.event.TemporalReservationChangedEvent;
import io.reservationservice.api.business.dto.inport.ReservationCreateCommand;
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
public class TemporalReservation extends AbstractAggregateRoot<TemporalReservation> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long temporalReservationId;

	private Long userId;

	@ManyToOne
	@JoinColumn(name = "concert_option_id")
	private ConcertOption concertOption;

	@ManyToOne
	@JoinColumn(name = "seat_id")
	private Seat seat;

	private Boolean isConfirmed;

	private LocalDateTime reserveAt;

	private LocalDateTime expireAt;

	@CreatedDate
	@Column(updatable = false)
	@Setter
	private LocalDateTime createdAt;

	@Setter
	private LocalDateTime requestAt;

	private Boolean isCanceled;



	@PostPersist
	@PostUpdate
	@PostRemove
	private void publishTemporalReservationChangedEvent() {
		registerEvent(new TemporalReservationChangedEvent(this.userId, this.concertOption.getConcertOptionId()));
	}

	public static TemporalReservationBuilder defaultBuilder(){
		return TemporalReservation.builder().reserveAt(now()).isConfirmed(false);
	}

	public static TemporalReservation create(ReservationCreateCommand command, ConcertOption concertOption, Seat seat) {
		return defaultBuilder().userId(command.getUserId()).requestAt(command.getRequestAt()).concertOption(concertOption).seat(seat).expireAt(calculateExpireAt()).build();
	}

	public void confirm() {
		this.isConfirmed = true;
	}

	public void cancelConfirm(){
		this.isConfirmed = false;
		this.isCanceled = true;
	}

	public Reservation toConfirmedReservation() {
		return Reservation.builder()
			.userId(this.userId)
			.concertOption(this.concertOption)
			.seat(this.seat)
			.temporalReservationReserveAt(this.reserveAt)
			.reserveAt(now())
			.build();

	}

	public Seat cancelSeat() {
		this.seat.cancelReservation();
		return this.getSeat();
	}


	private static LocalDateTime calculateExpireAt() {
		return now().plusSeconds(ymlLoader().getTemporalReservationExpireSeconds());
	}

	public void expire() {
		this.cancelSeat();
		this.cancelConfirm();
	}

	public void cancel() {
		this.cancelSeat();
		this.cancelConfirm();
	}
}
