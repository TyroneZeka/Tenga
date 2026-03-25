import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    id("org.springframework.boot") version "3.3.5"
    id("com.diffplug.spotless") version "6.25.0"
}

group = "com.tenga"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

extra["mapstructVersion"] = "1.6.0"
extra["testcontainersVersion"] = "1.20.1"
extra["jwtVersion"] = "0.12.6"

dependencies {
    // Spring Boot BOM — replaces io.spring.dependency-management plugin
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    annotationProcessor(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    testImplementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    testAnnotationProcessor(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))

    // Testcontainers BOM
    testImplementation(platform("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}"))

    // Spring Boot starters
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    // Kafka
    implementation("org.springframework.kafka:spring-kafka")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:${property("jwtVersion")}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${property("jwtVersion")}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${property("jwtVersion")}")

    // Database
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    // PostGIS support
    implementation("org.hibernate.orm:hibernate-spatial")
    implementation("org.locationtech.jts:jts-core:1.19.0")

    // Elasticsearch
    implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")

    // AWS S3 / MinIO
    implementation("software.amazon.awssdk:s3:2.28.0")
    implementation("software.amazon.awssdk:sts:2.28.0")

    // MapStruct
    implementation("org.mapstruct:mapstruct:${property("mapstructVersion")}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${property("mapstructVersion")}")

    // OpenAPI / Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

    // Utilities
    implementation("com.google.guava:guava:33.3.1-jre")

    // Sentry
    implementation("io.sentry:sentry-spring-boot-starter-jakarta:7.14.0")

    // Lombok (optional — project uses records, but useful for builders/logging)
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:kafka")
    testImplementation("org.testcontainers:elasticsearch")
    testImplementation("com.redis:testcontainers-redis:2.2.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<BootJar>("bootJar") {
    archiveFileName.set("tenga-backend.jar")
}

spotless {
    java {
        googleJavaFormat("1.23.0")
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
    }
}
