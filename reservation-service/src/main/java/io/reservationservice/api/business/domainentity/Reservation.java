package io.reservationservice.api.business.domainentity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.reservationservice.api.business.dto.event.ReservationChangedEvent;
import io.reservationservice.common.mapper.ObjectMapperBasedVoMapper;
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
import lombok.SneakyThrows;

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
@Table(
	indexes = {
		@Index(name = "idx_user_concert_option", columnList = "userId, concertOptionId"),
		@Index(name = "idx_concert_option_seat", columnList = "concertOptionId, seatId"),
	}
)
public class Reservation extends AbstractAggregateRoot<Reservation> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reservationId;

	private Long userId;

	@ManyToOne
	@JoinColumn(name = "concert_option_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private ConcertOption concertOption;

	@ManyToOne
	@JoinColumn(name = "seat_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
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


	@SneakyThrows
	public String toJson() {
		return ObjectMapperBasedVoMapper.getObjectMapper().writeValueAsString(this);
	}
}
