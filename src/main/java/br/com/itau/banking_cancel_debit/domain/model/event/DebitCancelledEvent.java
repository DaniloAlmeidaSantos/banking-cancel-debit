package br.com.itau.banking_cancel_debit.domain.model.event;

import br.com.itau.banking_cancel_debit.domain.model.Debit;
import br.com.itau.banking_cancel_debit.domain.model.DebitStatusEnum;
import br.com.itau.banking_cancel_debit.domain.model.command.CancelDebitCommand;

import java.time.Instant;
import java.util.UUID;

public record DebitCancelledEvent(
        String eventType,
        String debitId,
        String cancelledAt,
        String reason,
        String requestedBy,
        String correlationId
) {

    public static DebitCancelledEvent createEvent(Debit debit, CancelDebitCommand command) {
        return new DebitCancelledEvent(
                DebitStatusEnum.CANCELLED.getEventType(),
                debit.getId(),
                Instant.now().toString(),
                command.reason(),
                command.requestedBy(),
                UUID.randomUUID().toString()
        );
    }

}
