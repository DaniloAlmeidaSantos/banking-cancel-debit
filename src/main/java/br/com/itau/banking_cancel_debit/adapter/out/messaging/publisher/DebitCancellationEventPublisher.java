package br.com.itau.banking_cancel_debit.adapter.out.messaging.publisher;

import br.com.itau.banking_cancel_debit.adapter.out.messaging.QueueEvent;
import br.com.itau.banking_cancel_debit.adapter.out.messaging.model.DebitCancellationMessage;
import br.com.itau.banking_cancel_debit.infrastructure.exceptions.MessagingException;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

@Slf4j
@Service
public class DebitCancellationEventPublisher implements QueueEvent<DebitCancellationMessage> {

    private final Gson gson;
    private final SqsClient client;

    public DebitCancellationEventPublisher(Gson gson, SqsClient client) {
        this.gson = gson;
        this.client = client;
    }

    @Override
    public void publish(DebitCancellationMessage message, String queueName) {
        try {
            String queueUrl = client.getQueueUrl(
                    GetQueueUrlRequest.builder()
                            .queueName(queueName)
                            .build()
            ).queueUrl();

            client.sendMessage(
                    SendMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .messageBody(gson.toJson(message))
                            .build()
            );
        } catch (SqsException ex) {
            log.error("Error on sending message to {}: {}", queueName, ex.getMessage());
            throw new MessagingException("Message not processed.", ex.getCause());
        }
    }
}
