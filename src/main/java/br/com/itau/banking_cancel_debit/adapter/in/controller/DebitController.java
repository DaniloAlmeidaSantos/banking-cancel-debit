package br.com.itau.banking_cancel_debit.adapter.in.controller;

import br.com.itau.banking_cancel_debit.adapter.in.controller.dto.CancelDebitRequestDTO;
import br.com.itau.banking_cancel_debit.adapter.in.controller.dto.CreateDebitRequestDTO;
import br.com.itau.banking_cancel_debit.adapter.in.controller.dto.CreateDebitResponseDTO;
import br.com.itau.banking_cancel_debit.domain.model.command.CancelDebitCommand;
import br.com.itau.banking_cancel_debit.domain.model.command.CreateDebitCommand;
import br.com.itau.banking_cancel_debit.domain.port.in.DebitServicePort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/debit")
public class DebitController {

    private final DebitServicePort debitService;

    public DebitController(DebitServicePort debitService) {
        this.debitService = debitService;
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelDebit(
            @RequestBody @Valid CancelDebitRequestDTO body,
            @RequestHeader(name = "X-Correlation-Id", required = false) String correlationId
    ) {
        String correlation = Optional.ofNullable(correlationId)
                .filter(id ->!id.isEmpty())
                .orElse(UUID.randomUUID().toString());

        var request = new CancelDebitCommand(body.debitId(), body.reason(), body.requestedBy(), correlation);
        debitService.cancel(request);
        return ResponseEntity.ok().header("X-Correlation-Id", correlation).build();
    }

    @PostMapping
    public ResponseEntity<CreateDebitResponseDTO> createDebit(@RequestBody @Valid CreateDebitRequestDTO body) {
        var request = new CreateDebitCommand(body.amount(), body.userId(), body.description());
        var debit = debitService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(new CreateDebitResponseDTO(debit.getId(), debit.getStatus()));
    }

}
