package br.com.itau.banking_cancel_debit.adapter.out.persistence;

import br.com.itau.banking_cancel_debit.domain.model.Debit;
import br.com.itau.banking_cancel_debit.domain.port.in.DebitServicePort;
import br.com.itau.banking_cancel_debit.domain.port.out.DebitRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.Map;

@Slf4j
@Service
public class DebitRepository implements DebitRepositoryPort {

    private static final String TABLE_NAME = "Debits";

    private final DynamoDbClient dynamoDbClient;

    public DebitRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    @Override
    public void save(Debit debit) {
        Map<String, AttributeValue> item = DebitMapper.toItem(debit);
        PutItemRequest request = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(item)
                .build();
        dynamoDbClient.putItem(request);

    }

    @Override
    public Debit findById(String id) {
        Map<String, AttributeValue> key = Map.of(
                "id", AttributeValue.builder().s(id).build()
        );

        GetItemRequest request = GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .build();

        GetItemResponse response = dynamoDbClient.getItem(request);

        if (!response.hasItem()) return null;

        return DebitMapper.fromItem(response.item());
    }
}
