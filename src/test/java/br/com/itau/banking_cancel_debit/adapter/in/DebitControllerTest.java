package br.com.itau.banking_cancel_debit.adapter.in;

import br.com.itau.banking_cancel_debit.adapter.in.controller.DebitController;
import br.com.itau.banking_cancel_debit.adapter.in.controller.dto.CancelDebitRequestDTO;
import br.com.itau.banking_cancel_debit.adapter.in.controller.dto.CreateDebitRequestDTO;
import br.com.itau.banking_cancel_debit.adapter.in.controller.dto.CreateDebitResponseDTO;
import br.com.itau.banking_cancel_debit.domain.model.Debit;
import br.com.itau.banking_cancel_debit.domain.model.command.CancelDebitCommand;
import br.com.itau.banking_cancel_debit.domain.model.command.CreateDebitCommand;
import br.com.itau.banking_cancel_debit.domain.port.in.DebitServicePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DebitControllerTest {

    @InjectMocks
    private DebitController suite;

    @Mock
    private DebitServicePort debitServicePort;

    @Test
    public void shouldCreateDebit() {
        // Arrange
        String debitId = "debitId";
        String status = "ACTIVE";
        CreateDebitRequestDTO body = new CreateDebitRequestDTO(
                400.0,
                "userId",
                "Generic"
        );
        var request = new CreateDebitCommand(body.amount(), body.userId(), body.description());
        Debit debit = new Debit();
        debit.setId(debitId);
        debit.setStatus(status);

        when(debitServicePort.create(request)).thenReturn(debit);

        // Act
        var result = suite.createDebit(body);

        // Assert
        assertEquals(201, result.getStatusCode().value());
        assertEquals(debitId, result.getBody().debitId());
        assertEquals(status, result.getBody().status());
        verify(debitServicePort, times(1)).create(any());
    }

    @Test
    public void shouldCancelDebit() {
        // Arrange
        String correlationId = "correlationId";
        CancelDebitRequestDTO body = new CancelDebitRequestDTO(
                "debitId",
                "userId",
                "Generic"
        );
        var request = new CancelDebitCommand(body.debitId(), body.reason(), body.requestedBy(), correlationId);

        doNothing().when(debitServicePort).cancel(request);

        // Act
        var result = suite.cancelDebit(body, correlationId);

        // Assert
        assertEquals(correlationId, result.getHeaders().get("X-Correlation-Id").get(0));
        assertEquals(200, result.getStatusCode().value());
        verify(debitServicePort, times(1)).cancel(any());
    }

}
