package io.queuemanagement.api.business.domainmodel;

import java.time.LocalDateTime;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public interface DomainRecordable {
	LocalDateTime getRequestAt();
}
