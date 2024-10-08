package io.paymentservice.common.exception.apiadvice;




import static io.paymentservice.common.model.GlobalResponseCode.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.paymentservice.common.exception.definitions.PaymentProcessUnAvailableException;
import io.paymentservice.common.exception.definitions.ServerException;
import io.paymentservice.common.model.CommonApiResponse;
import io.paymentservice.common.model.GlobalResponseCode;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
class ApiControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public Object handleException(Exception ex) {
        log.error("processUnDefinedErrors: {}", ex.getMessage());
        return new CommonApiResponse<>(UNKNOWN_ERROR);
    }

    @ExceptionHandler(ServerException.class)
    public Object processServerException(ServerException serverException) {
        log.error("ServerException: {}", serverException.getMessage());
        return new CommonApiResponse<>(serverException.getCode());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CommonApiResponse<Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("DataIntegrityViolationException: {}", ex.getMessage());
        return new ResponseEntity<>(new CommonApiResponse<>(PAYMENT_PROCESSING_FAILED, "중복 결제가 감지되었습니다."), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(PaymentProcessUnAvailableException.class)
    public ResponseEntity<CommonApiResponse<Object>> handleCustomPaymentException(PaymentProcessUnAvailableException ex) {
        log.error("PaymentProcessUnAvailableException: {}", ex.getMessage());
        return new ResponseEntity<>(
            new CommonApiResponse<>(ex.getCode(), ex.getAdditionalInfo()), ex.getCode().getHttpStatus()
        );
    }

}
