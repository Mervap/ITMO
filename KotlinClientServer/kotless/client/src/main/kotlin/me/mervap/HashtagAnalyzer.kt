package me.mervap

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.input.MInputAdornmentPosition
import com.ccfraser.muirwik.components.input.mInputAdornment
import com.ccfraser.muirwik.components.lab.alert.MAlertSeverity
import com.ccfraser.muirwik.components.lab.alert.mAlert
import kotlinext.js.js
import kotlinx.browser.window
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.mervap.utils.calcBarWidth
import me.mervap.utils.get
import me.mervap.utils.post
import me.mervap.wrappers.Plot
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.Node
import react.*
import react.dom.br
import styled.css
import styled.styledDiv

enum class LoadingState {
  READY {
    override val icon = "search"
  },
  LOADING1 {
    override val icon = "cached"
  },
  LOADING2 {
    override val icon = "loop"
  };

  abstract val icon: String
}

external interface HashtagAnalyzerProps : RProps {
  var currentUser: CurrentUser
  var hashtag: String
  var plotData: PlotData?
  var plotSaved: Boolean
  var onHashtagChange: (String) -> Unit
  var onPlotDataChange: (PlotData?) -> Unit
  var onPlotSavedChange: (Boolean) -> Unit
}

data class HashtagAnalyzerState(
  var loadingState: LoadingState,
  var timerId: Int,
  var currentInterval: SearchInterval,
  var saveAlertOpen: Boolean = false,
  var saveAlertSeverity: MAlertSeverity = MAlertSeverity.error,
  var saveAlertText: String = ""
) : RState

class HashtagAnalyzer : RComponent<HashtagAnalyzerProps, HashtagAnalyzerState>() {

  init {
    state = HashtagAnalyzerState(LoadingState.READY, -1, SearchInterval.DAY)
  }

  private fun startLoad() {
    window.clearInterval(state.timerId)
    val loadingTimerId = window.setInterval({
      setState {
        loadingState = when (loadingState) {
          LoadingState.READY -> LoadingState.LOADING1
          LoadingState.LOADING1 -> LoadingState.LOADING2
          LoadingState.LOADING2 -> LoadingState.LOADING1
        }
      }
    }, 150)
    setState {
      loadingState = LoadingState.LOADING1
      timerId = loadingTimerId
    }
    props.onPlotSavedChange(false)

    get("/hashtag_stat?hashtag=${props.hashtag}&search_interval=${state.currentInterval.name}") {
      val updateState = {
        window.clearInterval(state.timerId)
        setState {
          loadingState = LoadingState.READY
          timerId = -1
        }
      }
      onload = {
        updateState()
        if (status.toInt() == 200) {
          props.onPlotDataChange(Json.decodeFromString(responseText))
        }
      }
      onerror = { updateState() }
    }
  }

  private fun savePlot() {
    setState { saveAlertOpen = false }
    props.onPlotSavedChange(true)
    post("/save_plot", Json.encodeToString(props.plotData)) {
      val onError = {
        setState {
          saveAlertOpen = true
          saveAlertSeverity = MAlertSeverity.error
          saveAlertText = "Error during saving plot. Please, try again"
        }
        props.onPlotSavedChange(false)
      }
      onload = {
        if (status.toInt() == 200) {
          setState {
            saveAlertOpen = true
            saveAlertSeverity = MAlertSeverity.success
            saveAlertText = "Plot succeeded saved!"
          }
        }
        else onError()
      }
      onerror = { onError() }
    }
  }

