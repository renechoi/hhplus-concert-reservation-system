package io.apiorchestrationservice.common.alert.client.messageservice;

import org.springframework.stereotype.Component;

import io.apiorchestrationservice.common.alert.client.messageservice.dto.SlackMessageReserveRequest;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */

@Component
@RequiredArgsConstructor
public class MessageServiceClientAdapter {
	private final MessageServiceClient messageServiceClient;

	public void reserveSlackMessage(SlackMessageReserveRequest slackMessageReserveRequest) {
		messageServiceClient.reserveSlackMessage(slackMessageReserveRequest);
	}

}