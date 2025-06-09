package br.com.itau.banking_cancel_debit.domain.model;

public enum DebitStatusEnum {
    CANCELLED("DEBIT_CANCELLATION"),
    PENDING( "DEBIT_PENDING"),
    ACTIVE("DEBIT_ACTIVE");

    private String eventType;

    DebitStatusEnum(String eventType) {
        this.eventType = eventType;
    }

    public String getEventType() {
        return eventType;
    }

    public boolean isCancelled(String status) {
        return status.equalsIgnoreCase(this.name());
    }
}
