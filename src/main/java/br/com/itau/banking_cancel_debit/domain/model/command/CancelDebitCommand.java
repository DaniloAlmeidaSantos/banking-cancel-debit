package br.com.itau.banking_cancel_debit.domain.model.command;

public record CancelDebitCommand(String debitId, String reason, String requestedBy, String correlationId) {}
