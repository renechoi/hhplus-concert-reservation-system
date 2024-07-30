package io.queuemanagement.api.infrastructure.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.queuemanagement.api.business.domainmodel.QueueStatus;
import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import io.queuemanagement.common.mapper.ObjectMapperBasedVoMapper;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(uniqueConstraints = {
	@UniqueConstraint(name = "uc_userid_status", columnNames = {"userId", "status"})
})
public class WaitingQueueTokenEntity implements EntityRecordable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long waitingQueueTokenId;
	private String userId;
	private String tokenValue;
	private Long position;
	private LocalDateTime validUntil;
	@Enumerated(EnumType.STRING)
	private QueueStatus status;

	@CreatedDate
	@Column(updatable = false)
	@Setter
	private LocalDateTime createdAt;

	@Setter
	private LocalDateTime requestAt;




	public static WaitingQueueTokenEntity from(WaitingQueueToken waitingQueueToken) {
		return ObjectMapperBasedVoMapper.convert(waitingQueueToken, WaitingQueueTokenEntity.class);
	}

	public WaitingQueueToken toDomain() {
		return ObjectMapperBasedVoMapper.convert(this, WaitingQueueToken.class);
	}


	public WaitingQueueTokenEntity updateStatus(QueueStatus status) {
		this.status = status;
		return this;
	}

	public WaitingQueueTokenEntity updatePosition(Long position) {
		this.position = position;
		return this;
	}
}
