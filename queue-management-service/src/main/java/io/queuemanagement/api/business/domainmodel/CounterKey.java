package io.queuemanagement.api.business.domainmodel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */

@Getter
@RequiredArgsConstructor
public enum CounterKey {
	WAITING_QUEUE_COUNTER("waitingQueueCounter"),
	PROCESSING_QUEUE_COUNTER("processingQueueCounter");

	private final String value;
}
