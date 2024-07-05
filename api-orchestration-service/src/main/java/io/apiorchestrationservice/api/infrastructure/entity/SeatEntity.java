package io.apiorchestrationservice.api.infrastructure.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * @author : Rene Choi
 * @since : 2024/07/03
 */
@Entity
public class SeatEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seatId;

	private Long concertId;
	private String section;
	private String seatRow; // row => mysql 예약어
	private String seatNumber;
	private Boolean available;
	private LocalDateTime reservedUntil;
}
