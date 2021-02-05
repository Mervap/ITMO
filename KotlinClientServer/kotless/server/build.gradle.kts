import io.kotless.plugin.gradle.dsl.kotless

plugins {
  java
  kotlin("jvm")
  id("application")
  id("distribution")
  id("io.kotless") version "0.1.7-beta-5" apply true
}

val kotlinxSerializationJsonVersion = project.property("kotlinx.serialization.json.version") as String
val kotlinxCliVersion = project.property("kotlinx.cli.version") as String
val ktorVersion = project.property("ktor.version") as String
val kotlessVersion = project.property("kotless.version") as String
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
  implementation("io.kotless:ktor-lang:$kotlessVersion")
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

tasks.withType<Copy>().named("processResources") {
  from("../client/build/distributions")
}

kotless {
  config {
    bucket = "mervap.kotless.bucket"

    terraform {
      profile = "default"
      region = "us-east-1"
    }
  }

  webapp {
    lambda {
      kotless {
        packages = setOf("me.mervap")
      }
    }
  }

  extensions {
    local {
      useAWSEmulation = true
    }
  }
}