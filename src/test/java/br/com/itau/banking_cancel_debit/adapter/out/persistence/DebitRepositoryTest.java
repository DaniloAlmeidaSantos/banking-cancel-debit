package br.com.itau.banking_cancel_debit.adapter.out.persistence;

import br.com.itau.banking_cancel_debit.adapter.out.persistence.DebitRepository;
import br.com.itau.banking_cancel_debit.domain.model.Debit;
import br.com.itau.banking_cancel_debit.infrastructure.exception.PersistenceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DebitRepositoryTest {

    @InjectMocks
    private DebitRepository suite;

    @Mock
    private DynamoDbClient dynamoDbClient;

    @Test
    public void shouldSaveDebitSuccess() {
        // Arrange
        Debit debit = getDebitToSave();

        SdkHttpResponse sdkHttpResponse = SdkHttpResponse.builder()
                .statusCode(200)
                .build();
        PutItemResponse response = (PutItemResponse) PutItemResponse.builder()
                .sdkHttpResponse(sdkHttpResponse)
                .build();

        when(dynamoDbClient.putItem(any(PutItemRequest.class))).thenReturn(response);

        // Act
        boolean isSaved = suite.save(debit);

        // Assert
        assertTrue(isSaved);
        verify(dynamoDbClient, times(1)).putItem(any(PutItemRequest.class));
    }

    @Test
    public void shouldSaveDebitError() {
        // Arrange
        Debit debit = getDebitToSave();

        SdkHttpResponse sdkHttpResponse = SdkHttpResponse.builder()
                .statusCode(500)
                .build();
        PutItemResponse response = (PutItemResponse) PutItemResponse.builder()
                .sdkHttpResponse(sdkHttpResponse)
                .build();

        when(dynamoDbClient.putItem(any(PutItemRequest.class))).thenReturn(response);

        // Act
        boolean isSaved = suite.save(debit);

        // Assert
        assertFalse(isSaved);
        verify(dynamoDbClient, times(1)).putItem(any(PutItemRequest.class));
    }

    @Test
    public void shouldThrowsExceptionOnSaveDebit() {
        // Arrange
        Debit debit = getDebitToSave();

        doThrow(RuntimeException.class).when(dynamoDbClient).putItem(any(PutItemRequest.class));

        // Act
        var result = assertThrows(
                PersistenceException.class,
                () -> suite.save(debit)
        );

        // Assert
        assertNotNull(result);
        verify(dynamoDbClient, times(1)).putItem(any(PutItemRequest.class));
    }

    @Test
    public void shouldFindDebitByIdSuccess() {
        // Arrange
        String id = "uuid";

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.fromS("uuid"));
        item.put("userId", AttributeValue.fromS("userId"));
        item.put("status", AttributeValue.fromS("status"));
        item.put("amount", AttributeValue.fromN("200.0"));
        item.put("description", AttributeValue.fromS("Desc"));
        item.put("createdAt", AttributeValue.fromS(Instant.now().toString()));
        item.put("updatedAt", AttributeValue.fromS(Instant.now().toString()));

        GetItemResponse response = GetItemResponse.builder()
                .item(item)
                .build();

        when(dynamoDbClient.getItem(any(GetItemRequest.class)))
                .thenReturn(response);

        // Act
        Debit result = suite.findById(id);

        // Assert
        assertEquals("uuid", result.getId());
        assertEquals("userId", result.getUserId());
        assertEquals("status", result.getStatus());
        assertEquals(200.0, result.getAmount());
        assertEquals("Desc", result.getDescription());
        verify(dynamoDbClient, times(1)).getItem(any(GetItemRequest.class));
    }

    @Test
    public void shouldNotFoundDebitById() {
        // Arrange
        String id = "uuid";

        GetItemResponse response = GetItemResponse.builder()
                .item(null)
                .build();

        when(dynamoDbClient.getItem(any(GetItemRequest.class)))
                .thenReturn(response);

        // Act
        Debit result = suite.findById(id);

        // Assert
        assertNull(result);
        verify(dynamoDbClient, times(1)).getItem(any(GetItemRequest.class));
    }

    @Test
    public void shouldThrowsExceptionOnFindDebitById() {
        // Arrange
        String id = "uuid";

        GetItemResponse response = GetItemResponse.builder()
                .item(null)
                .build();

        doThrow(RuntimeException.class).when(dynamoDbClient)
                .getItem(any(GetItemRequest.class));

        // Act
        var result = assertThrows(
                PersistenceException.class,
                () -> suite.findById(id)
        );

        // Assert
        assertNotNull(result);
        verify(dynamoDbClient, times(1)).getItem(any(GetItemRequest.class));
    }

    private Debit getDebitToSave() {
        return new Debit(
                "id",
                "userId",
                "status",
                200.0,
                "description",
                Instant.now(),
                Instant.now()
        );
    }

}
