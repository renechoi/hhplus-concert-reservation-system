package io.queuemanagement.api.business.dto.outport;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public record WaitingQueueTokenEnqueueInfo(
	Long waitingQueueTokenId
) {



	public static WaitingQueueTokenEnqueueInfo createEmpty() {
		return new WaitingQueueTokenEnqueueInfo(0L);
	}
}
