package br.com.santos.vinicius.nifflerapi.config.handler;

import br.com.santos.vinicius.nifflerapi.NifflerApplication;
import br.com.santos.vinicius.nifflerapi.exception.LambdaException;
import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.serverless.proxy.spring.SpringBootProxyHandlerBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SQSHandler implements RequestHandler<SQSEvent, AwsProxyResponse> {

    private final SpringBootLambdaContainerHandler<SQSEvent, AwsProxyResponse> handler;

    public SQSHandler() throws LambdaException {
        try {
            log.info("Building and initializing lambda handler.");
            handler = new SpringBootProxyHandlerBuilder<SQSEvent>()
                    .defaultProxy()
                    .asyncInit()
                    .springBootApplication(NifflerApplication.class)
                    .buildAndInitialize();
        } catch (ContainerInitializationException ex) {
            log.error("An error occurred when tried to initialize lambda handler. {}", ex.getMessage());
            throw new LambdaException("Unable to load spring boot application", ex);
        }
    }

    @Override
    public AwsProxyResponse handleRequest(SQSEvent sqsEvent, Context context) {
        return handler.proxy(sqsEvent, context);
    }
}
