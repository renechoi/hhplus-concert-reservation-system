package io.reservationservice.api.business.domainentity;

import io.reservationservice.api.business.dto.inport.OutboxStatusCompleteCommand;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


/**
 * @author : Rene Choi
 * @since : 2024/08/15
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
@Table(
	indexes = {
		@Index(name = "idx_status_createdAt", columnList = "status, createdAt")
	}
)
public class OutboxEvent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long outboxEventId;

	@Column(nullable = false)
	private String eventType;

	@Column(nullable = false, length = 5000)
	private String payload;

	@Column(nullable = false)
	private String topic;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OutboxStatus status = OutboxStatus.INIT;

	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime createdAt;

	public OutboxEvent(String eventType, String payload, String topic) {
		this.eventType = eventType;
		this.payload = payload;
		this.topic = topic;
	}

	public static OutboxEvent createConfirmOutbox(String payload, String topic) {
		return OutboxEvent.builder().eventType("ReservationConfirmEvent").status(OutboxStatus.INIT).topic(topic).payload(payload).build();
	}


	public void complete() {
		this.status=OutboxStatus.SENT;
	}
}
