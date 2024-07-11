package io.queuemanagement.api.infrastructure.entity;

import java.time.LocalDateTime;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public interface EntityRecordable {
	LocalDateTime getCreatedAt();
	void setCreatedAt(LocalDateTime createdAt);
	LocalDateTime getRequestAt();
	void setRequestAt(LocalDateTime requestAt);

}
