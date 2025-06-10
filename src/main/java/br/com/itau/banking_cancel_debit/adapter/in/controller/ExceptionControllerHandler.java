package br.com.itau.banking_cancel_debit.adapter.in.controller;

import br.com.itau.banking_cancel_debit.adapter.in.controller.dto.ErrorDetails;
import br.com.itau.banking_cancel_debit.infrastructure.exception.BusinessException;
import br.com.itau.banking_cancel_debit.infrastructure.exception.MessagingException;
import br.com.itau.banking_cancel_debit.infrastructure.exception.PersistenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ExceptionControllerHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDetails> handleBusinessException(final BusinessException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ErrorDetails(
                                Instant.now().toString(),
                                HttpStatus.BAD_REQUEST.value(),
                                "BUSINESS_ERROR",
                                ex.getMessage()
                        )
                );
    }

    @ExceptionHandler({MessagingException.class, PersistenceException.class})
    public ResponseEntity<ErrorDetails> handleAdapterOutboundException(final MessagingException ex) {
        String cause = ex.getCause().toString().isEmpty() ?
                "ADAPTER_ERROR" : ex.getCause().toString();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorDetails(
                                Instant.now().toString(),
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                cause,
                                ex.getMessage()
                        )
                );
    }

}
