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

import io.paymentservice.api.balance.business.dto.event.BalanceChargeEvent;
import io.paymentservice.api.balance.business.dto.event.BalanceUseEvent;
import io.paymentservice.common.exception.definitions.BalanceUseUnAvailableException;

/**
 * @author : Rene Choi
 * @since : 2024/07/12
 */

@ExtendWith(MockitoExtension.class)
@DisplayName("Balance 도메인 엔티티 테스트")
public class BalanceTest {

	@InjectMocks
	private Balance balance;

	@Mock
	private BalanceChargeEvent balanceChargeEvent;

	@Mock
	private BalanceUseEvent balanceUseEvent;

	@BeforeEach
	void setUp() {
		balance = Balance.createDefaultNewBalance(1L);
	}

	@Test
	@DisplayName("금액 추가 테스트")
	void testAddAmount() {
		// given
		BigDecimal amountToAdd = BigDecimal.valueOf(100);
		TransactionReason reason = TransactionReason.NORMAL;

		// when
		balance.charge(amountToAdd, reason);

		// then
		assertEquals(amountToAdd, balance.getAmount());
	}

	@Test
	@DisplayName("금액 차감 성공 테스트")
	void testDeductAmount_Success() {
		// given
		BigDecimal initialAmount = BigDecimal.valueOf(200);
		balance.charge(initialAmount, TransactionReason.NORMAL);

		BigDecimal amountToDeduct = BigDecimal.valueOf(100);
		TransactionReason reason = TransactionReason.NORMAL;

		// when
		balance.use(amountToDeduct, reason);

		// then
		assertEquals(initialAmount.subtract(amountToDeduct), balance.getAmount());
	}

	@Test
	@DisplayName("잔액 부족으로 인한 금액 차감 실패 테스트")
	void testDeductAmount_InsufficientFunds() {
		// given
		BigDecimal initialAmount = BigDecimal.valueOf(50);
		balance.charge(initialAmount, TransactionReason.NORMAL);

		BigDecimal amountToDeduct = BigDecimal.valueOf(100);

		// when / then
		assertThrows(BalanceUseUnAvailableException.class, () -> {
			balance.use(amountToDeduct, TransactionReason.NORMAL);
		});
	}
}
