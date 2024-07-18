package io.messageservice.api.infrastructure.orm;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.messageservice.api.business.model.entity.SlackChannel;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */
public interface SlackChannelJpaRepository extends JpaRepository<SlackChannel, Long> {
	Optional<SlackChannel> findByChannelName(String channelName);
}
