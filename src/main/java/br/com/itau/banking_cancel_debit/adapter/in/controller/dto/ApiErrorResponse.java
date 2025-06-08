package br.com.itau.banking_cancel_debit.adapter.in.controller.dto;

public record ApiErrorResponse(String timestamp, int status, String error, String message) {}
