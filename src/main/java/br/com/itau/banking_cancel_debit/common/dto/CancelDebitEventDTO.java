package br.com.itau.banking_cancel_debit.common.dto;

import java.time.Instant;

public record CancelDebitEventDTO(
        String eventType,
        String debitId,
        Instant cancelledAt,
        String reason,
        String requestedBy,
        String correlationId
) { }
