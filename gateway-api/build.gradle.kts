plugins {
    kotlin("jvm")
}

group = "ru.bmstu.dvasev.rsoi.microservices"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":payment-api"))
    implementation(project(":rental-api"))

    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.0")
}
