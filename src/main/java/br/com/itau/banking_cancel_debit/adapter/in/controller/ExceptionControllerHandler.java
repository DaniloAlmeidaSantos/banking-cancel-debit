package br.com.itau.banking_cancel_debit.adapter.in.controller;

import br.com.itau.banking_cancel_debit.adapter.in.controller.dto.ApiErrorResponse;
import br.com.itau.banking_cancel_debit.infrastructure.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ExceptionControllerHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessException(final BusinessException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ApiErrorResponse(
                                Instant.now().toString(),
                                HttpStatus.BAD_REQUEST.value(),
                                "BUSINESS_ERROR",
                                ex.getMessage()
                        )
                );
    }

}
