package br.com.itau.banking_cancel_debit.adapter.in.controller.advice;

import br.com.itau.banking_cancel_debit.common.dto.ErrorResponseDTO;
import br.com.itau.banking_cancel_debit.infrastructure.exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusinessException(final BusinessException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ErrorResponseDTO(
                                Instant.now().toString(),
                                HttpStatus.BAD_REQUEST.value(),
                                ex.getCause().toString(),
                                ex.getMessage()
                        )
                );
    }

}
