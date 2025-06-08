package br.com.itau.banking_cancel_debit.adapter.in.controller.dto;

public record CreateDebitRequestDTO(
        double amount,
        String userId,
        String description
) { }
