plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
}

group = "ru.bmstu.dvasev.rsoi.microservices"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation(project(":common-api"))
    implementation(project(":gateway-api"))
    implementation(project(":cars-api"))
    implementation(project(":payment-api"))
    implementation(project(":rental-api"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.retry:spring-retry")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")

    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.0")
    implementation("net.logstash.logback:logstash-logback-encoder:7.2")
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.21")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.1")
}
