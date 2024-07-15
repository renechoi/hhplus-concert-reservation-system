package io.reservationservice.api.business.domainentity;

import static java.time.LocalDateTime.*;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.reservationservice.api.business.dto.inport.ReservationCreateCommand;
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
 * @since : 2024/07/07
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EntityListeners({AuditingEntityListener.class})
public class TemporaryReservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long temporaryReservationId;

	private Long userId;

	@ManyToOne
	@JoinColumn(name = "concert_option_id")
	private ConcertOption concertOption;

	@ManyToOne
	@JoinColumn(name = "seat_id")
	private Seat seat;

	private Boolean isConfirmed;

	private LocalDateTime reserveAt; // todo -> 별도의 expireAt이 필요한가?

	@CreatedDate
	@Column(updatable = false)
	@Setter
	private LocalDateTime createdAt;

	@Setter
	private LocalDateTime requestAt;

	private Boolean isCanceled;

	public static TemporaryReservationBuilder defaultBuilder(){
		return TemporaryReservation.builder().reserveAt(now()).isConfirmed(false);
	}

	public static TemporaryReservation of(ReservationCreateCommand command, ConcertOption concertOption, Seat seat) {
		return defaultBuilder().userId(command.getUserId()).requestAt(command.getRequestAt()).concertOption(concertOption).seat(seat).build();
	}

	public void confirm() {
		this.isConfirmed = true;
	}

	public void cancelConfirm(){
		this.isConfirmed = false;
	}

	public Reservation toConfirmedReservation() {
		return Reservation.builder()
			.userId(this.userId)
			.concertOption(this.concertOption)
			.seat(this.seat)
			.temporaryReservationReserveAt(this.reserveAt)
			.reserveAt(now())
			.build();

	}

	public Seat doCancelSeat() {
		this.seat.cancelReservation();
		return this.getSeat();
	}
}
