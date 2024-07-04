package io.apiorchestrationservice.common.model;


import static io.apiorchestrationservice.common.model.GlobalResponseCode.*;
import static io.apiorchestrationservice.util.UrlEncodingHelper.*;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

/**
 * @author : Rene Choi
 * @since : 2024/06/25
 */

@Getter
@Setter
public class CommonApiResponse<T> extends ResponseEntity<Object> {

	private final String resultCode;
	private final String resultMessage;
	private final T data;

	public CommonApiResponse(String resultCode, String resultMessage, HttpStatus status, T data) {
		super(data, createHeaders(resultCode, resultMessage), status);
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
		this.data = data;
	}

	@SneakyThrows
	private static HttpHeaders createHeaders(String code, String message) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Response-Code", code);
		String encodedMessage = base64Encode(message);
		headers.add("Response-Message", encodedMessage);
		return headers;
	}

	public CommonApiResponse(GlobalResponseCode responseCode) {
		this(responseCode.getResultCode(), responseCode.getResultMessage(), responseCode.getHttpStatus(), null);
	}

	public CommonApiResponse(GlobalResponseCode responseCode, String customMessage, T data) {
		this(responseCode.getResultCode(), customMessage, responseCode.getHttpStatus(), data);
	}

	public CommonApiResponse(GlobalResponseCode responseCode, Throwable e) {
		this(responseCode.getResultCode(), e.getMessage(), responseCode.getHttpStatus(), null);
	}

	public static <T> CommonApiResponse<T> OK(GlobalResponseCode responseCode, T data) {
		return new CommonApiResponse<>(responseCode.getResultCode(), responseCode.getResultMessage(), responseCode.getHttpStatus(), data);
	}

	public static <T> CommonApiResponse<T> OK() {
		return OK(SUCCESS, null);
	}

	public static <T> CommonApiResponse<T> OK(T data) {
		if (data instanceof Page && ((Page<?>)data).isEmpty()) {
			return new CommonApiResponse<>(NO_CONTENT.getResultCode(), NO_CONTENT.getResultMessage(), NO_CONTENT.getHttpStatus(), data);
		}

		if (data instanceof List && ((List<?>)data).isEmpty()) {
			return new CommonApiResponse<>(NO_CONTENT.getResultCode(), NO_CONTENT.getResultMessage(), NO_CONTENT.getHttpStatus(), data);
		}

		return OK(SUCCESS, data);
	}

	public static <T> CommonApiResponse<T> created(T data) {
		if (data instanceof Page && ((Page<?>)data).isEmpty()) {
			return new CommonApiResponse<>(NO_CONTENT.getResultCode(), NO_CONTENT.getResultMessage(), NO_CONTENT.getHttpStatus(), data);
		}

		if (data instanceof List && ((List<?>)data).isEmpty()) {
			return new CommonApiResponse<>(NO_CONTENT.getResultCode(), NO_CONTENT.getResultMessage(), NO_CONTENT.getHttpStatus(), data);
		}

		return OK(CREATED, data);
	}


	public static <T> CommonApiResponse<T> error(GlobalResponseCode responseCode, Throwable e) {
		return new CommonApiResponse<>(responseCode.getResultCode(), responseCode.getResultMessage(), responseCode.getHttpStatus(), null);
	}

	public static <T> CommonApiResponse<List<T>> listResponse(GlobalResponseCode responseCode, List<T> list) {
		if (list.isEmpty()) {
			return new CommonApiResponse<>(NO_CONTENT.getResultCode(), NO_CONTENT.getResultMessage(), NO_CONTENT.getHttpStatus(), list);
		}
		return new CommonApiResponse<>(responseCode.getResultCode(), responseCode.getResultMessage(), responseCode.getHttpStatus(), list);
	}

	public static <T> CommonApiResponse<T> withCustomMessage(GlobalResponseCode responseCode, String customMessage, T data) {
		return new CommonApiResponse<>(responseCode, customMessage, data);
	}

	public static <T> ResponseEntity<Object> withHeaders(GlobalResponseCode responseCode, T data, Map<String, String> headers) {
		HttpHeaders httpHeaders = new HttpHeaders();
		headers.forEach(httpHeaders::add);
		return new ResponseEntity<>(new CommonApiResponse<>(responseCode.getResultCode(), responseCode.getResultMessage(), responseCode.getHttpStatus(), data), httpHeaders, responseCode.getHttpStatus());
	}

}