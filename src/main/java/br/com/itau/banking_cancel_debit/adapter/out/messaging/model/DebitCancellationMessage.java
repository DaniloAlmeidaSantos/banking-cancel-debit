package br.com.itau.banking_cancel_debit.adapter.out.messaging.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebitCancellationMessage {
    @JsonProperty("eventType")
    private String eventType;

    @JsonProperty("debitId")
    private String debitId;

    @JsonProperty("cancelledAt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime cancelledAt;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("requestedBy")
    private String requestedBy;

    @JsonProperty("correlationId")
    private String correlationId;
}

