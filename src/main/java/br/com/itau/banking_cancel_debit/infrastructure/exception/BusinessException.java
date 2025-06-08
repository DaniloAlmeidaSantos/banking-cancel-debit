package br.com.itau.banking_cancel_debit.infrastructure.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
