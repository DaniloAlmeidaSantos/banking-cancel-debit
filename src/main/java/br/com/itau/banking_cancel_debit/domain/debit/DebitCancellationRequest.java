package br.com.itau.banking_cancel_debit.domain.debit;

import java.time.Instant;

public record DebitCancellationRequest(String reason, String requestedBy, Instant timestamp) {}
