package br.com.itau.banking_cancel_debit.infrastructure.exceptions;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
