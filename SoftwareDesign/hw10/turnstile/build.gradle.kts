import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
}

val grpcVersion = project.property("grpc.version") as String
val grpcKotlinVersion = project.property("grpc.kotlin.version") as String

dependencies {
  implementation(project(":common"))
  implementation(project(":event_store"))
  implementation("io.grpc:grpc-api:$grpcVersion")
  implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.1")

  testImplementation(kotlin("test-junit5"))
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "11"
}