package io.paymentservice.common.model;

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
public enum GlobalResponseCode implements ResponseCapable{

	// 정상 응답
	SUCCESS("0000", "success", HttpStatus.OK),
	CREATED("0001", "success", HttpStatus.CREATED),



	// 예외 응답
	NO_CONTENT("1005", "no content", HttpStatus.NO_CONTENT),
	ILLEGAL_ARGUMENT("1006","illegal argument", HttpStatus.BAD_REQUEST),



	// 실패 응답
	UNKNOWN_ERROR("9000", "unknown error", HttpStatus.INTERNAL_SERVER_ERROR),
	FEIGN_EXCHANGE_EXCEPTION("9001","feign exchange exception", HttpStatus.BAD_REQUEST),

	// domain

	USER_BALANCE_CHARGE_UNAVAILABLE("24081", "charge unavailable", HttpStatus.BAD_REQUEST),
	USER_BALANCE_USE_UNAVAILABLE("24082", "use unavailable", HttpStatus.BAD_REQUEST),



	PAYMENT_PROCESSING_FAILED("24090", "payment processing failed", HttpStatus.BAD_REQUEST),
	PAYMENT_ALREADY_CANCELED("24091", "payment alread cancelled", HttpStatus.BAD_REQUEST);

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
