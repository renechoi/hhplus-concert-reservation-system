package io.reservationservice.api.business.domainentity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author : Rene Choi
 * @since : 2024/08/15
 */
public interface OutboxRepository {
	OutboxEvent save(OutboxEvent outboxEvent);

	Optional<OutboxEvent> fetch(Long outboxEventId);

	List<OutboxEvent> fetchUnsentEvents(LocalDateTime localDateTime);

}