  override fun RBuilder.render() {
    val screenWidth = window.screen.width
    val screenHeight = window.screen.height
    val hashtagIsNotEmpty = props.hashtag.isNotBlank()
    val hashtagContainsLatinsDigitsOnly = "\\w*".toRegex().matches(props.hashtag)
    styledDiv {
      css {
        display = Display.flex
        justifyContent = JustifyContent.center
        alignItems = Align.center
        height = LinearDimension("90%")
        width = LinearDimension("100%")
      }
      styledDiv {
        styledDiv {
          css {
            display = Display.flex
            justifyContent = JustifyContent.center
            alignItems = Align.center
            height = LinearDimension("90%")
          }
          mTextField("Hashtag", disabled = state.loadingState != LoadingState.READY) {
            css {
              width = LinearDimension("${screenWidth / 3}px")
              height = LinearDimension("30px")
            }
            attrs {
              value = props.hashtag
              placeholder = "winter2020"
              error = !hashtagContainsLatinsDigitsOnly
              helperText =
                if (hashtagContainsLatinsDigitsOnly) "by ${state.currentInterval.presentableString}"
                else "Hashtag can contains only latin chars & digits"
              @Suppress("unused")
              inputProps = object : RProps {
                val startAdornment = mInputAdornment { +"#" }
                val endAdornment = mInputAdornment(MInputAdornmentPosition.end) {
                  mIconButton(
                    state.loadingState.icon,
                    onClick = { startLoad() },
                    disabled = !hashtagIsNotEmpty || !hashtagContainsLatinsDigitsOnly || state.loadingState != LoadingState.READY
                  ) {}
                  mDivider(orientation = MDividerOrientation.vertical) {}
                  mIconButton(
                    "hourglass_empty",
                    onClick = { setState { currentInterval = currentInterval.next } },
                    disabled = state.loadingState != LoadingState.READY
                  ) {}
                }
              }
              onKeyPress = { event ->
                if (event.key === "Enter" && hashtagIsNotEmpty && hashtagContainsLatinsDigitsOnly) {
                  startLoad()
                }
              }
              onChange = { event ->
                props.onHashtagChange((event.target as HTMLInputElement).value)
              }
            }
          }
        }
        props.plotData?.let {
          val savePlotButtonText = "Save plot"
          val isOverflow = it.isOverflow
          val overflowSuffix = if (isOverflow) " (only last 1000 post)" else ""
          mSnackbar(
            open = state.saveAlertOpen,
            autoHideDuration = 7500,
            onClose = { _, _ -> setState { saveAlertOpen = false } },
            horizAnchor = MSnackbarHorizAnchor.center,
            vertAnchor = MSnackbarVertAnchor.top
          ) {
            mAlert(
              state.saveAlertText,
              severity = state.saveAlertSeverity,
              onClose = { setState { saveAlertOpen = false } }
            ) {}
          }
          br {}
          styledDiv {
            css {
              display = Display.flex
              justifyContent = JustifyContent.center
              alignItems = Align.center
              marginTop = LinearDimension("3rem")
            }
            if (it.x.isEmpty()) {
              css {
                fontSize = LinearDimension("2rem")
                opacity = 0.2
              }
              +"No info about #${it.hashtag} by ${it.searchInterval.presentableString}"
            }
            else {
              attrs {
                onClickFunction = { event ->
                  // Crutches...
                  if ((event.target as? Node)?.parentNode?.textContent == savePlotButtonText) {
                    when {
                      props.currentUser !is AuthenticatedUser -> {
                        setState {
                          saveAlertOpen = true
                          saveAlertSeverity = MAlertSeverity.error
                          saveAlertText = "You have to login to save plots"
                        }
                      }
                      props.plotSaved -> {
                        setState {
                          saveAlertOpen = true
                          saveAlertSeverity = MAlertSeverity.warning
                          saveAlertText = "This plot already saved"
                        }
                      }
                      else -> savePlot()
                    }
                  }
                }
              }
              val barWidth = calcBarWidth(it.x)
              child(Plot::class) {
                attrs.asDynamic().data =
                  arrayOf(
                    js {
                      x = it.x.toTypedArray()
                      y = it.y.toTypedArray()
                      this.type = "bar"
                      width = barWidth
                    }
                  )
                attrs.asDynamic().layout =
                  js {
                    width = screenWidth / 2
                    height = screenHeight / 1.75
                    title = "#${it.hashtag}$overflowSuffix"
                    updatemenus = arrayOf(js {
                      buttons = arrayOf(js {
                        args = arrayOf("")
                        this.label = savePlotButtonText
                        method = "update"
                      })
                      showactive = false
                      this.type = "buttons"
                      x = 0.1
                      y = 1.1
                    })
                  }
              }
            }
          }
        }
      }
    }
  }
}
