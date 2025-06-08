package br.com.itau.banking_cancel_debit.adapter.in.controller;

import br.com.itau.banking_cancel_debit.application.mapper.CancelDebitMapper;
import br.com.itau.banking_cancel_debit.application.usecases.CancelDebitUseCase;
import br.com.itau.banking_cancel_debit.adapter.in.controller.dto.DebitCancellationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/debits")
@RequiredArgsConstructor
public class CancelDebitController {

    private final CancelDebitMapper mapper;

    private final CancelDebitUseCase cancelDebit;

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelDebit(@PathVariable("id") String id, @RequestBody DebitCancellationDTO body) {
        cancelDebit.cancel(mapper.toDomain(body, id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
