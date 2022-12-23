plugins {
    kotlin("jvm")
}

group = "ru.bmstu.dvasev.rsoi.microservices"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-web:5.3.23")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.0")
    implementation("com.fasterxml.jackson.core:jackson-core:2.14.0")
}
