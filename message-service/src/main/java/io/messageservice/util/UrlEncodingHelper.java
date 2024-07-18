package io.messageservice.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import lombok.SneakyThrows;

/**
 * @author : Rene Choi
 * @since : 2024/06/25
 */
public class UrlEncodingHelper {
	// URL 인코딩
	@SneakyThrows(UnsupportedEncodingException.class)
	public static String urlEncode(String value) {
		return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
	}

	// URL 디코딩
	@SneakyThrows(UnsupportedEncodingException.class)
	public static String urlDecode(String value) {
		return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
	}

	// Base64 인코딩
	public static String base64Encode(String value) {
		return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
	}

	// Base64 디코딩
	public static String base64Decode(String value) {
		return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
	}
}
