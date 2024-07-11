package io.queuemanagement.api.infrastructure.entity;

import io.queuemanagement.api.business.domainmodel.WaitingQueuePositionJson;
import io.queuemanagement.common.mapper.ObjectMapperBasedVoMapper;
import jakarta.persistence.Column;
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
 * @since : 2024/07/11
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WaitingQueuePositionJsonEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long waitingQueuePositionJsonId;

	@Column(columnDefinition = "TEXT")
	private String positionJson;

	public static WaitingQueuePositionJsonEntity from(WaitingQueuePositionJson waitingQueuePositionJson) {
		return WaitingQueuePositionJsonEntity.builder()
			.waitingQueuePositionJsonId(waitingQueuePositionJson.getWaitingQueuePositionJsonId())
			.positionJson(waitingQueuePositionJson.getPositionJson())
			.build();
	}

	public WaitingQueuePositionJsonEntity updatePositionJson(String positionJson) {
		this.positionJson = positionJson;
		return this;
	}

	public WaitingQueuePositionJson toDomain() {
		return ObjectMapperBasedVoMapper.convert(this, WaitingQueuePositionJson.class);
	}
}
