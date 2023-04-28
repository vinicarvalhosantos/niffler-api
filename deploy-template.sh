#!/usr/bin/env bash

SECRETS=$(jq -r '. | to_entries[] | "          \(.key): \(.value)"' <(doppler secrets download --no-file))
ENV_TEMPLATE="Environment:
        Variables:"
ENV_VARS="$ENV_TEMPLATE\n$SECRETS"
TEMPLATE=$(cat template.yaml)

echo -e "${TEMPLATE/\#\$ENVIRONMENT/$ENV_VARS}"