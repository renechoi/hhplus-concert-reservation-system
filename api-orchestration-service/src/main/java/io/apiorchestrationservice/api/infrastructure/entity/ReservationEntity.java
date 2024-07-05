package io.apiorchestrationservice.api.infrastructure.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Entity
public class ReservationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reservationId;

	private Long userId;
	private Long concertOptionId;
	private Long seatId;
	private LocalDateTime reservationDate;
}
