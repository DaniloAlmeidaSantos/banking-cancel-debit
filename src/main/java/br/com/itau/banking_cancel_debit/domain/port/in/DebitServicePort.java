package br.com.itau.banking_cancel_debit.domain.port.in;

import br.com.itau.banking_cancel_debit.adapter.in.controller.dto.CancelDebitRequestDTO;
import br.com.itau.banking_cancel_debit.adapter.in.controller.dto.CreateDebitRequestDTO;
import br.com.itau.banking_cancel_debit.adapter.in.controller.dto.CreateDebitResponseDTO;

public interface DebitServicePort {
    void cancel(CancelDebitRequestDTO request);
    CreateDebitResponseDTO create(CreateDebitRequestDTO request);
}
