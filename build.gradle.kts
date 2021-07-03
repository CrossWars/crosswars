import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.20"
    kotlin("plugin.spring") version "1.5.20"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.5.20"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.5.20"
}

group = "xyz.crossward"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_16

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "16"
    }
}

// Allow spring to extend classes with these annotations
allOpen {
    annotation("org.springframework.stereotype.Component")
    annotation("org.springframework.stereotype.Async")
    annotation("org.springframework.stereotype.Transactional")
    annotation("org.springframework.stereotype.Cacheable")
    annotation("org.springframework.stereotype.SpringBootTest")
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.3")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("com.google.api-client:google-api-client:1.32.1")

    // Database
    implementation("org.xerial:sqlite-jdbc:3.34.0")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.6.0")
    testImplementation("io.kotest:kotest-assertions-core-jvm:4.6.0")
    testImplementation("io.kotest:kotest-property-jvm:4.6.0")
    testImplementation("io.mockk:mockk:1.11.0")
    testImplementation("com.ninja-squad:springmockk:3.0.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
