package br.com.itau.banking_cancel_debit.domain.port.out;

import br.com.itau.banking_cancel_debit.domain.model.Debit;

public interface DebitRepositoryPort {
    void save(Debit debit);
    Debit findById(String id);
}
