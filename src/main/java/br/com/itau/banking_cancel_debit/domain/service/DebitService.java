package br.com.itau.banking_cancel_debit.domain.service;

import br.com.itau.banking_cancel_debit.domain.model.Debit;
import br.com.itau.banking_cancel_debit.domain.model.command.CancelDebitCommand;
import br.com.itau.banking_cancel_debit.domain.model.command.CreateDebitCommand;
import br.com.itau.banking_cancel_debit.domain.model.event.DebitCancelledEvent;
import br.com.itau.banking_cancel_debit.domain.port.in.DebitServicePort;
import br.com.itau.banking_cancel_debit.domain.port.out.DebitRepositoryPort;
import br.com.itau.banking_cancel_debit.domain.port.out.QueueEventPort;
import br.com.itau.banking_cancel_debit.infrastructure.exception.BusinessException;

public class DebitService implements DebitServicePort {

    private final DebitRepositoryPort repository;

    private final QueueEventPort<DebitCancelledEvent> queueEvent;


    public DebitService(QueueEventPort<DebitCancelledEvent> queueEvent, DebitRepositoryPort repository) {
        this.queueEvent = queueEvent;
        this.repository = repository;
    }

    @Override
    public void cancel(CancelDebitCommand request) {
        Debit debit = this.findDebit(request.debitId());

        if (!debit.isOwnedBy(request.requestedBy()))
            throw new BusinessException("User does not have permission to cancel this debit.");

        debit.cancel();
        boolean isSaved = repository.save(debit);

        if (!isSaved)
            throw new BusinessException("Failed to persist data. Request was not dispatched.");

        queueEvent.publish(DebitCancelledEvent.createEvent(debit, request));
    }

    @Override
    public Debit create(CreateDebitCommand request) {
        Debit debit = Debit.createFrom(request);
        boolean isSaved = repository.save(debit);

        if (!isSaved) throw new BusinessException("Failed to persist data. Debit was not created.");

        return debit;
    }

    private Debit findDebit(String debitId) {
        Debit debit = repository.findById(debitId);

        if (debit == null)
            throw new BusinessException("Debit not found");

        return debit;
    }

}
