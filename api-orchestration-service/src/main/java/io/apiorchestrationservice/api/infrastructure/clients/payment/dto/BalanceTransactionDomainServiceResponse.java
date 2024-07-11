package io.apiorchestrationservice.api.infrastructure.clients.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public record BalanceTransactionDomainServiceResponse(
    Long transactionId,
    Long userId,
    BigDecimal amount,
    String transactionType,
    LocalDateTime createdAt
) {}