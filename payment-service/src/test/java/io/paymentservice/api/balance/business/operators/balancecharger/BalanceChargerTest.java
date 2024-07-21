package io.paymentservice.api.balance.business.operators.balancecharger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.paymentservice.api.balance.business.domainentity.Balance;
import io.paymentservice.api.balance.business.dto.inport.BalanceChargeCommand;
import io.paymentservice.api.balance.business.dto.outport.BalanceChargeInfo;
import io.paymentservice.api.balance.business.persistence.BalanceRepository;
import io.paymentservice.common.exception.definitions.BalanceChargeUnAvailableException;

/**
 * @author : Rene Choi
 * @since : 2024/07/12
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BalanceCharger 테스트")
public class BalanceChargerTest {

	@Mock
	private BalanceRepository balanceRepository;

	@InjectMocks
	private BalanceCharger balanceCharger;

	@Test
	@DisplayName("새로운 사용자 잔액 생성 및 저장 테스트")
	public void testCharge_NewBalanceCreatedAndSaved() {
		// given
		Long userId = 1L;
		BigDecimal amount = BigDecimal.valueOf(100);
		BalanceChargeCommand command = new BalanceChargeCommand(userId, amount, null);

		when(balanceRepository.findByUserIdOptional(userId)).thenReturn(Optional.empty());
		when(balanceRepository.save(any(Balance.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// when
		BalanceChargeInfo result = balanceCharger.charge(command);

		// then
		assertNotNull(result);
		assertEquals(userId, result.userId());
		assertEquals(amount, result.amount());
		verify(balanceRepository).findByUserIdOptional(userId);
		verify(balanceRepository).save(any(Balance.class));
	}

	@Test
	@DisplayName("기존 사용자 잔액 업데이트 및 저장 테스트")
	public void testCharge_ExistingBalanceUpdatedAndSaved() {
		// given
		Long userId = 1L;
		BigDecimal initialAmount = BigDecimal.valueOf(50);
		BigDecimal chargeAmount = BigDecimal.valueOf(100);
		BigDecimal expectedAmount = initialAmount.add(chargeAmount);
		Balance existingBalance = Balance.builder().userId(userId).amount(initialAmount).build();
		BalanceChargeCommand command = new BalanceChargeCommand(userId, chargeAmount, null);

		when(balanceRepository.findByUserIdOptional(userId)).thenReturn(Optional.of(existingBalance));
		when(balanceRepository.save(any(Balance.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// when
		BalanceChargeInfo result = balanceCharger.charge(command);

		// then
		assertNotNull(result);
		assertEquals(userId, result.userId());
		assertEquals(expectedAmount, result.amount());
		verify(balanceRepository).findByUserIdOptional(userId);
		verify(balanceRepository).save(existingBalance);
	}

	@Test
	@DisplayName("BalanceChargeUnAvailableException 예외 발생 테스트")
	public void testCharge_ThrowsBalanceChargeUnAvailableException() {
		// given
		Long userId = 1L;
		BigDecimal amount = BigDecimal.valueOf(100);
		BalanceChargeCommand command = new BalanceChargeCommand(userId, amount, null);

		when(balanceRepository.findByUserIdOptional(userId)).thenReturn(Optional.empty());
		when(balanceRepository.save(any(Balance.class))).thenReturn(null);

		// when & then
		assertThrows(BalanceChargeUnAvailableException.class, () -> balanceCharger.charge(command));
		verify(balanceRepository).findByUserIdOptional(userId);
		verify(balanceRepository).save(any(Balance.class));
	}
}
