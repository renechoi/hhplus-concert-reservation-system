package io.queuemanagement.api.application.dto.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenGenerateRequest {
	private String userId;
	private LocalDateTime requestedAt;
	private int priority; // 우선순위 (예: 1 - 일반, 2 - VIP)
}
