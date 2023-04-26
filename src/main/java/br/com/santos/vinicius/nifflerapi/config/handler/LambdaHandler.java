package br.com.santos.vinicius.nifflerapi.config.handler;

import br.com.santos.vinicius.nifflerapi.NifflerApplication;
import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.serverless.proxy.spring.SpringBootProxyHandlerBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
    private final SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    public LambdaHandler() {
        try {
            handler = new SpringBootProxyHandlerBuilder<AwsProxyRequest>()
                    .defaultProxy()
                    .asyncInit()
                    .springBootApplication(NifflerApplication.class)
                    .buildAndInitialize();
        } catch (ContainerInitializationException ex) {
            throw new RuntimeException("Unable to load spring boot application", ex);
        }
    }

    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest awsProxyRequest, Context context) {
        return handler.proxy(awsProxyRequest, context);

    }
}
