package br.com.itau.banking_cancel_debit.domain.debit;

import br.com.itau.banking_cancel_debit.infrastructure.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

import java.time.Instant;

public record Debit(
    double amount, String currency, DebitStatusEnum status, Instant createdAt, Instant cancelledAt
) {}
