package io.reservationservice.api.business.domainentity;

import static io.reservationservice.common.model.GlobalResponseCode.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.reservationservice.api.business.dto.event.SeatChangedEvent;
import io.reservationservice.common.exception.definitions.ReservationUnAvailableException;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.Table;
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
@Table(
	indexes = {
		@Index(name = "idx_seat_number", columnList = "seatNumber"),
		@Index(name = "idx_concert_option_seat_number", columnList = "concertOptionId, seatNumber"),
		@Index(name = "idx_concert_option_occupied", columnList = "concertOptionId, occupied"),
	}
)
public class Seat extends AbstractAggregateRoot<Seat> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seatId;
	//
	// @Version
	// private Long version;

	@ManyToOne
	@JoinColumn(name = "concert_option_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private ConcertOption concertOption;

	private Long seatNumber;
	private Boolean occupied;

	@CreatedDate
	@Column(updatable = false)
	@Setter
	private LocalDateTime createdAt;


	@PostPersist
	@PostUpdate
	@PostRemove
	private void publishSeatChangedEvent() {
		registerEvent(new SeatChangedEvent(this.concertOption.getConcertOptionId(), this.seatId));
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
		if (isOccupied()) {
			throw new ReservationUnAvailableException(SEAT_ALREADY_RESERVED);
		}
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

	public boolean isAvailableAt(Long requestAt) {
		LocalDateTime date = Instant.ofEpochMilli(requestAt).atZone(ZoneOffset.UTC).toLocalDateTime();
		return isAvailable(date);
	}

	public void occupy() {
		if (isOccupied()) {
			throw new ReservationUnAvailableException(SEAT_ALREADY_RESERVED);
		}
	}
}
