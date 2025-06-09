package br.com.itau.banking_cancel_debit.domain.model.command;

public record CreateDebitCommand(double amount, String userId, String description) {
}
