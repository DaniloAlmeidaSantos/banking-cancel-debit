package br.com.itau.banking_cancel_debit.adapter.in.controller;

import br.com.itau.banking_cancel_debit.application.usecases.CancelDebitUseCase;
import br.com.itau.banking_cancel_debit.domain.debit.DebitCancellationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/debits")
public class CancelDebitController {

    private final CancelDebitUseCase cancelDebit;

    public CancelDebitController(CancelDebitUseCase cancelDebit) {
        this.cancelDebit = cancelDebit;
    }

    // TODO: Alter response
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Object> cancelDebit(@PathVariable("id") String uuid, @RequestBody DebitCancellationRequest body) {
        cancelDebit.cancel(body);
        return null;
    }

}
