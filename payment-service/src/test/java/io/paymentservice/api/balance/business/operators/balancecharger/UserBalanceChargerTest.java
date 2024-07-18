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

import io.paymentservice.api.balance.business.domainentity.UserBalance;
import io.paymentservice.api.balance.business.dto.inport.UserBalanceChargeCommand;
import io.paymentservice.api.balance.business.dto.outport.UserBalanceChargeInfo;
import io.paymentservice.api.balance.business.persistence.UserBalanceRepository;
import io.paymentservice.common.exception.definitions.UserBalanceChargeUnAvailableException;

/**
 * @author : Rene Choi
 * @since : 2024/07/12
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserBalanceCharger 테스트")
public class UserBalanceChargerTest {

	@Mock
	private UserBalanceRepository userBalanceRepository;

	@InjectMocks
	private UserBalanceCharger userBalanceCharger;

	@Test
	@DisplayName("새로운 사용자 잔액 생성 및 저장 테스트")
	public void testCharge_NewUserBalanceCreatedAndSaved() {
		// given
		Long userId = 1L;
		BigDecimal amount = BigDecimal.valueOf(100);
		UserBalanceChargeCommand command = new UserBalanceChargeCommand(userId, amount, null);

		when(userBalanceRepository.findByUserIdOptional(userId)).thenReturn(Optional.empty());
		when(userBalanceRepository.save(any(UserBalance.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// when
		UserBalanceChargeInfo result = userBalanceCharger.charge(command);

		// then
		assertNotNull(result);
		assertEquals(userId, result.userId());
		assertEquals(amount, result.amount());
		verify(userBalanceRepository).findByUserIdOptional(userId);
		verify(userBalanceRepository).save(any(UserBalance.class));
	}

	@Test
	@DisplayName("기존 사용자 잔액 업데이트 및 저장 테스트")
	public void testCharge_ExistingUserBalanceUpdatedAndSaved() {
		// given
		Long userId = 1L;
		BigDecimal initialAmount = BigDecimal.valueOf(50);
		BigDecimal chargeAmount = BigDecimal.valueOf(100);
		BigDecimal expectedAmount = initialAmount.add(chargeAmount);
		UserBalance existingUserBalance = UserBalance.builder().userId(userId).amount(initialAmount).build();
		UserBalanceChargeCommand command = new UserBalanceChargeCommand(userId, chargeAmount, null);

		when(userBalanceRepository.findByUserIdOptional(userId)).thenReturn(Optional.of(existingUserBalance));
		when(userBalanceRepository.save(any(UserBalance.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// when
		UserBalanceChargeInfo result = userBalanceCharger.charge(command);

		// then
		assertNotNull(result);
		assertEquals(userId, result.userId());
		assertEquals(expectedAmount, result.amount());
		verify(userBalanceRepository).findByUserIdOptional(userId);
		verify(userBalanceRepository).save(existingUserBalance);
	}

	@Test
	@DisplayName("UserBalanceChargeUnAvailableException 예외 발생 테스트")
	public void testCharge_ThrowsUserBalanceChargeUnAvailableException() {
		// given
		Long userId = 1L;
		BigDecimal amount = BigDecimal.valueOf(100);
		UserBalanceChargeCommand command = new UserBalanceChargeCommand(userId, amount, null);

		when(userBalanceRepository.findByUserIdOptional(userId)).thenReturn(Optional.empty());
		when(userBalanceRepository.save(any(UserBalance.class))).thenReturn(null);

		// when & then
		assertThrows(UserBalanceChargeUnAvailableException.class, () -> userBalanceCharger.charge(command));
		verify(userBalanceRepository).findByUserIdOptional(userId);
		verify(userBalanceRepository).save(any(UserBalance.class));
	}
}
