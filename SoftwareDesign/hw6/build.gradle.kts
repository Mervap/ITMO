import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  kotlin("jvm") version "1.4.10"
  application
}

group = "itmo.mervap"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(kotlin("test-junit5"))
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

tasks.test {
  useJUnitPlatform()
  testLogging {
    events = setOf(FAILED, PASSED)
  }
}

tasks.withType<KotlinCompile>() {
  kotlinOptions.jvmTarget = "11"
}

application {
  mainClassName = "itmo.mervap.hw6.MainKt"
}