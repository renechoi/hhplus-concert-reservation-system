package io.reservationservice.api.business.dto.common;

import java.time.LocalDateTime;

/**
 * @author : Rene Choi
 * @since : 2024/07/05
 */
public interface CommonRequestInfo {
	LocalDateTime getRequestAt();
}
