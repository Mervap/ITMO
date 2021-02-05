plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization") version "1.4.21"
}

val kotlinxSerializationJsonVersion = project.property("kotlinx.serialization.json.version") as String

kotlin {
  targets {
    jvm()
    js {
      browser {
        binaries.executable()
        webpackTask {
          cssSupport.enabled = true
        }
        runTask {
          cssSupport.enabled = true
        }
        testTask {
          useKarma {
            useChromeHeadless()
            webpackConfig.cssSupport.enabled = true
          }
        }
      }
    }
  }
  sourceSets {
    commonMain {
      dependencies {
        implementation(kotlin("stdlib-common"))
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationJsonVersion")
      }
    }
  }
}