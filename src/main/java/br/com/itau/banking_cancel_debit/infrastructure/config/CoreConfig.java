package br.com.itau.banking_cancel_debit.infrastructure.config;

import br.com.itau.banking_cancel_debit.domain.model.event.DebitCancelledEvent;
import br.com.itau.banking_cancel_debit.domain.port.in.DebitServicePort;
import br.com.itau.banking_cancel_debit.domain.port.out.DebitRepositoryPort;
import br.com.itau.banking_cancel_debit.domain.port.out.QueueEventPort;
import br.com.itau.banking_cancel_debit.domain.service.DebitService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreConfig {

    @Bean
    public DebitServicePort debitService(QueueEventPort<DebitCancelledEvent> queueEvent, DebitRepositoryPort repository) {
        return new DebitService(queueEvent, repository);
    }
}
