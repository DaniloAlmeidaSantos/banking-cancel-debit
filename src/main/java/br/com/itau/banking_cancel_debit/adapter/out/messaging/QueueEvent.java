package br.com.itau.banking_cancel_debit.adapter.out.messaging;

public interface QueueEvent<T> {
    void publish(T message, String queueName);
}
