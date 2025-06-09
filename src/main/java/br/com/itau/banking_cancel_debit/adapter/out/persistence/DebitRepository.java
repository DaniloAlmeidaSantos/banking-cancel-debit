package br.com.itau.banking_cancel_debit.adapter.out.persistence;

import br.com.itau.banking_cancel_debit.domain.model.Debit;
import br.com.itau.banking_cancel_debit.domain.port.in.DebitServicePort;
import br.com.itau.banking_cancel_debit.domain.port.out.DebitRepositoryPort;
import br.com.itau.banking_cancel_debit.infrastructure.exception.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

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
    public boolean save(Debit debit) {
        try {
            Map<String, AttributeValue> item = DebitMapper.toItem(debit);
            PutItemRequest request = PutItemRequest.builder()
                    .tableName(TABLE_NAME)
                    .item(item)
                    .build();
            PutItemResponse response = dynamoDbClient.putItem(request);

            if (!response.sdkHttpResponse().isSuccessful()) {
                log.warn("Failed to persist item in DynamoDB: {}", response.sdkHttpResponse().statusText());
                return false;
            }

            log.info("Item successfully persisted in DynamoDB.");
            return true;
        } catch (Exception ex) {
            log.error("Error persisting data in DynamoDB: {}", ex.getMessage());
            throw new PersistenceException("Failed to persist data in DynamoDB.", ex);
        }
    }

    @Override
    public Debit findById(String id) {
        try {
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
        } catch (Exception ex) {
            log.error("Error retrieving data from DynamoDB: {}", ex.getMessage());
            throw new PersistenceException("Failed to retrieve data from DynamoDB.", ex);
        }
    }
}
