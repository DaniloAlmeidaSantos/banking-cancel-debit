package br.com.itau.banking_cancel_debit.common.dto;

public record DebitCancellationDTO(String debitId, String reason, String requestedBy) {}
