package br.com.itau.banking_cancel_debit.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Configuration
public class AwsConfig {

    @Value("${aws.awsEndpoint}")
    private String awsEndpoint;

    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.accessKey}")
    private String secretKey;

    @Bean
    public SqsClient amazonSQSClient() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        return SqsClient.builder()
                .region(Region.SA_EAST_1)
                .endpointOverride(URI.create(awsEndpoint))
                .credentialsProvider(
                        StaticCredentialsProvider.create(credentials)
                )
                .build();
    }
}
