package br.com.itau.banking_cancel_debit.infrastructure.exception;

public class PersistenceException extends RuntimeException{

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

}
