package io.clientchannelservice.testutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public class TestObjectMapper {

	private static final ObjectMapper objectMapper;

	static {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
	}

	public static ObjectMapper getInstance() {
		return objectMapper;
	}
}