#!/usr/bin/env bash

SECRETS=$(jq -r '. | to_entries[] | "          \(.key): \(.value)"' <(doppler secrets download --no-file))
ENV_TEMPLATE="Environment:
        Variables:
          JAVA_TOOL_OPTIONS: -XX:+TieredCompilation -XX:TieredStopAtLevel=1
          TWITCH_AUTH_BASE_URL: ${TWITCH_AUTH_BASE_URL}
          TWITCH_HELIX_BASE_URL: ${TWITCH_HELIX_BASE_URL}
          CLIENT_ID: ${CLIENT_ID}
          CLIENT_SECRET: ${CLIENT_SECRET}
          AWS__ACCESS_KEY: ${AWS__ACCESS_KEY}
          AWS__SECRET_KEY: ${AWS__SECRET_KEY}
          STAGE_URL: ${STAGE_URL}
          PROD_URL: ${PROD_URL}"
ENV_VARS="$ENV_TEMPLATE\n$SECRETS"
TEMPLATE=$(cat template.yaml)

echo -e "${TEMPLATE/\#\$ENVIRONMENT/$ENV_VARS}"