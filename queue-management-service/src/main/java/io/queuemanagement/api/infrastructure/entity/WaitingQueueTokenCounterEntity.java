package io.queuemanagement.api.infrastructure.entity;

import io.queuemanagement.api.business.domainmodel.WaitingQueueTokenCounter;
import io.queuemanagement.common.mapper.ObjectMapperBasedVoMapper;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/05
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WaitingQueueTokenCounterEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long waitingQueueTokenCounterId;
	private Long count;

	public static WaitingQueueTokenCounterEntity from(WaitingQueueTokenCounter counter) {
		return ObjectMapperBasedVoMapper.convert(counter, WaitingQueueTokenCounterEntity.class);
	}

	public WaitingQueueTokenCounter toDomain() {
		return ObjectMapperBasedVoMapper.convert(this, WaitingQueueTokenCounter.class);
	}
}
