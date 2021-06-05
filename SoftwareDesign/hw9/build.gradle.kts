import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.5.0-M1"
  application
}

group = "me.mervap"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  implementation("io.reactivex.rxjava2:rxkotlin:2.4.0")
  implementation("io.reactivex:rxnetty-http:0.5.3")
  implementation("io.reactivex:rxnetty-common:0.5.3")
  implementation("io.reactivex:rxnetty-tcp:0.5.3")
  implementation("io.netty:netty-all:4.1.60.Final")
  implementation("org.mongodb:mongodb-driver-rx:1.5.0")
  testImplementation(kotlin("test-junit5"))
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

tasks.test {
  useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
  kotlinOptions.jvmTarget = "11"
}

application {
  mainClassName = "me.mervap.MainKt"
}