package io.paymentservice.util;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
@Slf4j
public class CustomFieldMapper {

	public static <T> T applyCustomMappings(T targetObject, Map<String, String> fieldValues) {
		for (Map.Entry<String, String> entry : fieldValues.entrySet()) {
			if ("now".equalsIgnoreCase(entry.getValue())) {
				try {
					Field field = targetObject.getClass().getDeclaredField(entry.getKey());
					if (field.getType().equals(LocalDateTime.class)) {
						field.setAccessible(true);
						field.set(targetObject, LocalDateTime.now());
					}
				} catch (Exception e) {
					log.info("Custom Field not found: " + entry.getKey() + ", skipping.");
				}
			}
		}
		return targetObject;
	}
}
