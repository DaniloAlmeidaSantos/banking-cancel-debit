package br.com.itau.banking_cancel_debit.domain.model;

import br.com.itau.banking_cancel_debit.domain.model.command.CreateDebitCommand;
import br.com.itau.banking_cancel_debit.infrastructure.exception.BusinessException;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Debit {
    private String id;
    private String userId;
    private String status;
    private Double amount;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;

    public Debit() {
    }

    public Debit(String id, String userId, String status, Double amount, String description, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.amount = amount;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isOwnedBy(String userId) {
        return Objects.equals(this.userId, userId);
    }

    public void cancel() {
        if (DebitStatusEnum.CANCELLED.isCancelled(this.status)) {
            throw new BusinessException("Debit already cancelled.");
        }

        this.status = DebitStatusEnum.CANCELLED.name();
        this.updatedAt = Instant.now();
    }

    public static Debit createFrom(CreateDebitCommand command) {
        Instant now = Instant.now();
        return new Debit(
                UUID.randomUUID().toString(),
                command.userId(),
                DebitStatusEnum.ACTIVE.name(),
                command.amount(),
                command.description(),
                now,
                now
        );
    }

    @Override
    public String toString() {
        return String.format("Debit{id='%s', userId='%s', status='%s', amount=%.2f, description='%s'}",
                id, userId, status, amount, description);
    }
}
