package br.com.itau.banking_cancel_debit.domain.model;

import java.time.Instant;
import java.util.Date;

public class Debit {
    private String id;
    private String userId;
    private String status;
    private Double amount;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;

    public Debit() {}

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

    @Override
    public String toString() {
        return "Debit{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", status='" + status + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
