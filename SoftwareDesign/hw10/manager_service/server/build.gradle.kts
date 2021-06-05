import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  java
  kotlin("jvm")
}

val kotlinxSerializationJsonVersion = project.property("kotlinx.serialization.json.version") as String
val kotlinxDatetimeVersion = project.property("kotlinx.datetime.version") as String
val ktorVersion = project.property("ktor.version") as String
val grpcVersion = project.property("grpc.version") as String
val grpcKotlinVersion = project.property("grpc.kotlin.version") as String
val protobufVersion = project.property("protobuf.version") as String

dependencies {
  implementation(kotlin("stdlib"))
  implementation(project(":common"))
  implementation(project(":event_store"))
  implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetimeVersion")
  implementation("io.grpc:grpc-api:$grpcVersion")
  implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationJsonVersion")
  implementation("io.ktor:ktor-server-netty:$ktorVersion")
  implementation("io.ktor:ktor-html-builder:$ktorVersion")
}

tasks.withType<Copy>().named("processResources") {
  from(project(":manager_service:client").tasks.named("browserDistribution"))
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "11"
}