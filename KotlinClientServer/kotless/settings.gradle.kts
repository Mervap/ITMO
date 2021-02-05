pluginManagement {
  resolutionStrategy {
    eachPlugin {
      if (requested.id.id == "kotlinx-serialization") {
        useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
      }
    }
  }

  repositories {
    gradlePluginPortal()
    maven("https://kotlin.bintray.com/kotlinx")
  }
}

rootProject.name = "hashtag-analyzer-kotless"

include("common")
include("client")
include("server")
