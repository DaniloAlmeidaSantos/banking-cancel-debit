package br.com.itau.banking_cancel_debit.adapter.out.messaging;

import br.com.itau.banking_cancel_debit.adapter.out.messaging.DebitCancellationEventPublisher;
import br.com.itau.banking_cancel_debit.domain.model.event.DebitCancelledEvent;
import br.com.itau.banking_cancel_debit.infrastructure.exception.MessagingException;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DebitCancellationEventPublisherTest {

    private static final String QUEUE_NAME = "cancellationName";

    @InjectMocks
    private DebitCancellationEventPublisher suite;

    @Mock
    private SqsClient sqsClient;

    @Mock
    private Gson gson;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(suite, "queueName", QUEUE_NAME);
    }

    @Test
    public void shouldBeCreateSQSEvent() {
        // Arrange
        String queueUrl = "queueUrl";
        String json = "{json}";

        DebitCancelledEvent eventPayload = getEventPayload();

        GetQueueUrlRequest request = GetQueueUrlRequest.builder()
                .queueName(QUEUE_NAME)
                .build();
        GetQueueUrlResponse response = GetQueueUrlResponse.builder()
                .queueUrl(queueUrl)
                .build();

        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(json)
                .build();

        when(sqsClient.getQueueUrl(request)).thenReturn(response);
        when(gson.toJson(eventPayload)).thenReturn(json);
        when(sqsClient.sendMessage(sendMessageRequest)).thenReturn(any());

        // Act
        suite.publish(eventPayload);

        // Assert
        verify(gson, times(1)).toJson(any(DebitCancelledEvent.class));
        verify(sqsClient, times(1)).sendMessage(any(SendMessageRequest.class));
        verify(sqsClient, times(1)).getQueueUrl(any(GetQueueUrlRequest.class));
    }

    @Test
    public void shouldThrowsSqsExceptionOnCreateEnvent() {
        // Arrange
        String queueUrl = "queueUrl";
        String json = "{json}";

        DebitCancelledEvent eventPayload = getEventPayload();

        GetQueueUrlRequest request = GetQueueUrlRequest.builder()
                .queueName(QUEUE_NAME)
                .build();
        GetQueueUrlResponse response = GetQueueUrlResponse.builder()
                .queueUrl(queueUrl)
                .build();

        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(json)
                .build();

        when(sqsClient.getQueueUrl(request)).thenReturn(response);
        when(gson.toJson(eventPayload)).thenReturn(json);

        doThrow(SqsException.class).when(sqsClient).sendMessage(sendMessageRequest);

        // Act
       var result = Assertions.assertThrows(
               MessagingException.class,
               () ->  suite.publish(eventPayload)
       );

        // Assert
        assertNotNull(result);
        assertEquals("Message not processed.", result.getMessage());
        verify(gson, times(1)).toJson(any(DebitCancelledEvent.class));
        verify(sqsClient, times(1)).sendMessage(any(SendMessageRequest.class));
        verify(sqsClient, times(1)).getQueueUrl(any(GetQueueUrlRequest.class));
    }

    private DebitCancelledEvent getEventPayload() {
        return new DebitCancelledEvent(
                "eventType",
                "debitID",
                "cancelledAt",
                "reason",
                "requestedBy",
                "correlationId"
        );
    }
}
