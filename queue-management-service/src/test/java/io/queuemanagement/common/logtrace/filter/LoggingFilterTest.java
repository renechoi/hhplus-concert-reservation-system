package io.queuemanagement.common.logtrace.filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.mock.web.DelegatingServletOutputStream;

import io.queuemanagement.common.logtrace.TraceId;
import io.queuemanagement.common.logtrace.TraceStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/17
 */
@ExtendWith(MockitoExtension.class)
class LoggingFilterTest {

	@Mock
	private RequestResponseLogger logger;

	@Mock
	private FilterChain filterChain;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	private LoggingFilter loggingFilter;

	@BeforeEach
	void setUp() {
		loggingFilter = new LoggingFilter(logger);
	}

	@Test
	void testDoFilter() throws Exception {
		// Given
		String requestBody = "test request body";
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBody.getBytes());
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		when(request.getInputStream()).thenReturn(new DelegatingServletInputStream(byteArrayInputStream));
		when(response.getOutputStream()).thenReturn(new DelegatingServletOutputStream(byteArrayOutputStream));

		TraceStatus traceStatus = new TraceStatus(new TraceId(), System.currentTimeMillis(), "Test Trace");
		when(logger.startTrace(any())).thenReturn(traceStatus);

		// When
		loggingFilter.doFilter(request, response, filterChain);

		// Then
		verify(logger).startTrace(any());
		verify(logger).endTrace(any(), any());
		verify(logger).logResponseBody(any());
		verify(filterChain).doFilter(any(), any());
	}

	@ParameterizedTest
	@MethodSource("provideRequestBodies")
	void testDoFilterPerformance(String requestBody) throws Exception {
		when(request.getInputStream()).thenReturn(new DelegatingServletInputStream(new ByteArrayInputStream(requestBody.getBytes())));
		when(response.getOutputStream()).thenReturn(new DelegatingServletOutputStream(new ByteArrayOutputStream()));

		long startTime = System.currentTimeMillis();

		loggingFilter.doFilter(request, response, filterChain);

		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;

		System.out.println("Request body size: " + requestBody.length() + ", Duration: " + duration + "ms");

		assertTrue(duration < 5000, "Performance test failed");
	}

	private static Stream<String> provideRequestBodies() {
		return Stream.of(
			"short request body",
			repeat('a', 100),
			repeat('a', 1000),
			repeat('a', 10000),
			"json body {\"key\":\"value\",\"key2\":\"value2\"}",
			"xml body <note><to>Tove</to><from>Jani</from><heading>Reminder</heading><body>Don't forget me this weekend!</body></note>"
		);
	}

	private static String repeat(char ch, int times) {
		StringBuilder sb = new StringBuilder(times);
		for (int i = 0; i < times; i++) {
			sb.append(ch);
		}
		return sb.toString();
	}
}

