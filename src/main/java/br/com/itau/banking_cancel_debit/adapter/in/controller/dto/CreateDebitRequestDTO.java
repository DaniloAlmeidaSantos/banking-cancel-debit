package br.com.itau.banking_cancel_debit.adapter.in.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateDebitRequestDTO(
        @NotNull @Positive double amount,
        @NotNull @NotBlank String userId,
        @NotNull String description
) {}
