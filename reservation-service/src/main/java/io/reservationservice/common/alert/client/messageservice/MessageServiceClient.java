package io.reservationservice.common.alert.client.messageservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.reservationservice.common.alert.client.messageservice.dto.SlackMessageReserveRequest;
import io.reservationservice.common.model.CommonApiResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */
@FeignClient(
	name = "message-service"
)
public interface MessageServiceClient {

	@PostMapping("/message-service/api/message/slack/slack/reserve")
	CommonApiResponse<Void> reserveSlackMessage(@RequestBody SlackMessageReserveRequest slackMessageReserveRequest);
}
