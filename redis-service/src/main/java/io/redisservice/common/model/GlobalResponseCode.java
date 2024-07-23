package io.redisservice.common.model;

import java.util.Arrays;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/25
 */
@Getter
@RequiredArgsConstructor
public enum GlobalResponseCode implements ResponseCapable {

	// 정상 응답
	SUCCESS("0000", "success", HttpStatus.OK),
	CREATED("0001", "success", HttpStatus.CREATED),



	// 예외 응답
	NO_CONTENT("1005", "no content", HttpStatus.NO_CONTENT),
	ILLEGAL_ARGUMENT("1006","illegal argument", HttpStatus.BAD_REQUEST),



	// 실패 응답
	UNKNOWN_ERROR("9000", "unknown error", HttpStatus.INTERNAL_SERVER_ERROR),


	// domain

	RESERVATION_UNAVAILABLE("24000", "reservation unavailable", HttpStatus.BAD_REQUEST),
	SEAT_ALREADY_RESERVED("24001", "seat already reserved", HttpStatus.BAD_REQUEST),
	SEAT_NOT_FOUND("24002", "seat not found", HttpStatus.BAD_REQUEST),
	RESERVATION_RETRIEVAL_NO_CONTENT("24003", "reservation not found", HttpStatus.NO_CONTENT),

	;

	private final String resultCode;
	private final String resultMessage;
	private final HttpStatus httpStatus;


	public static GlobalResponseCode getResponseCode(String code){
		return Arrays.stream(GlobalResponseCode.values())
			.filter(item->item.hasCodeName(code))
			.findAny()
			.orElse(GlobalResponseCode.UNKNOWN_ERROR);
	}

	public boolean hasCodeName(String code){
		return this.resultCode.equals(code);
	}

}
