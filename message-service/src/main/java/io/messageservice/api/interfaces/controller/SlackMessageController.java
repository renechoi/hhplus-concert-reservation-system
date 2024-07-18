package io.messageservice.api.interfaces.controller;

import static io.messageservice.common.model.CommonApiResponse.*;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.messageservice.api.application.dto.request.SlackChannelRegistrationRequest;
import io.messageservice.api.application.dto.request.SlackMessageReserveRequest;
import io.messageservice.api.application.dto.response.SlackChannelRegistrationResponse;
import io.messageservice.api.application.service.SlackMessageService;
import io.messageservice.common.model.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */

@RestController
@RequestMapping("/api/message/slack")
@RequiredArgsConstructor
@Tag(name = "slack message API")
public class SlackMessageController {
	private final SlackMessageService slackMessageService;


	@Operation(summary = "슬랙 채널 등록")
	@PostMapping("/slack/channel")
	public CommonApiResponse<SlackChannelRegistrationResponse> registerChannel(@RequestBody @Validated SlackChannelRegistrationRequest registrationRequest) {
		return created(slackMessageService.register(registrationRequest));
	}


	@Operation(summary = "슬랙 메시지 전송 예약")
	@PostMapping("/slack/reserve")
	public CommonApiResponse<Void> reserveSlackMessage(@RequestBody @Validated SlackMessageReserveRequest slackMessageReserveRequest) {
		slackMessageService.reserve(slackMessageReserveRequest);
		return OK();
	}
}
