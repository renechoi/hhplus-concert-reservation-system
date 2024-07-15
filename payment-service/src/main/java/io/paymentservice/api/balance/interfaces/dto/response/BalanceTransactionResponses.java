package io.paymentservice.api.balance.presentation.dto.response;

import java.util.List;

import io.paymentservice.api.balance.business.dto.outport.BalanceTransactionInfos;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
public record BalanceTransactionResponses(List<BalanceTransactionResponse> balanceTransactionResponses) {
	public static BalanceTransactionResponses from(BalanceTransactionInfos pointHistories) {
		List<BalanceTransactionResponse> pointHistoryInfos = pointHistories.balanceTransactionInfos().stream()
			.map(BalanceTransactionResponse::from)
			.toList();
		return new BalanceTransactionResponses(pointHistoryInfos);
	}
}
