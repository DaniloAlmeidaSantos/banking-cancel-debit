package br.com.itau.banking_cancel_debit.domain.service;

import br.com.itau.banking_cancel_debit.domain.model.Debit;
import br.com.itau.banking_cancel_debit.domain.model.DebitStatusEnum;
import br.com.itau.banking_cancel_debit.domain.model.command.CancelDebitCommand;
import br.com.itau.banking_cancel_debit.domain.model.command.CreateDebitCommand;
import br.com.itau.banking_cancel_debit.domain.model.event.DebitCancelledEvent;
import br.com.itau.banking_cancel_debit.domain.port.out.DebitRepositoryPort;
import br.com.itau.banking_cancel_debit.domain.port.out.QueueEventPort;
import br.com.itau.banking_cancel_debit.infrastructure.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    public void shouldThrowsBusinessExceptionWhenValidateRequestUserToCancel() {
        // Arrange
        String debitId = "debitId";
        String userID = "userId";
        Debit debit = getDebit(debitId, "incorrectUser", DebitStatusEnum.CANCELLED.name());
        CancelDebitCommand command = new CancelDebitCommand(debitId, "reason", userID);

        when(repositoryPort.findById(debitId)).thenReturn(debit);

        // Act
        var result = assertThrows(
                BusinessException.class,
                () -> suite.cancel(command)
        );

        // Assert
        assertEquals("User does not have permission to cancel this debit.",
                result.getMessage());
        verify(repositoryPort, times(1)).findById(any());
    }

    @Test
    public void shouldThrowsBusinessExceptionWhenPersistDataToCancel() {
        // Arrange
        String debitId = "debitId";
        String userID = "userId";
        Debit debit = getDebit(debitId, userID, DebitStatusEnum.CANCELLED.name());
        CancelDebitCommand command = new CancelDebitCommand(debitId, "reason", userID);

        when(repositoryPort.findById(debitId)).thenReturn(debit);
        when(repositoryPort.save(debit)).thenReturn(false);

        // Act
        var result = assertThrows(
                BusinessException.class,
                () -> suite.cancel(command)
        );

        // Assert
        assertEquals("Failed to persist data. Request was not dispatched.",
                result.getMessage());
        verify(repositoryPort, times(1)).save(any());
        verify(repositoryPort, times(1)).findById(any());
    }

    @Test
    public void shouldThrowsBusinessExceptionWhenDebitNotFoundToCancel() {
        // Arrange
        String debitId = "debitId";
        String userID = "userId";
        CancelDebitCommand command = new CancelDebitCommand(debitId, "reason", userID);

        when(repositoryPort.findById(debitId)).thenReturn(null);

        // Act
        var result = assertThrows(
                BusinessException.class,
                () -> suite.cancel(command)
        );

        // Assert
        assertEquals("Debit not found", result.getMessage());
        verify(repositoryPort, times(1)).findById(any());
    }

    @Test
    public void shouldCreateNewDebit() {
        // Arrange
        double amount = 50.0;
        String description = "desc";
        String userID = "userId";
        CreateDebitCommand request = new CreateDebitCommand(amount, userID, description);

        when(repositoryPort.save(any(Debit.class))).thenReturn(true);

        // Act
        Debit result = suite.create(request);

        // Assert
        assertEquals(amount, result.getAmount());
        assertEquals(userID, result.getUserId());
        assertEquals(description, result.getDescription());
        assertEquals(DebitStatusEnum.ACTIVE.name(), result.getStatus());
        verify(repositoryPort, times(1)).save(any(Debit.class));
    }

    @Test
    public void shouldThrowsExceptionWhenCreateNewDebit() {
        // Arrange
        double amount = 50.0;
        String description = "desc";
        String userID = "userId";
        CreateDebitCommand request = new CreateDebitCommand(amount, userID, description);

        when(repositoryPort.save(any(Debit.class))).thenReturn(false);

        // Act
        var result = assertThrows(
                BusinessException.class,
                () -> suite.create(request)
        );

        // Assert
        assertEquals("Failed to persist data. Debit was not created.", result.getMessage());
        verify(repositoryPort, times(1)).save(any(Debit.class));
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
