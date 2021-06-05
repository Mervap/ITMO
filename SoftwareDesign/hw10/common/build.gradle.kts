plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
}

val kotlinxSerializationJsonVersion = project.property("kotlinx.serialization.json.version") as String
val kotlinxDatetimeVersion = project.property("kotlinx.datetime.version") as String

kotlin {
  targets {
    jvm {
      compilations.all {
        kotlinOptions.jvmTarget = "11"
      }
      testRuns["test"].executionTask.configure {
        useJUnitPlatform()
      }
      withJava()
    }
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
    all {
      languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
    }
    commonMain {
      dependencies {
        implementation(kotlin("stdlib-common"))
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationJsonVersion")
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetimeVersion")
      }
    }
  }
}