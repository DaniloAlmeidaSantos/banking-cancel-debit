package br.com.itau.banking_cancel_debit.application.usecases;

import br.com.itau.banking_cancel_debit.domain.debit.DebitCancellationRequest;

public interface CancelDebitUseCase {
    void cancel(DebitCancellationRequest request);
}
