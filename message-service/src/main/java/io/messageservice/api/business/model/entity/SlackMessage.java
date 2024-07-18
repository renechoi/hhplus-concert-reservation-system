package io.messageservice.api.business.model.entity;

import static java.time.LocalDateTime.*;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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

	@Column(length = 10000)
	private String message;
	private LocalDateTime reservedAt;
	private boolean sent;
	private boolean popped;


	@PrePersist
	@PreUpdate
	public void truncateMessage() {
		if (message != null && message.length() > 10000) {
			message = message.substring(0, 10000);
		}
	}

	public SlackMessage init() {
		this.reservedAt = now();
		this.sent = false;
		return this;
	}



	public void markAsSent() {
		this.sent = true;
	}

	public void markAsPopped() {
		this.popped = true;
	}
}
