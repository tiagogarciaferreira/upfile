import org.springframework.boot.gradle.tasks.bundling.BootJar
import java.net.InetAddress
import java.time.Instant

object Versions {
    const val SPRINGDOC_OPENAPI = "3.0.2"
    const val BOUNCY_CASTLE = "1.84"
    const val GOOGLE_TINK = "1.21.0"
    const val AWS_SDK_S3 = "2.44.4"
    const val MAP_STRUCT = "1.6.3"
    const val MAP_STRUCT_BINDING = "0.2.0"
    const val TIKA_CORE = "3.3.0"
    const val HASH4J = "0.30.0"
}

plugins {
    idea
    java
    id("org.springframework.boot") version "4.0.6"
    id("io.spring.dependency-management") version "1.1.7"
}

description = "File upload and storage service"
group = "com.tgfcodes.upfile"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

springBoot {
    mainClass.set("$group.UpfileApplication")
    buildInfo {
        properties {
            name.set(project.name)
            artifact.set(project.name)
            time.set(Instant.now().toString())
            version.set(project.version.toString())
            additional.set(mapOf("description" to (project.description ?: "")))
            excludes.set(setOf("group"))
        }
    }
}

tasks.named<BootJar>("bootJar") {
    archiveBaseName.set(project.name)
    archiveVersion.set(project.version.toString())

    val buildTime = Instant.now()
    manifest {
        attributes(
            "Main-Class" to "org.springframework.boot.loader.launch.JarLauncher",
            "Start-Class" to "${project.group}.UpfileApplication",
            "Implementation-Name" to project.name,
            "Implementation-Title" to "upfile-service",
            "Implementation-Version" to project.version.toString(),
            "Implementation-Description" to project.description.toString(),
            "Implementation-Vendor" to "tgfcodes",
            "Implementation-Vendor-Id" to "com.tgfcodes",
            "Implementation-Vendor-Url" to "https://github.com/tiagogarciaferreira",
            "Implementation-Url" to "https://github.com/tiagogarciaferreira/upfile",
            "Implementation-Timestamp" to buildTime.toEpochMilli(),
            "Implementation-Build-Time" to buildTime.toString(),
            "Implementation-Build-Jdk-Vendor" to System.getProperty("java.vendor"),
            "Implementation-Build-Jdk-Spec" to System.getProperty("java.specification.version"),
            "Implementation-Build-Jdk" to System.getProperty("java.version"),
            "Implementation-Build-User" to (System.getProperty("user.name") ?: "unknown"),
            "Implementation-Build-Host" to runCatching { InetAddress.getLocalHost().hostName }.getOrDefault("unknown"),
            "Implementation-Build-Git-Commit" to (System.getenv("GIT_COMMIT") ?: "local"),
            "Implementation-Build-Git-Branch" to (System.getenv("GIT_BRANCH") ?: "local"),
            "Implementation-Build-Git-Tag" to (System.getenv("GIT_TAG") ?: "none")
        )
    }

    layered {
        isEnabled = true
        includeTools = true
    }
}

tasks.named<Jar>("jar") {
    enabled = false
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-flyway")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    //implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("software.amazon.awssdk:s3:${Versions.AWS_SDK_S3}")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${Versions.SPRINGDOC_OPENAPI}")
    implementation("org.bouncycastle:bcpkix-jdk18on:${Versions.BOUNCY_CASTLE}")
    implementation("com.google.crypto.tink:tink:${Versions.GOOGLE_TINK}")
    implementation("org.mapstruct:mapstruct:${Versions.MAP_STRUCT}")
    implementation("org.apache.tika:tika-core:${Versions.TIKA_CORE}")
    implementation("com.dynatrace.hash4j:hash4j:${Versions.HASH4J}")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor:${Versions.MAP_STRUCT}")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:${Versions.MAP_STRUCT_BINDING}")
    annotationProcessor("org.projectlombok:lombok")
    runtimeOnly("org.flywaydb:flyway-database-postgresql")
    runtimeOnly("org.postgresql:postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register("printVersion") {
    group = "Help"
    description = "Prints the project version."
    doLast {
        println(project.version.toString())
    }
}