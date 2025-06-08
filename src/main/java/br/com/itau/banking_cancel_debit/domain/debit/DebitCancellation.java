package br.com.itau.banking_cancel_debit.domain.debit;

import java.time.Instant;

public class DebitCancellation {
    private String debitId;
    private String reason;
    private String requestedBy;
    private Instant timestamp;
}
