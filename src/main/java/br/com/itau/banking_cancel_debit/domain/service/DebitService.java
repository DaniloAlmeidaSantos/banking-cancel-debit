package br.com.itau.banking_cancel_debit.domain.service;

import br.com.itau.banking_cancel_debit.adapter.in.controller.dto.CreateDebitRequestDTO;
import br.com.itau.banking_cancel_debit.adapter.in.controller.dto.CreateDebitResponseDTO;
import br.com.itau.banking_cancel_debit.domain.model.Debit;
import br.com.itau.banking_cancel_debit.domain.model.DebitCancelledEvent;
import br.com.itau.banking_cancel_debit.adapter.in.controller.dto.CancelDebitRequestDTO;
import br.com.itau.banking_cancel_debit.domain.model.DebitStatusEnum;
import br.com.itau.banking_cancel_debit.domain.port.in.DebitServicePort;
import br.com.itau.banking_cancel_debit.domain.port.out.DebitRepositoryPort;
import br.com.itau.banking_cancel_debit.domain.port.out.QueueEventPort;
import br.com.itau.banking_cancel_debit.infrastructure.exception.BusinessException;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class DebitService implements DebitServicePort {

    private final DebitRepositoryPort repository;

    private final QueueEventPort<DebitCancelledEvent> queueEvent;


    public DebitService(QueueEventPort<DebitCancelledEvent> queueEvent, DebitRepositoryPort repository) {
        this.queueEvent = queueEvent;
        this.repository = repository;
    }

    @Override
    public void cancel(CancelDebitRequestDTO request) {
        Debit debit = getAndValidateDebit(request.debitId(), request.requestedBy());
        debit.setStatus(DebitStatusEnum.CANCELLED.name());
        debit.setUpdatedAt(Instant.now());

        DebitCancelledEvent event = buildNewEvent(request);

        repository.save(debit);
        queueEvent.publish(event);
    }

    @Override
    public CreateDebitResponseDTO create(CreateDebitRequestDTO request) {
        Debit debit = buildNewDebit(request);
        repository.save(debit);
        return new CreateDebitResponseDTO(debit.getId(), debit.getStatus());
    }

    private DebitCancelledEvent buildNewEvent(CancelDebitRequestDTO request) {
        return new DebitCancelledEvent(
                "DEBIT_CANCELLATION",
                request.debitId(),
                Instant.now().toString(),
                request.reason(),
                request.requestedBy(),
                UUID.randomUUID().toString()
        );
    }

    private Debit buildNewDebit(CreateDebitRequestDTO request) {
        return new Debit(
                UUID.randomUUID().toString(),
                request.userId(),
                DebitStatusEnum.ACTIVE.name(),
                request.amount(),
                request.description(),
                Instant.now(),
                Instant.now()
        );
    }

    private Debit getAndValidateDebit(String debitId, String requestedBy) {
        Debit debit = repository.findById(debitId);

        if (debit == null)
            throw new BusinessException("Debit not found");

        if (!Objects.equals(debit.getUserId(), requestedBy))
            throw new BusinessException("User no have permission to cancel this debit.");

        if (DebitStatusEnum.CANCELLED.isCancelled(debit.getStatus()))
            throw new BusinessException("Debit already cancelled");

        return debit;
    }

}
