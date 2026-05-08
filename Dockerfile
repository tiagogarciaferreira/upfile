# ============================================================
# STAGE 1: Builder
# ============================================================
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /workspace

COPY gradle/                 gradle/
COPY gradlew                 gradlew
COPY settings.gradle.kts     settings.gradle.kts
COPY build.gradle.kts        build.gradle.kts
COPY gradle.properties       gradle.properties

RUN chmod +x gradlew

RUN --mount=type=cache,target=/root/.gradle,sharing=locked \
    ./gradlew dependencies --no-daemon

COPY src src

RUN --mount=type=cache,target=/root/.gradle,sharing=locked \
    ./gradlew clean build --no-daemon

RUN java -Djarmode=tools -jar build/libs/*.jar extract --layers --destination build/extracted

# ============================================================
# STAGE 2: Runtime Image
# ============================================================
FROM gcr.io/distroless/java25-debian13:nonroot AS runtime

WORKDIR /app

COPY --chown=nonroot:nonroot --from=builder /workspace/build/extracted/lib/ ./lib/
COPY --chown=nonroot:nonroot --from=builder /workspace/build/extracted/org/ ./org/
COPY --chown=nonroot:nonroot --from=builder /workspace/build/extracted/snapshot-lib/ ./snapshot-lib/
COPY --chown=nonroot:nonroot --from=builder /workspace/build/extracted/application/ ./

USER nonroot

EXPOSE 8443

ENTRYPOINT [
  "java",
  "-XX:MaxRAMPercentage=75.0",
  "-XX:+UseZGC",
  "-XX:+ZGenerational",
  "-XX:+ExitOnOutOfMemoryError",
  "-cp", "/app:/app/lib/*:/app/snapshot-lib/*",
  "org.springframework.boot.loader.launch.JarLauncher"
]