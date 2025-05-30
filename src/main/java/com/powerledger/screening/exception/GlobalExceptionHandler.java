package com.powerledger.screening.exception;

import com.powerledger.screening.enums.CodeEnum;
import com.powerledger.screening.model.ErrorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handle all exception in this application
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler  extends ResponseStatusExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public Mono<ResponseEntity<List<ErrorInfo>>> handleBadRequestException(BadRequestException ex) {
        return handleException(ex.getCause(), Collections.singletonList(new ErrorInfo(CodeEnum.INVALID_REQUEST,
                ex.getMessage())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<List<ErrorInfo>>> handleWebExchangeBindException(WebExchangeBindException ex) {
        return handleException(ex.getCause(), ((WebExchangeBindException )ex).getFieldErrors().stream()
                .map(fieldError -> new ErrorInfo(CodeEnum.REQUIRED, fieldError.getDefaultMessage()))
                .toList(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<List<ErrorInfo>>> handleException(Exception ex) {
        return handleException(ex.getCause(), Collections.singletonList(new ErrorInfo(CodeEnum.UNKNOWN,
                ex.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * Generate error response
     * @param throwable
     * @param errors
     * @param httpStatus
     * @return List<ErrorInfo>
     */
    private Mono<ResponseEntity<List<ErrorInfo>>> handleException(Throwable throwable, List<ErrorInfo> errors, HttpStatus httpStatus) {
        String errorMessage = errors.stream().map(errorInfo ->
                errorInfo.getDescription() + " " + errorInfo.getCode()
        ).collect(Collectors.joining(","));

        log.error(errorMessage, throwable);
        return Mono.just(ResponseEntity.status(httpStatus).body(errors));
    }
}
