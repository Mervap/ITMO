package me.mervap.manager.client

import com.ccfraser.muirwik.components.MColor
import com.ccfraser.muirwik.components.button.mButton
import kotlinx.css.LinearDimension
import kotlinx.css.marginTop
import me.mervap.manager.client.RedirectTo.*
import react.*
import react.router.dom.redirect
import styled.css
import styled.styledDiv

internal enum class RedirectTo {
  NO_REDIRECT,
  BUY_SUBSCRIPTION,
  EXTEND_SUBSCRIPTION
}

internal data class MainMenuState(
  var redirectTo: RedirectTo
) : RState

internal class MainMenu : RComponent<RProps, MainMenuState>() {

  init {
    state = MainMenuState(NO_REDIRECT)
  }

  override fun RBuilder.render() {
    when (state.redirectTo) {
      BUY_SUBSCRIPTION -> redirect(to = "/buy")
      EXTEND_SUBSCRIPTION -> redirect(to = "/extend")
      NO_REDIRECT -> styledDiv {
        css {
          marginTop = LinearDimension("2.5rem")
        }
        styledDiv {
          mButton(
            "Buy subscription",
            color = MColor.secondary,
            onClick = {
              setState { redirectTo = BUY_SUBSCRIPTION }
            }
          )
        }
        styledDiv {
          mButton(
            "Extend subscription",
            color = MColor.primary,
            onClick = {
              setState { redirectTo = EXTEND_SUBSCRIPTION }
            }
          )
        }
      }
    }
  }
}
