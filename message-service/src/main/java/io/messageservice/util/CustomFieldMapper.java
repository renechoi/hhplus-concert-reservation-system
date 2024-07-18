package io.messageservice.util;

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
					Field field = getField(targetObject.getClass(), entry.getKey());
					if (field != null && field.getType().equals(LocalDateTime.class)) {
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

	private static Field getField(Class<?> clazz, String fieldName) {
		while (clazz != null) {
			try {
				return clazz.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			}
		}
		return null;
	}
}
