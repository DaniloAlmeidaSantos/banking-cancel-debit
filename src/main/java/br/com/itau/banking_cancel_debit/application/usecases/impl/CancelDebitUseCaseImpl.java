package br.com.itau.banking_cancel_debit.application.usecases.impl;

import br.com.itau.banking_cancel_debit.adapter.out.messaging.QueueEvent;
import br.com.itau.banking_cancel_debit.adapter.out.messaging.model.DebitCancellationMessage;
import br.com.itau.banking_cancel_debit.application.mapper.CancelDebitMapper;
import br.com.itau.banking_cancel_debit.application.usecases.CancelDebitUseCase;
import br.com.itau.banking_cancel_debit.domain.debit.DebitCancellation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CancelDebitUseCaseImpl implements CancelDebitUseCase {

    @Value("${aws.sqs.debitCancelQueueName}")
    private String cancellationQueue;

    private final CancelDebitMapper cancelDebitMapper;
    private final QueueEvent<DebitCancellationMessage> queueEvent;

    public CancelDebitUseCaseImpl(CancelDebitMapper cancelDebitMapper, QueueEvent<DebitCancellationMessage> queueEvent) {
        this.cancelDebitMapper = cancelDebitMapper;
        this.queueEvent = queueEvent;
    }

    @Override
    public void cancel(DebitCancellation request) {
        // TODO: Find debits in DynamoDB and validate status
        // TODO: Persist new debit status after validations

        queueEvent.publish(null, cancellationQueue);
    }
}
