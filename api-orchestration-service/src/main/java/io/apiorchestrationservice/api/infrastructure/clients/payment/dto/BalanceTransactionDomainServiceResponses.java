package io.apiorchestrationservice.api.infrastructure.clients.payment.dto;

import java.util.List;
/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public record BalanceTransactionDomainServiceResponses(
    List<BalanceTransactionDomainServiceResponse> balanceTransactionResponses
) {}
