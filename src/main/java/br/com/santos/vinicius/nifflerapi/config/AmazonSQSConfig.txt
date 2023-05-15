package br.com.santos.vinicius.nifflerapi.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.messaging.config.QueueMessageHandlerFactory;
import io.awspring.cloud.messaging.config.SimpleMessageListenerContainerFactory;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;

import java.util.Collections;

@Configuration
public class AmazonSQSConfig {

    @Value("${aws.auth.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.auth.secretAccessKey}")
    private String secretAccessKey;

    @Value("${aws.auth.region}")
    private String region;

    @Value("${aws.sqs.queue.url}")
    private String sqsUrl;

    @Bean
    @Primary
    AmazonSQSAsync amazonSQSClient() {
        final BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);

        return AmazonSQSAsyncClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(sqsUrl, region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync) {
        return new QueueMessagingTemplate(amazonSQSAsync);
    }

    @Bean
    public QueueMessageHandlerFactory queueMessageHandlerFactory(final ObjectMapper mapper, final AmazonSQSAsync amazonSQSAsync) {
        final QueueMessageHandlerFactory queueHandlerFactory = new QueueMessageHandlerFactory();
        queueHandlerFactory.setAmazonSqs(amazonSQSAsync);
        queueHandlerFactory.setArgumentResolvers(Collections.singletonList(
                new PayloadMethodArgumentResolver(jacksonMessageConverter(mapper))
        ));

        return queueHandlerFactory;
    }

    @Bean
    public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(AmazonSQSAsync amazonSQSAsync) {
        SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
        factory.setAmazonSqs(amazonSQSAsync);
        factory.setMaxNumberOfMessages(10);
        return factory;
    }

    private MessageConverter jacksonMessageConverter(final ObjectMapper mapper) {
        final MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(mapper);

        return converter;
    }
}
