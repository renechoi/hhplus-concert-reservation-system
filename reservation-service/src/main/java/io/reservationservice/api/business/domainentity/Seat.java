package io.reservationservice.api.business.domainentity;

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
 * @since : 2024/07/03
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EntityListeners({AuditingEntityListener.class})
public class Seat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seatId;

	@ManyToOne
	@JoinColumn(name = "concert_option_id")
	private ConcertOption concertOption;

	private Long seatNumber;
	private Boolean occupied;

	@CreatedDate
	@Column(updatable = false)
	@Setter
	private LocalDateTime createdAt;

	public static Seat createSeatWithConcertOptionAndCommand(ConcertOption concertOption, ReservationCreateCommand command) {
		return Seat.builder().concertOption(concertOption).seatNumber(command.getSeatNumber()).occupied(false).build();
	}

	public static Seat createSeatWithConcertOptionAndNumber(ConcertOption concertOption, Long seatNumber) {
		return Seat.builder().concertOption(concertOption).seatNumber(seatNumber).occupied(false).build();
	}

	public boolean isNotOccupied() {
		return !isOccupied();
	}

	public boolean isOccupied() {
		return this.occupied != null && this.occupied;
	}

	public Seat doReserve() {
		this.occupied = true;
		return this;
	}

	public Seat cancelReservation() {
		this.occupied = false;
		return this;
	}


	public boolean isAvailable(LocalDateTime date) {
		return !isOccupied() && concertOption.isConcertDateAfter(date);
	}
}
