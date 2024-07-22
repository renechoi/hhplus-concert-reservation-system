package io.paymentservice.api.balance.business.dto.outport;

import java.util.List;

import io.paymentservice.api.balance.business.entity.BalanceTransaction;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
public record BalanceTransactionInfos(
	List<BalanceTransactionInfo> balanceTransactionInfos
) {
	public static BalanceTransactionInfos from(List<BalanceTransaction> pointHistories) {
		List<BalanceTransactionInfo> pointHistoryInfos = pointHistories.stream()
			.map(BalanceTransactionInfo::from)
			.toList();
		return new BalanceTransactionInfos(pointHistoryInfos);
	}
}
