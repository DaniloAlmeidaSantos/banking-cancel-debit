package br.com.itau.banking_cancel_debit.adapter.in.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CancelDebitRequestDTO(
        @NotEmpty @NotBlank String debitId,
        @NotEmpty @NotBlank String reason,
        @NotEmpty @NotBlank String requestedBy
) {}
