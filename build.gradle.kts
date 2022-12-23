import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("idea")
    id("java-library")
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21" apply false

    id("org.springframework.boot") version "2.7.5" apply false
    id("io.spring.dependency-management") version "1.1.0"
}

ext {
    set("kotlinVersion", "1.6.21")
    set("springBootVersion", "2.7.5")
}

subprojects {
    apply(plugin = "idea")
    apply(plugin = "kotlin")
    apply(plugin = "io.spring.dependency-management")

    repositories {
        mavenCentral()
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:${rootProject.ext["springBootVersion"]}")
        }
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect:${rootProject.ext["kotlinVersion"]}")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${rootProject.ext["kotlinVersion"]}")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }
}
