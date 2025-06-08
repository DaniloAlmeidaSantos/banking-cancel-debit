package br.com.itau.banking_cancel_debit.domain.debit;

import br.com.itau.banking_cancel_debit.infrastructure.exceptions.DebitAlreadyCancelledException;

import java.time.Instant;

public record Debit(
    double amount, String currency, String status, Instant createdAt, Instant cancelledAt
) {

    public void cancel(Instant timestamp) {
        if ("CANCELLED".equalsIgnoreCase(status())) {
            throw new DebitAlreadyCancelledException();
        }
    }

}
