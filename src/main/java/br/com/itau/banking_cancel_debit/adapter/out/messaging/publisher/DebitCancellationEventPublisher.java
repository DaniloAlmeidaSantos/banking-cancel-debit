package br.com.itau.banking_cancel_debit.adapter.out.messaging.publisher;

import br.com.itau.banking_cancel_debit.adapter.out.messaging.QueueEvent;
import br.com.itau.banking_cancel_debit.domain.debit.Debit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Slf4j
@Service
public class DebitCancellationEventPublisher implements QueueEvent<Debit> {

    private final SqsClient client;

    public DebitCancellationEventPublisher(SqsClient client) {
        this.client = client;
    }

    @Override
    public void publish(Debit message, String queueName) {
        try {
            String queueUrl = client.getQueueUrl(
                    GetQueueUrlRequest.builder()
                            .queueName(queueName)
                            .build()
            ).queueUrl();

            client.sendMessage(
                    SendMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .messageBody(message.toString()) // TODO: Convert to json
                            .build()
            );
        } catch (Exception ex) {
            log.error("Error on sending message to {}: {}", queueName, ex.getMessage());
            // TODO: Throws checked exception
        }
    }
}
