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

import io.paymentservice.api.balance.business.domainentity.UserBalance;
import io.paymentservice.api.balance.business.dto.inport.UserBalanceUseCommand;
import io.paymentservice.api.balance.business.dto.outport.UserBalanceUseInfo;
import io.paymentservice.api.balance.business.persistence.UserBalanceRepository;
import io.paymentservice.common.exception.UserBalanceUseUnAvailableException;

/**
 * @author : Rene Choi
 * @since : 2024/07/12
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SimpleUserBalanceUseManager 테스트")
public class SimpleUserBalanceUseManagerTest {

	@Mock
	private UserBalanceRepository userBalanceRepository;

	@InjectMocks
	private SimpleUserBalanceUseManager simpleUserBalanceUseManager;

	private UserBalance userBalance;

	@BeforeEach
	void setUp() {
		userBalance = UserBalance.builder()
			.userBalanceId(1L)
			.userId(1L)
			.amount(BigDecimal.valueOf(1000))
			.build();
	}

	@Test
	@DisplayName("use 메서드 - 잔액 차감 후 저장")
	void use_ShouldDeductAmountAndSave() {
		// given
		UserBalanceUseCommand command = UserBalanceUseCommand.createPaymentCommand(1L, BigDecimal.valueOf(500));
		when(userBalanceRepository.findByUserIdWithThrows(1L)).thenReturn(userBalance);
		when(userBalanceRepository.save(any(UserBalance.class))).thenReturn(userBalance);

		// when
		UserBalanceUseInfo result = simpleUserBalanceUseManager.use(command);

		// then
		assertNotNull(result);
		assertEquals(1L, result.userId());
		assertEquals(BigDecimal.valueOf(500), result.amount());

		verify(userBalanceRepository, times(1)).findByUserIdWithThrows(1L);
		verify(userBalanceRepository, times(1)).save(userBalance);
	}

	@Test
	@DisplayName("use 메서드 - 잔액 부족 예외 발생")
	void use_ShouldThrowExceptionWhenInsufficientBalance() {
		// given
		UserBalanceUseCommand command = UserBalanceUseCommand.createPaymentCommand(1L, BigDecimal.valueOf(1500));
		when(userBalanceRepository.findByUserIdWithThrows(1L)).thenReturn(userBalance);

		// when & then
		assertThrows(UserBalanceUseUnAvailableException.class, () -> simpleUserBalanceUseManager.use(command));

		verify(userBalanceRepository, times(1)).findByUserIdWithThrows(1L);
		verify(userBalanceRepository, times(0)).save(any(UserBalance.class));
	}
}
