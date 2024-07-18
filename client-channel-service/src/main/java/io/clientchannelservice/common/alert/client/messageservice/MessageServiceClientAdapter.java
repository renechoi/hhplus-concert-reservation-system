package io.clientchannelservice.common.alert.client.messageservice;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import io.clientchannelservice.common.alert.client.messageservice.dto.SlackMessageReserveRequest;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */

@Component
@RequiredArgsConstructor
public class MessageServiceClientAdapter {

	private final RestTemplate restTemplate;

	public void reserveSlackMessage(SlackMessageReserveRequest slackMessageReserveRequest) {
		String url = "http://message-service/message-service/api/message/slack/slack/reserve";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<SlackMessageReserveRequest> requestEntity = new HttpEntity<>(slackMessageReserveRequest, headers);

		ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);
	}
}