package io.messageservice.api.business.model.entity;

import static java.time.LocalDateTime.*;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(indexes = {
	@Index(name = "idx_reservedAt_sent", columnList = "reservedAt, sent")
})
public class SlackMessage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String token;
	private String channelId;

	private String message;
	private LocalDateTime reservedAt;
	private boolean sent;
	private boolean popped;

	public SlackMessage init() {
		this.reservedAt = now();
		this.sent = false;
		return this;
	}

	public void markAsSent() {
		this.popped = true;
	}

	public void markAsPopped() {
		this.popped = true;
	}
}
