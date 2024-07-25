package io.paymentservice.api.balance.business.dto.inport;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BalanceSearchCommand implements DateSearchCommand {
	private Long balanceId;
	private Long userId;
	private BigDecimal amount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private DateSearchCondition dateSearchCondition;
	private DateSearchTarget dateSearchTarget;

	public static BalanceSearchCommand userIdWithMinAmount(Long userId, BigDecimal minAmount) {
		return BalanceSearchCommand.builder()
			.userId(userId)
			.amount(minAmount)
			.dateSearchCondition(DateSearchCondition.AFTER)
			.dateSearchTarget(DateSearchTarget.CREATED_AT)
			.build();
	}

	public static BalanceSearchCommand onUser(BalanceUseCommand command) {
		return BalanceSearchCommand.builder().userId(command.getUserId()).build();
	}

	public static BalanceSearchCommand onUser(BalanceChargeCommand command) {
		return BalanceSearchCommand.builder().userId(command.getUserId()).build();
	}
}