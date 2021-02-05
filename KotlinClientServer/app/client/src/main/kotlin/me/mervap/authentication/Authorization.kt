package me.mervap.authentication

import com.ccfraser.muirwik.components.MColor
import com.ccfraser.muirwik.components.button.MButtonVariant
import com.ccfraser.muirwik.components.button.color
import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.button.variant
import com.ccfraser.muirwik.components.lab.alert.MAlertSeverity
import com.ccfraser.muirwik.components.lab.alert.mAlert
import com.ccfraser.muirwik.components.mTextField
import com.ccfraser.muirwik.components.targetValue
import kotlinx.css.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.mervap.AuthenticatedUser
import me.mervap.CurrentUser
import me.mervap.UserCredentials
import me.mervap.utils.post
import react.*
import react.dom.br
import react.router.dom.redirect
import styled.css
import styled.styledDiv

external interface AuthorizationProps : RProps {
  var currentUser: CurrentUser
  var isRegistration: Boolean
  var onAuthorization: (String) -> Unit
}

data class AuthorizationState(
  var userCredentials: UserCredentials,
  var passwordConfirm: String = "",
  var usernameError: String? = null,
  var passwordError: String? = null,
  var passwordConfirmError: String? = null,
  var errorMessage: String? = null,
  var loading: Boolean = false
) : RState

class Authorization(props: AuthorizationProps) : RComponent<AuthorizationProps, AuthorizationState>(props) {

  init {
    state = AuthorizationState(UserCredentials("", ""))
  }

  private fun validateForm(): Boolean {
    val (username, password) = state.userCredentials
    var isOk = true
    if (!"\\w{2,}".toRegex().matches(username)) {
      setState { usernameError = "Username should contains only latin characters/digits and len should be >= 2" }
      isOk = false
    }
    if (!"\\w{6,}".toRegex().matches(password)) {
      setState { passwordError = "Password should contain at least 6 chars" }
      isOk = false
    }
    if (password != state.passwordConfirm) {
      setState { passwordConfirmError = "Passwords doesn't matches" }
      isOk = false
    }
    return isOk
  }

  private fun handleClick() {
    if (props.isRegistration && !validateForm()) return
    setState { loading = true }
    val path = if (props.isRegistration) "registration" else "login"
    post("/$path", Json.encodeToString(state.userCredentials)) {
      val onError = {
        setState {
          errorMessage = "Unexpected error"
          loading = false
        }
      }
      onload = {
        when (status.toInt()) {
          200 -> {
            props.onAuthorization(state.userCredentials.username)
            setState {
              errorMessage = null
              loading = false
            }
          }
          401 -> setState {
            errorMessage = responseText
            loading = false
          }
          else -> onError()
        }
      }
      onerror = { onError() }
    }
  }

  override fun RBuilder.render() {
    if (props.currentUser is AuthenticatedUser) {
      redirect(to = "/")
      return
    }

    styledDiv {
      css {
        textAlign = TextAlign.center
        display = Display.flex
        justifyContent = JustifyContent.center
        alignItems = Align.center
        height = LinearDimension("50%")
        width = LinearDimension("100%")
      }
      styledDiv {
        if (state.errorMessage != null) {
          mAlert(state.errorMessage, severity = MAlertSeverity.error)
          br {}
        }
        mTextField("Username") {
          css {
            width = LinearDimension("20rem")
          }
          attrs {
            value = state.userCredentials.username
            error = state.usernameError != null
            helperText = state.usernameError ?: ""
            onChange = {
              setState {
                userCredentials.username = it.targetValue as String
                usernameError = null
              }
            }
          }
        }
        br {}
        mTextField("Password") {
          css {
            width = LinearDimension("20rem")
          }
          attrs {
            type = "password"
            value = state.userCredentials.password
            error = state.passwordError != null
            helperText = state.passwordError ?: ""
            onChange = {
              setState {
                userCredentials.password = it.targetValue as String
                passwordError = null
                passwordConfirmError = null
              }
            }
          }
        }
        if (props.isRegistration) {
          br {}
          mTextField("Password confirmation") {
            css {
              width = LinearDimension("20rem")
            }
            attrs {
              type = "password"
              value = state.passwordConfirm
              error = state.passwordConfirmError != null
              helperText = state.passwordConfirmError ?: ""
              onChange = {
                setState {
                  passwordConfirm = it.targetValue as String
                  passwordError = null
                  passwordConfirmError = null
                }
              }
            }
          }
        }
        styledDiv {
          css {
            marginTop = LinearDimension("1rem")
          }
        }
        val buttonText = if (props.isRegistration) "Register" else "Login"
        mButton(buttonText, disabled = state.loading) {
          attrs {
            variant = MButtonVariant.contained
            color = MColor.primary
            onClick = {
              setState {
                usernameError = null
                passwordError = null
                passwordConfirmError = null
              }
              handleClick()
            }
          }
        }
      }
    }
  }
}
