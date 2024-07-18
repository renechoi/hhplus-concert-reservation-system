package io.reservationservice.common.alert;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */

@Data
@AllArgsConstructor
public class GlobalExceptionAlertEvent {
	private String methodName;
	private String exceptionMessage;
	private String stackTrace;

	public GlobalExceptionAlertEvent(String methodName, Throwable throwable) {
		this.methodName = methodName;
		this.exceptionMessage = throwable.getMessage();
		this.stackTrace = getStackTraceAsString(throwable);
	}

	private String getStackTraceAsString(Throwable throwable) {
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement element : throwable.getStackTrace()) {
			sb.append(element.toString()).append("\n");
		}
		return sb.toString();
	}
}
