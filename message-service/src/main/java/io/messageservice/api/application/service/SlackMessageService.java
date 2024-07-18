package io.messageservice.api.application.service;

import org.springframework.stereotype.Service;

import io.messageservice.api.application.dto.request.SlackChannelRegistrationRequest;
import io.messageservice.api.application.dto.request.SlackMessageReserveRequest;
import io.messageservice.api.application.dto.response.SlackChannelRegistrationResponse;
import io.messageservice.api.application.dto.response.SlackMessageReserveResponse;
import io.messageservice.api.business.model.dto.outport.SlackChannelInfo;
import io.messageservice.api.business.model.dto.outport.SlackMessageReserveInfo;
import io.messageservice.api.business.operators.SlackChannelRegistrar;
import io.messageservice.api.business.operators.SlackChannelRetriever;
import io.messageservice.api.business.operators.SlackMessageReservationManager;
import io.messageservice.api.business.operators.SlackMessageSender;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */
@Service
@RequiredArgsConstructor
public class SlackMessageService {

	private final SlackChannelRegistrar slackChannelRegistrar;
	private final SlackChannelRetriever slackChannelRetriever;
	private final SlackMessageSender slackMessageSender;
	private final SlackMessageReservationManager slackMessageReservationManager;

	public SlackChannelRegistrationResponse register(SlackChannelRegistrationRequest request) {
		return SlackChannelRegistrationResponse.from(slackChannelRegistrar.save(request.toCommand()));
	}

	public SlackMessageReserveResponse reserve(SlackMessageReserveRequest slackMessageReserveRequest) {
		SlackChannelInfo slackChannelInfo = slackChannelRetriever.retrieveSlackChannelByName(slackMessageReserveRequest.getChannelName());
		return SlackMessageReserveResponse.from(slackMessageReservationManager.reserve(slackMessageReserveRequest.toCommand().withChannelInfo(slackChannelInfo)));
	}

	public void sendReservedMessages() {
		SlackMessageReserveInfo slackMessageReserveInfo = slackMessageReservationManager.popNextReservedMessage();
		if (slackMessageReserveInfo.isEmpty()) {
			return;
		}
		slackMessageSender.sendAsync(slackMessageReserveInfo.toCommand());
		slackMessageReservationManager.markAsSentAsync(slackMessageReserveInfo.id());
	}
}
