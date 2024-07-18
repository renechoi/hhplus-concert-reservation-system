package io.messageservice.api.infrastructure.core;

import org.springframework.stereotype.Repository;

import io.messageservice.api.business.model.entity.SlackChannel;
import io.messageservice.api.business.persistence.SlackChannelRetrieveRepository;
import io.messageservice.api.infrastructure.orm.SlackChannelJpaRepository;
import io.messageservice.common.exception.definitions.ItemNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */
@Repository
@RequiredArgsConstructor
public class SlackChannelRetrieveCoreRepository implements SlackChannelRetrieveRepository {
	private final SlackChannelJpaRepository slackChannelJpaRepository;

	@Override
	public SlackChannel findByChannelNameWithThrows(String channelName) {
		return slackChannelJpaRepository.findByChannelName(channelName).orElseThrow(ItemNotFoundException::new);
	}
}
