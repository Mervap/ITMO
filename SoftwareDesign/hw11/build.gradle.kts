import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.4.30"
  application
}

val grpcVersion = project.property("grpc.version") as String
val grpcKotlinVersion = project.property("grpc.kotlin.version") as String
val kotlinxCoroutinesCoreVersion = project.property("kotlinx.coroutines.core.version") as String

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":market"))
  implementation("io.grpc:grpc-api:$grpcVersion")
  implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")

  testImplementation(kotlin("test-junit5"))
  testImplementation("org.testcontainers:testcontainers:1.15.2")
  testImplementation("org.testcontainers:junit-jupiter:1.15.2")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesCoreVersion")
}

tasks.test {
  useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "11"
}

application {
  mainClassName = "MainKt"
}


allprojects {
  group = "me.mervap"
  version = "1.0"

  repositories {
    mavenCentral()
    jcenter()
  }
}