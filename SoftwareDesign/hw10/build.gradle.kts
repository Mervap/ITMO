plugins {
  kotlin("multiplatform") version "1.4.30" apply false
  kotlin("plugin.serialization") version "1.4.30" apply false
}

allprojects {
  group = "me.mervap"
  version = "1.0"

  repositories {
    mavenCentral()
    jcenter()
  }
}