package br.com.itau.banking_cancel_debit.domain.service;

import br.com.itau.banking_cancel_debit.domain.model.Debit;
import br.com.itau.banking_cancel_debit.domain.model.DebitStatusEnum;
import br.com.itau.banking_cancel_debit.domain.model.command.CancelDebitCommand;
import br.com.itau.banking_cancel_debit.domain.model.event.DebitCancelledEvent;
import br.com.itau.banking_cancel_debit.domain.port.out.DebitRepositoryPort;
import br.com.itau.banking_cancel_debit.domain.port.out.QueueEventPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DebitServiceTest {

    @InjectMocks
    private DebitService suite;

    @Mock
    private DebitRepositoryPort repositoryPort;

    @Mock
    private QueueEventPort<DebitCancelledEvent> queueEventPort;

    @Test
    public void shouldCancelDebitSuccess() {
        // Arrange
        String debitId = "debitId";
        String userID = "userId";
        Debit debit = getDebit(debitId, userID, DebitStatusEnum.CANCELLED.name());
        CancelDebitCommand command = new CancelDebitCommand(debitId, "reason", userID);

        when(repositoryPort.findById(debitId)).thenReturn(debit);
        when(repositoryPort.save(debit)).thenReturn(true);
        doNothing().when(queueEventPort).publish(any(DebitCancelledEvent.class));

        // Act
        suite.cancel(command);

        // Assert
        verify(repositoryPort, times(1)).save(any());
        verify(repositoryPort, times(1)).findById(any());
        verify(queueEventPort, times(1)).publish(any());
    }

    private Debit getDebit(String debitId, String userId, String status) {
        return new Debit(
                debitId,
                userId,
                "status",
                200.0,
                "desc",
                Instant.now(),
                Instant.now()
        );
    }

}
