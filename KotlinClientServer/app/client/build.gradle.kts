plugins {
  kotlin("js")
}

repositories {
  maven("https://dl.bintray.com/kotlin/kotlin-js-wrappers")
  maven("https://dl.bintray.com/mipt-npm/dataforge")
  maven("https://dl.bintray.com/mipt-npm/kscience")
  maven("https://dl.bintray.com/mipt-npm/dev")
}

val kotlinxSerializationJsonVersion = project.property("kotlinx.serialization.json.version") as String
val kotlinReactVersion = project.property("kotlin.react.version") as String
val kotlinStyledVersion = project.property("kotlin.styled.version") as String
val kotlinReactRouterVersion = project.property("kotlin.react.router.version") as String
val kotlinReduxVersion = project.property("kotlin.redux.version") as String
val kotlinReactReduxVersion = project.property("kotlin.react.redux.version") as String
val muirwikComponentsVersion = project.property("muirwik.components.version") as String
val plotlyJsVersion = project.property("plotly.js.version") as String
val reactPlotlyJsVersion = project.property("react.plotly.js.version") as String
val cryptoJsVersion = project.property("crypto.js.version") as String

dependencies {
  implementation(kotlin("stdlib-js"))
  implementation(project(":common"))
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationJsonVersion")
  implementation("org.jetbrains:kotlin-react:$kotlinReactVersion")
  implementation("org.jetbrains:kotlin-react-dom:$kotlinReactVersion")
  implementation("org.jetbrains:kotlin-styled:$kotlinStyledVersion")
  implementation("org.jetbrains:kotlin-react-router-dom:$kotlinReactRouterVersion")
  implementation("org.jetbrains:kotlin-redux:$kotlinReduxVersion")
  implementation("org.jetbrains:kotlin-react-redux:$kotlinReactReduxVersion")
  implementation("com.ccfraser.muirwik:muirwik-components:$muirwikComponentsVersion")
  implementation(npm("plotly.js", plotlyJsVersion))
  implementation(npm("react-plotly.js", reactPlotlyJsVersion))
}

kotlin {
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