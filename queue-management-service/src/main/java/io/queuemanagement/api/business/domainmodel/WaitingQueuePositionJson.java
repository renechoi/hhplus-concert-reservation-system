package io.queuemanagement.api.business.domainmodel;

import io.queuemanagement.common.annotation.DomainModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@DomainModel
public class WaitingQueuePositionJson {
	private Long waitingQueuePositionJsonId;
	private String positionJson;

	public static WaitingQueuePositionJson createPositionJson(Long waitingQueuePositionJsonId, String positionJson) {
		return new WaitingQueuePositionJson(waitingQueuePositionJsonId, positionJson);
	}

	public static WaitingQueuePositionJson createDefault() {
		return new WaitingQueuePositionJson(null, "{}");
	}
}
