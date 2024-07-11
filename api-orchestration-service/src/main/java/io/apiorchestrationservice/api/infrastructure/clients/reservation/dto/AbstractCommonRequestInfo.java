package io.apiorchestrationservice.api.infrastructure.clients.reservation.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author : Rene Choi
 * @since : 2024/07/05
 */
@Data
public class AbstractCommonRequestInfo implements CommonRequestInfo {

	@NotNull
	private LocalDateTime requestAt;
}
