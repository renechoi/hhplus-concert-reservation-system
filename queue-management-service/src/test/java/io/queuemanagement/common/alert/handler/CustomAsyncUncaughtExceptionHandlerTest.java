package io.queuemanagement.common.alert.handler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import io.queuemanagement.common.alert.GlobalExceptionAlertEvent;
import io.queuemanagement.common.alert.publisher.GlobalExceptionAlertInternalPublisher;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */
@Slf4j
class CustomAsyncUncaughtExceptionHandlerTest {

	@Mock
	private GlobalExceptionAlertInternalPublisher globalExceptionAlertInternalPublisher;

	@InjectMocks
	private CustomAsyncUncaughtExceptionHandler customAsyncUncaughtExceptionHandler;

	private ListAppender<ILoggingEvent> listAppender;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		Logger logger = (Logger)LoggerFactory.getLogger(CustomAsyncUncaughtExceptionHandler.class);
		listAppender = new ListAppender<>();
		listAppender.start();
		logger.addAppender(listAppender);
	}

	@AfterEach
	void tearDown() {
		listAppender.stop();
	}

	@SneakyThrows
	@Test
	@DisplayName("handleUncaughtException 메소드가 예외를 캡처하고 로그로 기록하는지 테스트")
	void testHandleUncaughtException() {
		String exceptionMessage = "Test Exception";
		Throwable throwable = new RuntimeException(exceptionMessage);
		Method method = CustomAsyncUncaughtExceptionHandler.class.getMethod("handleUncaughtException", Throwable.class, Method.class, Object[].class);

		customAsyncUncaughtExceptionHandler.handleUncaughtException(throwable, method);

		verify(globalExceptionAlertInternalPublisher, times(1))
			.publish(any(GlobalExceptionAlertEvent.class));

		boolean hasExpectedLog = listAppender.list.stream()
			.anyMatch(event -> event.getLevel() == Level.ERROR &&
				event.getFormattedMessage().contains(exceptionMessage));

		assertTrue(hasExpectedLog, "예상한 로그 메시지가 발견되지 않았습니다!");
	}
}
