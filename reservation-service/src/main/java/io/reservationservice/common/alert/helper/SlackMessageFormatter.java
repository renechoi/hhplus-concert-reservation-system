package io.reservationservice.common.alert.helper;

import io.reservationservice.common.alert.GlobalExceptionAlertEvent;

/**
 * @author : Rene Choi
 * @since : 2024/07/19
 */

public class SlackMessageFormatter {
	private static final int STACKTRACE_LIMIT = 100;

	public static String format(GlobalExceptionAlertEvent event) {
		return """
                :rotating_light: *Exception Alert* :rotating_light:
                ------------------------------------------------
                :warning: *Method:* %s
                :speech_balloon: *Message:* %s
                :page_facing_up: *StackTrace:*
                ```%s```
                """
			.formatted(event.getMethodName(), event.getExceptionMessage(), limitStackTrace(event.getStackTrace()));
	}
	private static String limitStackTrace(String stackTrace) {
		if (stackTrace.length() > STACKTRACE_LIMIT) {
			return stackTrace.substring(0, STACKTRACE_LIMIT) + "...";
		} else {
			return stackTrace;
		}
	}
}
