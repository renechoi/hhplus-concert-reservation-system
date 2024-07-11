package io.apiorchestrationservice.api.infrastructure.clients.reservation.dto;

import java.time.LocalDateTime;

/**
 * @author : Rene Choi
 * @since : 2024/07/05
 */
public interface CommonRequestInfo {
	LocalDateTime getRequestAt();
}
