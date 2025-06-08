package br.com.itau.banking_cancel_debit.infrastructure.config;

import br.com.itau.banking_cancel_debit.common.dto.CancelDebitEventDTO;
import br.com.itau.banking_cancel_debit.domain.port.in.DebitServicePort;
import br.com.itau.banking_cancel_debit.domain.port.out.QueueEventPort;
import br.com.itau.banking_cancel_debit.domain.service.DebitServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreConfig {

    @Bean
    public DebitServicePort debitService(QueueEventPort<CancelDebitEventDTO> queueEvent) {
        return new DebitServiceImpl(queueEvent);
    }
}
