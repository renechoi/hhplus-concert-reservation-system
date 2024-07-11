package io.paymentservice.api.balance.business.domainentity;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public enum TransactionReason {
	NORMAL,
	PAYMENT,
	REFUND,
	ROLLED_BACK,
	ADJUSTMENT
}