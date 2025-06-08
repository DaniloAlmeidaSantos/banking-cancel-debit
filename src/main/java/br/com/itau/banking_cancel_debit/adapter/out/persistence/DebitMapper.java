package br.com.itau.banking_cancel_debit.adapter.out.persistence;

import br.com.itau.banking_cancel_debit.domain.model.Debit;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DebitMapper {
    public static Map<String, AttributeValue> toItem(Debit debit) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.builder().s(debit.getId()).build());
        item.put("userId", AttributeValue.builder().s(debit.getUserId()).build());
        item.put("status", AttributeValue.builder().s(debit.getStatus()).build());
        item.put("amount", AttributeValue.builder().n(debit.getAmount().toString()).build());
        item.put("description", AttributeValue.builder().s(debit.getDescription()).build());
        item.put("createdAt", AttributeValue.builder().s(debit.getCreatedAt().toString()).build());
        item.put("updatedAt", AttributeValue.builder().s(debit.getUpdatedAt().toString()).build());
        return item;
    }

    public static Debit fromItem(Map<String, AttributeValue> item) {
        Debit debit = new Debit();

        debit.setId(item.get("id").s());
        debit.setUserId(item.get("userId").s());
        debit.setStatus(item.get("status").s());
        debit.setAmount(Double.valueOf(item.get("amount").n()));
        debit.setDescription(item.get("description").s());
        debit.setCreatedAt(Instant.parse(item.get("createdAt").s()));
        debit.setUpdatedAt(Instant.parse(item.get("updatedAt").s()));
        return debit;
    }
}
