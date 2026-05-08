#!/bin/bash

set -euo pipefail

KEY_FILE="cosign.key"
PUB_FILE="cosign.pub"

if [[ -f "$KEY_FILE" && -f "$PUB_FILE" ]]; then
  echo "Cosign key pair already exists. Skipping generation."
else
  echo "Generating cosign key pair..."
  cosign generate-key-pair
fi