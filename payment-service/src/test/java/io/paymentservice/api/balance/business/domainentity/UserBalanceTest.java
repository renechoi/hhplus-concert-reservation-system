package io.paymentservice.api.balance.business.domainentity;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.paymentservice.api.balance.business.dto.event.UserBalanceChargeEvent;
import io.paymentservice.api.balance.business.dto.event.UserBalanceUseEvent;
import io.paymentservice.common.exception.UserBalanceUseUnAvailableException;

/**
 * @author : Rene Choi
 * @since : 2024/07/12
 */

@ExtendWith(MockitoExtension.class)
@DisplayName("UserBalance 도메인 엔티티 테스트")
public class UserBalanceTest {

	@InjectMocks
	private UserBalance userBalance;

	@Mock
	private UserBalanceChargeEvent userBalanceChargeEvent;

	@Mock
	private UserBalanceUseEvent userBalanceUseEvent;

	@BeforeEach
	void setUp() {
		userBalance = UserBalance.createDefaultNewUserBalance(1L);
	}

	@Test
	@DisplayName("금액 추가 테스트")
	void testAddAmount() {
		// given
		BigDecimal amountToAdd = BigDecimal.valueOf(100);
		TransactionReason reason = TransactionReason.NORMAL;

		// when
		userBalance.addAmount(amountToAdd, reason);

		// then
		assertEquals(amountToAdd, userBalance.getAmount());
	}

	@Test
	@DisplayName("금액 차감 성공 테스트")
	void testDeductAmount_Success() {
		// given
		BigDecimal initialAmount = BigDecimal.valueOf(200);
		userBalance.addAmount(initialAmount, TransactionReason.NORMAL);

		BigDecimal amountToDeduct = BigDecimal.valueOf(100);
		TransactionReason reason = TransactionReason.NORMAL;

		// when
		userBalance.deductAmount(amountToDeduct, reason);

		// then
		assertEquals(initialAmount.subtract(amountToDeduct), userBalance.getAmount());
	}

	@Test
	@DisplayName("잔액 부족으로 인한 금액 차감 실패 테스트")
	void testDeductAmount_InsufficientFunds() {
		// given
		BigDecimal initialAmount = BigDecimal.valueOf(50);
		userBalance.addAmount(initialAmount, TransactionReason.NORMAL);

		BigDecimal amountToDeduct = BigDecimal.valueOf(100);

		// when / then
		assertThrows(UserBalanceUseUnAvailableException.class, () -> {
			userBalance.deductAmount(amountToDeduct, TransactionReason.NORMAL);
		});
	}
}
