package io.apiorchestrationservice.api.infrastructure.clients.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public record UserBalanceUseDomainServiceResponse(
    Long userBalanceId,
    Long userId,
    BigDecimal amount,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
