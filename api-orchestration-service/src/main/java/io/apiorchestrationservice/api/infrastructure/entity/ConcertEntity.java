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
public class ConcertEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long concertId;

	private String title;
}
