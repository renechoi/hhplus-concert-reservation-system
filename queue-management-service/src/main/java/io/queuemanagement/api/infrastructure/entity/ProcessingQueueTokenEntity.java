package io.queuemanagement.api.infrastructure.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.queuemanagement.api.business.domainmodel.ProcessingQueueToken;
import io.queuemanagement.api.business.domainmodel.QueueStatus;
import io.queuemanagement.common.mapper.ObjectMapperBasedVoMapper;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EntityListeners({AuditingEntityListener.class})
@Slf4j
public class ProcessingQueueTokenEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long processingQueueTokenId;
	private String userId;
	private String tokenValue;
	private LocalDateTime validUntil;
	@Enumerated(EnumType.STRING)
	private QueueStatus status;

	@CreatedDate
	@Column(updatable = false)
	@Setter
	private LocalDateTime createdAt;


	public static ProcessingQueueTokenEntity from(ProcessingQueueToken processingQueueToken) {
		return ObjectMapperBasedVoMapper.convert(processingQueueToken, ProcessingQueueTokenEntity.class);
	}

	public ProcessingQueueToken toDomain() {
		return ObjectMapperBasedVoMapper.convert(this, ProcessingQueueToken.class);
	}
}
