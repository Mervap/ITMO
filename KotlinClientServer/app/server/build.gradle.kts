plugins {
  java
  kotlin("jvm")
  id("application")
  id("distribution")
}

val kotlinxSerializationJsonVersion = project.property("kotlinx.serialization.json.version") as String
val kotlinxCliVersion = project.property("kotlinx.cli.version") as String
val ktorVersion = project.property("ktor.version") as String
val logbackVersion = project.property("logback.version") as String
val exposedVersion = project.property("exposed.version") as String
val postgresqlVersion = project.property("postgresql.version") as String
val khttpVersion = project.property("khttp.version") as String
val gsonVersion = project.property("gson.version") as String

dependencies {
  implementation(kotlin("stdlib"))
  implementation(project(":common"))
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationJsonVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-cli:$kotlinxCliVersion")
  implementation("io.ktor:ktor-server-netty:$ktorVersion")
  implementation("io.ktor:ktor-html-builder:$ktorVersion")
  implementation("io.ktor:ktor-auth:$ktorVersion")
  implementation("ch.qos.logback:logback-classic:$logbackVersion")
  implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
  implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
  implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
  implementation("org.jetbrains.exposed:exposed-jodatime:$exposedVersion")
  implementation("org.postgresql:postgresql:$postgresqlVersion")
  implementation("khttp:khttp:$khttpVersion")
}

application {
  mainClass.set("me.mervap.MainKt")
}

tasks.withType<Copy>().named("processResources") {
  from(project(":client").tasks.named("browserDistribution"))
}