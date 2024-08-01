package io.queuemanagement.api.business.domainmodel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
@Getter
@RequiredArgsConstructor
public enum QueueName {
	WAITING_QUEUE("waitingQueue"),
	PROCESSING_QUEUE("processingQueue");

	private final String value;
}
