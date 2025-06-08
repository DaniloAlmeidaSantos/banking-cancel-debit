package br.com.itau.banking_cancel_debit.application.mapper;

import br.com.itau.banking_cancel_debit.adapter.in.controller.dto.DebitCancellationDTO;
import br.com.itau.banking_cancel_debit.domain.debit.DebitCancellation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CancelDebitMapper {
    @Mappings({
            @Mapping(source = "id", target = "debitId"),
            @Mapping(source = "dto.reason", target = "reason"),
            @Mapping(source = "dto.requestedBy", target = "requestedBy"),
            @Mapping(source = "dto.timestamp", target = "timestamp")
    })
    DebitCancellation toDomain(DebitCancellationDTO dto, String id);


}
