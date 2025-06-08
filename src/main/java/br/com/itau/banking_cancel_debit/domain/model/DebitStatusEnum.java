package br.com.itau.banking_cancel_debit.domain.model;

public enum DebitStatusEnum {
    CANCELLED,
    PENDING,
    ACTIVE;

    public boolean isCancelled(String status) {
        return status.equalsIgnoreCase(this.name());
    }
}
