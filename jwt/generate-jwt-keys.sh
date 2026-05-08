#!/bin/bash

set -euo pipefail

KEY_FILE="jwt-private-key.jwk"
PUB_FILE="jwt-public-key.jwk"

if [[ -f "$KEY_FILE" && -f "$PUB_FILE" ]]; then
  echo "Jwt key pair already exists. Skipping generation."
else
  echo "Generating jwt key pair..."
  step crypto jwk create \
    --kty OKP \
    --crv Ed25519 \
    --no-password \
    --insecure \
    jwt-public-key.jwk \
    jwt-private-key.jwk

  printf "\n"
  echo -e "🔄 [Syncing] Copying JWT Keys to resources..."
  rsync -av jwt-public-key.jwk ../src/main/resources/
  rsync -av jwt-private-key.jwk ../src/main/resources/
fi
