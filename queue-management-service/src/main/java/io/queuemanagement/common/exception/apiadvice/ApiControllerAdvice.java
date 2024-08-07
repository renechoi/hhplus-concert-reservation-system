package io.queuemanagement.common.exception.apiadvice;


import static io.queuemanagement.common.model.GlobalResponseCode.*;

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

import io.queuemanagement.common.exception.definitions.ItemNotFoundException;
import io.queuemanagement.common.exception.definitions.ServerException;
import io.queuemanagement.common.model.CommonApiResponse;
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

    @ExceptionHandler(ItemNotFoundException.class)
    public Object handleItemNotFoundException(ItemNotFoundException ex) {
        log.error("ItemNotFoundException: {}", ex.getMessage());
        return new CommonApiResponse<>(NO_CONTENT, ex.getMessage(),null);
    }



    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("DataIntegrityViolationException: {}", ex.getMessage());
        String userId = extractUserIdFromException(ex);
        String errorMessage = "Duplicate entry - " + userId;
        return new CommonApiResponse<>(DUPLICATE_ENTRY, errorMessage);
    }

    private String extractUserIdFromException(DataIntegrityViolationException ex) {
        String message = ex.getMessage();
        int startIndex = message.indexOf("Duplicate entry '") + "Duplicate entry '".length();
        int endIndex = message.indexOf('-', startIndex);
        return message.substring(startIndex, endIndex);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
