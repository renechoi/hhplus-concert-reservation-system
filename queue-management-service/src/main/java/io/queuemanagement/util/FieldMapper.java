package io.queuemanagement.util;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
@Slf4j
public class FieldMapper {

	public static <T> T updateFields(T targetObject, Map<String, String> fieldValues) {
		for (Map.Entry<String, String> entry : fieldValues.entrySet()) {
			try {
				Field field = targetObject.getClass().getDeclaredField(entry.getKey());
				field.setAccessible(true);
				field.set(targetObject, convertValueForField(field.getType(), entry.getValue()));
			} catch (Exception e) {
				// 필드를 찾지 못했을 때 무시하고 계속 진행
				log.info("Field not found: " + entry.getKey() + ", skipping.");
			}
		}
		return targetObject;
	}


	private static Object convertValueForField(Class<?> fieldType, String value) {
		if(List.of("random","same", "monthly").contains(value)) {
			return null;
		}
		if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
			return Long.parseLong(value);
		} else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
			return Integer.parseInt(value);
		} else if (fieldType.equals(LocalDateTime.class)) {
			return "now".equalsIgnoreCase(value) ? LocalDateTime.now() : LocalDateTime.parse(value);
		} else if (fieldType.isEnum()) {
			return Enum.valueOf((Class<Enum>) fieldType, value.toUpperCase());
		} else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
			return Boolean.parseBoolean(value);
		}
		return value;
	}


}
