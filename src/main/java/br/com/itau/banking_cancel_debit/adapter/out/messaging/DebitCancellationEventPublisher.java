package br.com.itau.banking_cancel_debit.adapter.out.messaging;

import br.com.itau.banking_cancel_debit.domain.model.event.DebitCancelledEvent;
import br.com.itau.banking_cancel_debit.domain.port.out.QueueEventPort;
import br.com.itau.banking_cancel_debit.infrastructure.exception.MessagingException;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.Map;

@Slf4j
@Service
public class DebitCancellationEventPublisher implements QueueEventPort<DebitCancelledEvent> {

    @Value("${aws.sqs.debitCancelQueueName}")
    private String queueName;

    private final Gson gson;
    private final SqsClient client;

    public DebitCancellationEventPublisher(Gson gson, SqsClient client) {
        this.gson = gson;
        this.client = client;
    }

    @Override
    public void publish(DebitCancelledEvent message) {
        try {
            String queueUrl = client.getQueueUrl(
                    GetQueueUrlRequest.builder()
                            .queueName(queueName)
                            .build()
            ).queueUrl();

            String payload = gson.toJson(message);

            SendMessageResponse response = client.sendMessage(
                    SendMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .messageAttributes(Map.of(
                                    "correlationId", MessageAttributeValue.builder()
                                            .stringValue(message.correlationId())
                                            .dataType("String")
                                            .build()
                            ))
                            .messageBody(payload)
                            .build()
            );

            if (!response.sdkHttpResponse().isSuccessful()) {
                log.warn("Failed to send message to SQS [{}]: {}", payload, response.sdkHttpResponse().statusText());
                throw new MessagingException(
                        String.format(
                                "Failed to send message %s to SQS: %s", payload, response.sdkHttpResponse().statusText()
                        )
                );
            }

            log.info("Message successfully send to SQS: [{}]", payload);
        } catch (SqsException ex) {
            log.error("Error on sending message to {}: {}", queueName, ex.getMessage());
            throw new MessagingException("Message not processed.", ex.getCause());
        }
    }
}
