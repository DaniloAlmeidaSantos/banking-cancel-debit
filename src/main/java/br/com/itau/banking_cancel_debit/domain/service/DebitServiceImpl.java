package br.com.itau.banking_cancel_debit.domain.service;

import br.com.itau.banking_cancel_debit.common.dto.CancelDebitEventDTO;
import br.com.itau.banking_cancel_debit.common.dto.DebitCancellationDTO;
import br.com.itau.banking_cancel_debit.domain.port.in.DebitServicePort;
import br.com.itau.banking_cancel_debit.domain.port.out.QueueEventPort;

import java.time.Instant;
import java.util.UUID;

public class DebitServiceImpl implements DebitServicePort {

    private final QueueEventPort<CancelDebitEventDTO> queueEvent;

    public DebitServiceImpl(QueueEventPort<CancelDebitEventDTO> queueEvent) {
        this.queueEvent = queueEvent;
    }

    @Override
    public void cancel(DebitCancellationDTO request) {
        // TODO: Find debits in DynamoDB and validate status
        // TODO: Persist new debit status after validations
        CancelDebitEventDTO event = new CancelDebitEventDTO(
                "DEBIT_CANCELLATION",
                request.debitId(),
                Instant.now(),
                request.reason(),
                request.requestedBy(),
                UUID.randomUUID().toString()
        );
        queueEvent.publish(event);
    }
}
