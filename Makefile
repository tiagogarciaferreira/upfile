IMAGE_NAME = tiagogarciaferreira/upfile
TAG ?= latest
FULL_IMAGE = $(IMAGE_NAME):$(TAG)
VERSION := $(shell ./gradlew printVersion -q)

MAKEFLAGS += --no-print-directory

add-host:
	@printf "\n"
	@echo "🌐 Adding host entry for api.upfile.tgfcodes.com..."
	@HOST_ENTRY="127.0.0.1       api.upfile.tgfcodes.com"; \
	if ! grep -q "api.upfile.tgfcodes.com" /etc/hosts; then \
		echo "$$HOST_ENTRY" | sudo tee -a /etc/hosts > /dev/null; \
		echo "✅ Host added"; \
	else \
		echo "⚠️  Host already exists"; \
	fi

generate-cert:
	@printf "\n"
	@echo "🔐 Generating certificate..."
	@cd certs && chmod +x ./generate-certificate.sh && ./generate-certificate.sh
	@echo "✅ Certificate generated successfully!"

clean-cert:
	@printf "\n"
	@echo "🧹 Cleaning certificate files..."
	@rm -vf certs/*.csr certs/*.key certs/*.crt certs/*.p12 certs/*.srl certs/*.pem src/main/resources/*.p12 2>/dev/null || true
	@sudo rm -vf /usr/local/share/ca-certificates/dev-root-ca.crt && update-ca-certificates --fresh 2>/dev/null || true
	@echo "✅ Cleanup completed!"

generate-cosign-key:
	@printf "\n"
	@echo "🔐 Generating Cosign key pair..."
	@cd cosign && chmod +x ./generate-signature-key.sh && ./generate-signature-key.sh
	@echo "✅ Cosign key pair ready!"

clear-cosign-key:
	@printf "\n"
	@echo "🧹 Cleaning Cosign key files..."
	@rm -vf cosign/*.key cosign/*.pub 2>/dev/null || true
	@echo "✅ Cleanup completed!"

generate-jwt-keys:
	@printf "\n"
	@echo "🔐 Generating Ed25519 JWT key pair..."
	@cd jwt && chmod +x ./generate-jwt-keys.sh && ./generate-jwt-keys.sh
	@echo "✅ Ed25519 keys generated successfully!"

clear-jwt-keys:
	@printf "\n"
	@echo "🧹 Cleaning Jwt key files..."
	@rm -vf jwt/*.jwk src/main/resources/*.jwk 2>/dev/null || true
	@echo "✅ Cleanup completed!"

setup-local:
	@printf "\n"
	@echo "🔧 Setting up local environment..."
	@$(MAKE) clean-local add-host generate-cert generate-cosign-key generate-jwt-keys
	@echo "✅ Local environment setup complete!"

clean-local:
	@printf "\n"
	@echo "🗑️  Cleaning local environment artifacts..."
	@$(MAKE) clean-cert clear-cosign-key clear-jwt-keys
	@echo "✨ Local environment cleaned successfully!"

build-image:
	@printf "\n"
	@docker images --format '{{.Repository}}:{{.Tag}}' | grep "^$(IMAGE_NAME):" | xargs -r docker rmi -f
	@echo "🔨 Building image..."
	@docker build -f Dockerfile --no-cache -t $(FULL_IMAGE) .
	@echo "✅ Image built"

add-image-tag:
	@printf "\n"
	@echo "🏷️ Tagging image..."
	@docker tag $(FULL_IMAGE) $(IMAGE_NAME):$(VERSION)
	@echo "✅ Tagged as: $(IMAGE_NAME):$(VERSION)"

image-push:
	@printf "\n"
	@echo "📤 Pushing image to registry..."
	@docker push $(IMAGE_NAME) -a 2>&1 | grep -E "digest:|latest: digest:" || true
	@echo "✅ Image pushed"

publish-image:
	@printf "\n"
	@echo "📦 Preparing to publish image to registry..."
	@$(MAKE) build-image add-image-tag image-push
	@echo "🚀 Image published successfully to registry!"

image-signature:
	@printf "\n"
	@echo "🔐 Signing Docker image by digest..."
	@$(eval DIGEST=$(shell docker inspect $(FULL_IMAGE) --format='{{index .RepoDigests 0}}'))

	@echo "📋 Digest: $(DIGEST)"
	@cosign sign --key cosign/cosign.key $(DIGEST)
	@echo "✅ Image signed: $(FULL_IMAGE)"

verify-signature:
	@echo "🔍 Verifying signature..."
	@sleep 10
	@cosign verify \
		--key cosign/cosign.pub \
		--output json \
		$(DIGEST) \
	| jq -r '.[0].critical | { docker_reference: .identity["docker-reference"], manifest_digest: .image["docker-manifest-digest"], signature_type: .type }'
	@echo "✅ Signature verified successfully"

sign-and-verify-image:
	@printf "\n"
	@echo "🔐 Starting image signing and verification process..."
	@$(MAKE) image-signature verify-signature
	@echo "✅ Image signed and verified successfully!"

image-analyze:
	@echo "🔍 Analyzing image: $(FULL_IMAGE)"
	$(eval DIGEST := $(shell docker inspect $(FULL_IMAGE) --format='{{index .RepoDigests 0}}'))
	@docker run --rm --quiet \
				-v /var/run/docker.sock:/var/run/docker.sock \
				-v "$(CURDIR)/analyze:/ci" \
				wagoodman/dive:latest \
				--ci \
				--ci-config /ci/.dive.yaml \
				$(DIGEST)
	@echo "✅ Analysis complete"

image-info:
	@printf "\n"
	@echo "📊 Image Information..."
	@docker images --filter reference=$(IMAGE_NAME) --format "table {{.Repository}}:{{.Tag}}\t{{.Size}}\t{{.CreatedAt}}" 2>/dev/null || echo "  ⚠️ No images found"
	@echo "  📦       Name: $(IMAGE_NAME)"
	@echo "  🏷️        Tag: $(TAG) - $(VERSION)"
	@SIGN_STATUS=$$(docker trust inspect --pretty $(IMAGE_NAME):$(TAG) 2>&1 | grep -qi "no signatures" && echo "❌ Not signed" || echo "✅ Signed (Docker Trust)"); \
	echo  "  🔐  Signature: $$SIGN_STATUS"

compose-clean:
	@printf "\n"
	@echo "🧹 Cleaning containers and volumes..."
	@docker stop app postgres rustfs 2>/dev/null || true
	@docker rm -f app postgres rustfs 2>/dev/null || true
	@docker volume rm postgres_data rustfs_data 2>/dev/null || true
	@echo "✨ Cleanup completed successfully!"

compose-up:
	@printf "\n"
	@echo "🚀 Starting containers..."
	@docker compose -p "upfile" -f docker-compose.yaml --profile local --env-file ./.env.local up -d
	@echo "✅ Containers are up!"

compose-down:
	@printf "\n"
	@echo "🛑 Stopping containers..."
	@docker compose -p "upfile" down
	@echo "🔴 Containers are down!"

compose-recreate:
	@printf "\n"
	@echo "🔄 Recreating environment..."
	@$(MAKE) compose-clean compose-up
	@echo "✅ Environment recreated successfully!"

help:
	@printf "\n"
	@echo "🚀 Available Makefile Commands"
	@echo "=================================================="
	@echo ""

	@echo "🔧 Security & Certificates"
	@echo "--------------------------------------------------"
	@echo "  generate-cert         Generate SSL/TLS certificates"
	@echo "  clean-cert            Remove generated certificate files"
	@echo "  generate-cosign-key   Generate Cosign key pair"
	@echo "  clean-cosign-key      Remove generated Cosign key files"
	@echo "  generate-jwt-keys     Generate JWT key pair"
	@echo "  clean-jwt-keys        Remove generated JWT key files"
	@echo "  add-host              Add host entry to the system hosts file"
	@echo ""

	@echo "🐳 Docker Image Lifecycle"
	@echo "--------------------------------------------------"
	@echo "  build-image           Build Docker image"
	@echo "  add-image-tag         Tag image with version ($(VERSION))"
	@echo "  image-signature       Sign container image (Cosign)"
	@echo "  verify-signature      Verify container image signature (Cosign)"
	@echo "  image-push            Push image to registry"
	@echo "  sign-and-verify-image Sign and verify image in sequence"
	@echo "  publish-image         Build, tag and push image to registry"
	@echo "  image-analyze         Analyze image layers and efficiency (dive)"
	@echo "  image-info            Show local image metadata and signature status"
	@echo ""

	@echo "📦 Docker Compose Environment"
	@echo "--------------------------------------------------"
	@echo "  compose-up            Start containers using docker-compose"
	@echo "  compose-down          Stop containers"
	@echo "  compose-clean         Remove containers and volumes"
	@echo "  compose-recreate      Recreate environment (clean + up)"
	@echo ""

	@echo "🔧 Local Setup"
	@echo "--------------------------------------------------"
	@echo "  setup-local           Setup complete local environment (certificates, keys, host)"
	@echo "  clean-local           Remove all local environment artifacts"
	@echo ""

	@echo "🛠️  Utilities"
	@echo "--------------------------------------------------"
	@echo "  help                  Show this help message"
	@echo ""

.PHONY: \
	generate-cert \
	clean-cert \
	compose-up \
	compose-down \
	compose-clean \
	compose-recreate \
	generate-cosign-key \
	clear-cosign-key \
	generate-jwt-keys \
	clean-jwt-keys \
	build-image \
	add-image-tag \
	image-signature \
	verify-signature \
	image-push \
	sign-and-verify-image \
	publish-image \
	add-host \
	setup-local \
	clean-local \
	image-analyze \
	image-info
	help