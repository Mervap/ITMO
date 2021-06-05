import com.google.protobuf.gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm")
  id("com.google.protobuf") version "0.8.15"
  kotlin("plugin.serialization")
}

val kotlinxSerializationJsonVersion = project.property("kotlinx.serialization.json.version") as String
val kotlinxDatetimeVersion = project.property("kotlinx.datetime.version") as String
val kotlinxCoroutinesCoreVersion = project.property("kotlinx.coroutines.core.version") as String
val grpcVersion = project.property("grpc.version") as String
val grpcKotlinVersion = project.property("grpc.kotlin.version") as String
val protobufVersion = project.property("protobuf.version") as String
val javaxAnnotationApiVersion = project.property("javax.annotation.api.version") as String
val kmongoCoroutineVersion = project.property("kmongo.coroutine.version") as String

dependencies {
  implementation(project(":common"))
  implementation("com.google.protobuf:protobuf-java:$protobufVersion")
  implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
  compileOnly("javax.annotation:javax.annotation-api:$javaxAnnotationApiVersion")
  runtimeOnly("io.grpc:grpc-netty-shaded:$grpcVersion")
  protobuf(files("protos/"))

  implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetimeVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationJsonVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesCoreVersion")
  implementation("org.litote.kmongo:kmongo-coroutine-serialization:$kmongoCoroutineVersion")

  testImplementation(kotlin("test-junit5"))
  testImplementation("org.testcontainers:junit-jupiter:1.15.2")
  testImplementation("org.testcontainers:mongodb:1.15.2")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

protobuf {
  generatedFilesBaseDir = "$projectDir/gen-grpc"
  protoc {
    artifact = "com.google.protobuf:protoc:$protobufVersion"
  }
  plugins {
    id("grpc") {
      artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
    }
    id("grpckt") {
      artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk7@jar"
    }
  }
  generateProtoTasks {
    ofSourceSet("main").forEach {
      it.plugins {
        id("grpc")
        id("grpckt")
      }
    }
  }
}

sourceSets {
  main {
    java {
      srcDir("gen-grpc/main/grpc")
      srcDir("gen-grpc/main/grpckt")
      srcDir("gen-grpc/main/java")
    }
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "11"
  kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}