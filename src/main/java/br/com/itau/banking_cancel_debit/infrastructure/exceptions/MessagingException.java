package br.com.itau.banking_cancel_debit.infrastructure.exceptions;

public class MessagingException extends RuntimeException {

    public MessagingException(String message, Throwable cause) {
        super(message, cause);
    }

}
