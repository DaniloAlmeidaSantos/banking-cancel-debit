package br.com.itau.banking_cancel_debit.adapter.in.controller.dto;

public record CancelDebitRequestDTO(String debitId, String reason, String requestedBy) {}
