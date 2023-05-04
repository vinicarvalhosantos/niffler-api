#!/usr/bin/env bash

TEMPLATE="AWSTemplateFormatVersion: '2010-09-09'
Transform: AWS::Serverless-2016-10-31

Globals:
  Function:
    Timeout: 30

Resources:
  ProfileApiFunction:
    Type: AWS::Serverless::Function
    Properties:
      CodeUri: .
      Handler: br.com.santos.vinicius.nifflerapi.config.handler.LambdaHandler::handleRequest
      Runtime: java11
      AutoPublishAlias: ${APP_STAGE}
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
      Events:
        Blacklist:
          Type: Api
          Properties:
            Path: /{proxy+}
            Method: ANY"

echo -e "${TEMPLATE}"