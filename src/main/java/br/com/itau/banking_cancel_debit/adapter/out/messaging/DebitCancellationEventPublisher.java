package br.com.itau.banking_cancel_debit.adapter.out.messaging;

import br.com.itau.banking_cancel_debit.domain.model.event.DebitCancelledEvent;
import br.com.itau.banking_cancel_debit.domain.port.out.QueueEventPort;
import br.com.itau.banking_cancel_debit.infrastructure.exception.MessagingException;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

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
