#!/bin/bash

set -euo pipefail

printf "\n"
echo "🔑 Generating Root CA..."
openssl genrsa -out rootCA.key 4096

openssl req -x509 -new -nodes \
  -key rootCA.key \
  -sha256 -days 3650 \
  -out rootCA.crt \
  -subj "/C=BR/ST=RS/L=Local/O=Dev CA/CN=Dev Root CA"

echo "✅ Root CA created: rootCA.crt"


printf "\n"
echo "🔐 Generating server certificate..."
openssl genrsa -out server.key 2048

openssl req -new \
  -key server.key \
  -out server.csr \
  -config san.cnf

openssl x509 -req \
  -in server.csr \
  -CA rootCA.crt \
  -CAkey rootCA.key \
  -CAcreateserial \
  -out server.crt \
  -days 825 \
  -sha256 \
  -extensions req_ext \
  -extfile san.cnf

echo "✅ Server certificate created: server.crt"


printf "\n"
echo "🛡️  Installing Root CA on system..."
sudo cp rootCA.crt /usr/local/share/ca-certificates/dev-root-ca.crt
sudo update-ca-certificates
echo "✅ Root CA trusted by system"


printf "\n"
echo "📦 Loading environment variables from .env..."
source ../../.env
export SSL_STORE_PASSWORD

printf "\n"
echo "🔐 Generating PKCS12 keystore..."

openssl pkcs12 -export \
  -in server.crt \
  -inkey server.key \
  -certfile rootCA.crt \
  -out upfile-tls-keystore.p12 \
  -name upfile-tls \
  -passout env:SSL_STORE_PASSWORD

echo "✅ PKCS12 keystore created: upfile-tls-keystore.p12"


printf "\n"
echo "🔄 Copying TLS keystore to resources..."
rsync -av upfile-tls-keystore.p12 ../../src/main/resources/
echo "✅ Keystore copied to ../../src/main/resources/"


printf "\n"
echo "🎉 All certificate files have been generated!"
echo "📂 Files:"
for file in rootCA.key rootCA.crt server.key server.csr server.crt upfile-tls-keystore.p12; do
  [ -e "$file" ] && echo "  ✔ $file" || echo "  ✘ $file (missing)"
done
printf "\n"