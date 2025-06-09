package br.com.itau.banking_cancel_debit.adapter.in.controller.dto;

public record ErrorDetails(String timestamp, int status, String error, String message) {}
