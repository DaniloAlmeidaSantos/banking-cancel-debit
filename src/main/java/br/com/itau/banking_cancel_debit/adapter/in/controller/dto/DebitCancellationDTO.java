package br.com.itau.banking_cancel_debit.adapter.in.controller.dto;

import java.time.Instant;

public record DebitCancellationDTO(String reason, String requestedBy, Instant timestamp) {}
