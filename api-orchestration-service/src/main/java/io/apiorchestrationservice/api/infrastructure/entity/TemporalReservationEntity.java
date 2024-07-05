package io.apiorchestrationservice.api.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Entity
public class TemporalReservationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long temporalReservationId;

	private Long userId;
	private Long concertOptionId;
	private Long seatId;
}
