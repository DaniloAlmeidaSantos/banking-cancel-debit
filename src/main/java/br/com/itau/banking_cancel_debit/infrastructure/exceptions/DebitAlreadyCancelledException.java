package br.com.itau.banking_cancel_debit.infrastructure.exceptions;

public class DebitAlreadyCancelledException extends IllegalStateException {
    public DebitAlreadyCancelledException() {
        super("Debit already cancelled.");
    }

}
