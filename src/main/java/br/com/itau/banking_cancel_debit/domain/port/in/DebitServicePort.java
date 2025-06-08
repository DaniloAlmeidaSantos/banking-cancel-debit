package br.com.itau.banking_cancel_debit.domain.port.in;

import br.com.itau.banking_cancel_debit.common.dto.DebitCancellationDTO;

public interface DebitServicePort {
    void cancel(DebitCancellationDTO request);
}
