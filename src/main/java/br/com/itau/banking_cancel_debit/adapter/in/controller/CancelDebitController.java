package br.com.itau.banking_cancel_debit.adapter.in.controller;

import br.com.itau.banking_cancel_debit.common.dto.DebitCancellationDTO;
import br.com.itau.banking_cancel_debit.domain.port.in.DebitServicePort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("debit")
public class CancelDebitController {
    
    private final DebitServicePort debitService;

    public CancelDebitController(DebitServicePort debitService) {
        this.debitService = debitService;
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelDebit(@RequestBody DebitCancellationDTO body) {
        debitService.cancel(body);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
