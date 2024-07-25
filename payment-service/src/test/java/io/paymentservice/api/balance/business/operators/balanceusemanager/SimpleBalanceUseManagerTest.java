package io.paymentservice.api.balance.business.operators.balanceusemanager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.paymentservice.api.balance.business.dto.inport.BalanceSearchCommand;
import io.paymentservice.api.balance.business.entity.Balance;
import io.paymentservice.api.balance.business.dto.inport.BalanceUseCommand;
import io.paymentservice.api.balance.business.dto.outport.BalanceUseInfo;
import io.paymentservice.api.balance.business.persistence.BalanceRepository;
import io.paymentservice.common.exception.definitions.BalanceUseUnAvailableException;

/**
 * @author : Rene Choi
 * @since : 2024/07/12
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SimpleBalanceUseManager 테스트")
public class SimpleBalanceUseManagerTest {

	@Mock
	private BalanceRepository balanceRepository;

	@InjectMocks
	private SimpleBalanceUseManager simpleBalanceUseManager;

	private Balance balance;

	@BeforeEach
	void setUp() {
		balance = Balance.builder()
			.balanceId(1L)
			.userId(1L)
			.amount(BigDecimal.valueOf(1000))
			.build();
	}

	@Test
	@DisplayName("use 메서드 - 잔액 차감 후 저장")
	void use_ShouldDeductAmountAndSave() {
		// given
		BalanceUseCommand command = BalanceUseCommand.paymentCommand(1L, BigDecimal.valueOf(500));
		BalanceSearchCommand searchCommand = BalanceSearchCommand.builder().userId(1L).build();

		when(balanceRepository.findSingleByConditionWithLock(searchCommand)).thenReturn(balance);
		when(balanceRepository.save(any(Balance.class))).thenReturn(balance);

		// when
		BalanceUseInfo result = simpleBalanceUseManager.use(command);

		// then
		assertNotNull(result);
		assertEquals(1L, result.userId());
		assertEquals(BigDecimal.valueOf(500), result.amount());

		verify(balanceRepository, times(1)).findSingleByConditionWithLock(searchCommand);
		verify(balanceRepository, times(1)).save(balance);
	}

	@Test
	@DisplayName("use 메서드 - 잔액 부족 예외 발생")
	void use_ShouldThrowExceptionWhenInsufficientBalance() {
		// given
		BalanceUseCommand command = BalanceUseCommand.paymentCommand(1L, BigDecimal.valueOf(1500));
		BalanceSearchCommand searchCommand = BalanceSearchCommand.builder().userId(1L).build();

		when(balanceRepository.findSingleByConditionWithLock(searchCommand)).thenReturn(balance);

		// when & then
		assertThrows(BalanceUseUnAvailableException.class, () -> simpleBalanceUseManager.use(command));

		verify(balanceRepository, times(1)).findSingleByConditionWithLock(searchCommand);
		verify(balanceRepository, times(0)).save(any(Balance.class));
	}
}
