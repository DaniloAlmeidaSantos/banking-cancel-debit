package br.com.itau.banking_cancel_debit.domain.model;

public record DebitCancelledEvent(
        String eventType,
        String debitId,
        String cancelledAt,
        String reason,
        String requestedBy,
        String correlationId
) { }
