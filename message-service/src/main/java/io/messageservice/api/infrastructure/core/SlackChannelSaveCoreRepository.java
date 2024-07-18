package io.messageservice.api.infrastructure.core;

import org.springframework.stereotype.Repository;

import io.messageservice.api.business.model.entity.SlackChannel;
import io.messageservice.api.business.persistence.SlackChannelSaveRepository;
import io.messageservice.api.infrastructure.orm.SlackChannelJpaRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */
@Repository
@RequiredArgsConstructor
public class SlackChannelSaveCoreRepository implements SlackChannelSaveRepository {
	private final SlackChannelJpaRepository slackChannelJpaRepository;

	@Override
	public SlackChannel save(SlackChannel slackChannel) {
		return slackChannelJpaRepository.save(slackChannel);
	}
}
