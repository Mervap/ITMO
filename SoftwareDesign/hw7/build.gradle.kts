import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED

val aspectjVersion by extra("1.9.6")

plugins {
  java
  kotlin("jvm") version "1.4.21"
  id("io.freefair.aspectj.post-compile-weaving") version "5.3.0"
}

group = "me.mervap.hw7"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}


dependencies {
  implementation(kotlin("stdlib"))
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
  testImplementation("org.junit.jupiter:junit-jupiter-params:5.3.0")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

sourceSets {
  main {
    java {
      srcDirs("src")
    }
  }
}

tasks.test {
  useJUnitPlatform()
  testLogging {
    events = setOf(FAILED, PASSED)
  }
}