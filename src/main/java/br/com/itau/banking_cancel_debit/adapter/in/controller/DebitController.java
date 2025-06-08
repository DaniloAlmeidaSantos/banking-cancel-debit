package br.com.itau.banking_cancel_debit.adapter.in.controller;

import br.com.itau.banking_cancel_debit.adapter.in.controller.dto.CancelDebitRequestDTO;
import br.com.itau.banking_cancel_debit.adapter.in.controller.dto.CreateDebitRequestDTO;
import br.com.itau.banking_cancel_debit.adapter.in.controller.dto.CreateDebitResponseDTO;
import br.com.itau.banking_cancel_debit.domain.port.in.DebitServicePort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/debit")
public class DebitController {

    private final DebitServicePort debitService;

    public DebitController(DebitServicePort debitService) {
        this.debitService = debitService;
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelDebit(@RequestBody CancelDebitRequestDTO body) {
        debitService.cancel(body);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CreateDebitResponseDTO> createDebit(@RequestBody CreateDebitRequestDTO body) {
        var debit = debitService.create(body);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(debit);
    }

}
