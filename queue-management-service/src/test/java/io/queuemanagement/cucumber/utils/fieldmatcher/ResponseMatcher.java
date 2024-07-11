package io.queuemanagement.cucumber.utils.fieldmatcher;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;

import lombok.SneakyThrows;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */

public class ResponseMatcher {

	@SneakyThrows
	public static <T> boolean matchResponse(Map<String, String> expectedResponse, T actualResponse) {
		for (Field field : actualResponse.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			String fieldName = field.getName();

			// Skip validation if the field is not present in expectedResponse
			if (!expectedResponse.containsKey(fieldName)) {
				continue;
			}

			String expectedValue = expectedResponse.get(fieldName);
			Object actualValue = field.get(actualResponse);

			if (actualValue instanceof LocalDateTime) {
				if ("notNull".equalsIgnoreCase(expectedValue)) {
					return true;
				} else {
					actualValue = ((LocalDateTime) actualValue).truncatedTo(java.time.temporal.ChronoUnit.SECONDS);
					if ("now".equalsIgnoreCase(expectedValue)) {
						expectedValue = LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS).toString();
					}
					if (!expectedValue.equals(actualValue.toString())) {
						return false;
					}
				}
			} else {
				if ("notNull".equalsIgnoreCase(expectedValue) && actualValue !=null) {
					return true;
				}
				if (!expectedValue.equals(actualValue != null ? actualValue.toString() : null)) {
					return false;
				}
			}
		}
		return true;
	}


	@SneakyThrows
	public static boolean assertFieldsEqual(Object expected, Object actual) {
		for (Field field : expected.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			Object expectedValue = field.get(expected);

			try {
				Field actualField = actual.getClass().getDeclaredField(field.getName());
				actualField.setAccessible(true);
				Object actualValue = actualField.get(actual);

				if (!expectedValue.equals(actualValue)) {
					return false;
				}
			} catch (NoSuchFieldException e) {
				return false;
			}
		}
		return true;
	}


	@SneakyThrows
	private static Field getField(Class<?> clazz, String fieldName) {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			return null;  // 필드가 없으면 null 반환
		}
	}
}