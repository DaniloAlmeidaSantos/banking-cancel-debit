package br.com.itau.banking_cancel_debit.application.usecases;

import br.com.itau.banking_cancel_debit.adapter.in.controller.dto.DebitCancellationDTO;
import br.com.itau.banking_cancel_debit.domain.debit.DebitCancellation;

public interface CancelDebitUseCase {
    void cancel(DebitCancellation request);
}
