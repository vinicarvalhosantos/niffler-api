#!/usr/bin/env bash

TEMPLATE="AWSTemplateFormatVersion: '2010-09-09'
Transform: AWS::Serverless-2016-10-31

Globals:
  Function:
    Timeout: 30

Resources:
  ProfileSQSFunction:
    Type: AWS::SQS::Queue
    Properties:
      QueueName: user_messages_queue.fifo
      ContentBasedDeduplication: true
      FifoQueue: true
      MessageRetentionPeriod: 345600
  ProfileApiFunction:
    Type: AWS::Serverless::Function
    Properties:
      CodeUri: .
      Handler: br.com.santos.vinicius.nifflerapi.config.handler.LambdaHandler::handleRequest
      Runtime: java11
      AutoPublishAlias: live
      SnapStart:
        ApplyOn: PublishedVersions
      Architectures:
        - x86_64
      MemorySize: 2048
      Environment:
        Variables:
          JAVA_TOOL_OPTIONS: -XX:+TieredCompilation -XX:TieredStopAtLevel=1
          TWITCH_AUTH_BASE_URL: ${TWITCH_AUTH_BASE_URL}
          TWITCH_HELIX_BASE_URL: ${TWITCH_HELIX_BASE_URL}
          CLIENT_ID: ${CLIENT_ID}
          CLIENT_SECRET: ${CLIENT_SECRET}
          AWS__ACCESS_KEY: ${AWS__ACCESS_KEY}
          AWS__SECRET_KEY: ${AWS__SECRET_KEY}
          DEV_URL: ${DEV_URL}
          PROD_URL: ${PROD_URL}
          SQS_QUEUE_URL: ${SQS_QUEUE_URL}
          SQS_QUEUE_NAME: ${SQS_QUEUE_NAME}
          SPRING_SECURITY_PASSWORD: ${SPRING_SECURITY_PASSWORD}
          AWS_KMS_KEY_ID: ${AWS_KMS_KEY_ID}
      Events:
        Blacklist:
          Type: Api
          Properties:
            Path: /{proxy+}
            Method: ANY
        SqsEvent:
          Type: SQS
          Properties:
            Queue: !GetAtt ProfileSQSFunction.Arn
            BatchSize: 10"

echo -e "${TEMPLATE}"
