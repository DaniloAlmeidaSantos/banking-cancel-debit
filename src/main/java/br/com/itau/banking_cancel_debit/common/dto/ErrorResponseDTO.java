package br.com.itau.banking_cancel_debit.common.dto;

public record ErrorResponseDTO(String timestamp, int status, String error, String message) {}
