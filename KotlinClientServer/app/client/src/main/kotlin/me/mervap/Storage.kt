package me.mervap

import com.ccfraser.muirwik.components.button.MIconButtonSize
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.lab.alert.MAlertSeverity
import com.ccfraser.muirwik.components.lab.alert.mAlert
import com.ccfraser.muirwik.components.mDivider
import kotlinext.js.js
import kotlinx.browser.window
import kotlinx.css.*
import kotlinx.html.DIV
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import me.mervap.utils.calcBarWidth
import me.mervap.utils.get
import me.mervap.wrappers.Plot
import react.*
import react.router.dom.redirect
import styled.StyledDOMBuilder
import styled.css
import styled.styledDiv

enum class LoadingStage {
  NOT_STARTED,
  IN_PROGRESS,
  SUCCESS,
  ERROR
}

external interface StorageProps : RProps {
  var currentUser: CurrentUser
}

data class StorageState(var savedPlots: List<SavedPlot>, var loadingStage: LoadingStage) : RState

class Storage(props: StorageProps) : RComponent<StorageProps, StorageState>(props) {

  init {
    state = StorageState(emptyList(), LoadingStage.NOT_STARTED)
  }

  private fun startLoadPlots() {
    setState { loadingStage = LoadingStage.IN_PROGRESS }
    get("/get_saved_plot") {
      val onError = { setState { loadingStage = LoadingStage.ERROR } }
      onload = {
        if (status.toInt() == 200) {
          val savedPlots = Json.decodeFromString<List<SavedPlot>>(responseText)
          setState {
            this.savedPlots = savedPlots
            this.loadingStage = LoadingStage.SUCCESS
          }
        }
        else onError()
      }
      onerror = { onError() }
    }
  }

  private fun RBuilder.centeredStyledDiv(block: StyledDOMBuilder<DIV>.() -> Unit) = styledDiv {
    css {
      display = Display.flex
      justifyContent = JustifyContent.center
      alignItems = Align.center
      height = LinearDimension("90%")
      width = LinearDimension("100%")
    }
    block()
  }

  override fun RBuilder.render() {

    if (props.currentUser !is AuthenticatedUser) {
      redirect(to = "/")
      return
    }

    when (state.loadingStage) {
      LoadingStage.NOT_STARTED -> startLoadPlots()
      LoadingStage.IN_PROGRESS -> centeredStyledDiv {
        css {
          fontSize = LinearDimension("6rem")
          opacity = 0.3
        }
        +"Loading..."
      }
      LoadingStage.ERROR -> centeredStyledDiv {
        mAlert(
          "Error during loading plots",
          severity = MAlertSeverity.error,
          closeText = "Retry"
        ) {
          attrs {
            action = mIconButton(
              "replay",
              size = MIconButtonSize.small,
              addAsChild = false,
              onClick = { setState { loadingStage = LoadingStage.NOT_STARTED } }
            )
          }
        }
      }
      LoadingStage.SUCCESS -> centeredStyledDiv {
        css {
          overflowX = Overflow.auto
        }
        if (state.savedPlots.isEmpty()) {
          css {
            fontSize = LinearDimension("5rem")
            opacity = 0.3
          }
          +"No saved plot found"
        }
        else {
          styledDiv {
            css {
              height = LinearDimension("100%")
            }
            val screenWidth = window.screen.width
            val screenHeight = window.screen.height
            for ((plotData, saveTime) in state.savedPlots) {
              val barWidth = calcBarWidth(plotData.x)
              child(Plot::class) {
                attrs.asDynamic().data =
                  arrayOf(
                    js {
                      x = plotData.x.toTypedArray()
                      y = plotData.y.toTypedArray()
                      this.type = "bar"
                      width = barWidth
                    }
                  )
                attrs.asDynamic().layout =
                  js {
                    width = screenWidth / 2.25
                    height = screenHeight / 2
                    title = """
                      #${plotData.hashtag} <br>
                      Looking at $saveTime <br>
                      Search by ${plotData.searchInterval.presentableString}
                    """.trimIndent()
                  }
              }
              mDivider {}
            }
          }
        }
      }
    }
  }
}
