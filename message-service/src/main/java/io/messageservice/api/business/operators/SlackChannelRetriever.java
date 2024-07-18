package io.messageservice.api.business.operators;

import org.springframework.stereotype.Component;

import io.messageservice.api.business.model.dto.outport.SlackChannelInfo;
import io.messageservice.api.business.persistence.SlackChannelRetrieveRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */
@Component
@RequiredArgsConstructor
public class SlackChannelRetriever {
	private final SlackChannelRetrieveRepository slackChannelRetrieveRepository;

	public SlackChannelInfo retrieveSlackChannelByName(String channelName) {
		return SlackChannelInfo.from(slackChannelRetrieveRepository.findByChannelNameWithThrows(channelName));
	}
}
