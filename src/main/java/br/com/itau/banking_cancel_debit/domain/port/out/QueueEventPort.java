package br.com.itau.banking_cancel_debit.domain.port.out;

public interface QueueEventPort<T> {
    void publish(T message);
}
