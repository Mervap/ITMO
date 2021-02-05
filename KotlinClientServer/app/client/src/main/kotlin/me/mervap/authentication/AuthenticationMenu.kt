package me.mervap.authentication

import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.dialog.onClose
import com.ccfraser.muirwik.components.menu.mMenu
import com.ccfraser.muirwik.components.menu.mMenuItemWithIcon
import kotlinx.css.*
import me.mervap.*
import me.mervap.utils.get
import org.w3c.dom.Node
import org.w3c.dom.events.Event
import react.*
import react.router.dom.routeLink
import styled.*
import styled.styledDiv

external interface AuthenticationMenuProps : RProps {
  var currentUser: CurrentUser
  var onAuthorization: (String) -> Unit
  var onLogout: () -> Unit
}

data class AuthenticationMenuState(var anchorEl: Node?) : RState

class AuthenticationMenu(props: AuthenticationMenuProps) :
  RComponent<AuthenticationMenuProps, AuthenticationMenuState>(props) {

  init {
    state = AuthenticationMenuState(null)
  }

  private fun handleClick(event: Event) {
    if (state.anchorEl != null) return
    val currentTarget = event.currentTarget
    setState { anchorEl = currentTarget.asDynamic() as? Node }
  }

  private fun handleClose() {
    setState { anchorEl = null }
  }

  private val isAuthenticated: Boolean
    get() = when (props.currentUser) {
      is GuestUser -> false
      is AuthenticatedUser -> true
      is UnknownUser -> {
        get("/get_username") {
          onload = {
            if (status.toInt() == 200) props.onAuthorization(responseText)
            else props.onLogout()
          }
          onerror = { props.onLogout() }
        }
        false
      }
    }

  override fun RBuilder.render() {
    styledDiv {
      css { display = Display.flex }
      styledDiv {
        css {
          display = Display.flex
          justifyContent = JustifyContent.flexStart
          padding = "0.25rem 0 0 0.25rem"
        }
        routeLink("/", className = "text-link") {
          mIconButton("home") {}
        }
        if (props.currentUser is AuthenticatedUser) {
          routeLink("/storage", className = "text-link") {
            mIconButton("storage") {}
          }
        }
      }
      styledDiv {
        css {
          display = Display.flex
          justifyContent = JustifyContent.flexEnd
          padding = "1rem 1rem 0 0"
          marginLeft = LinearDimension.auto
        }
        styledDiv {
          css {
            borderWidth = LinearDimension("1px")
            borderStyle = BorderStyle.solid
            borderRadius = LinearDimension("5px")
            borderColor = Color("#3f51b5")
            height = LinearDimension.maxContent
          }

          mButton("Welcome, ${props.currentUser.username}!", onClick = { handleClick(it) }) {
            mMenu(false) {
              attrs {
                elevation = 0
                anchorEl = state.anchorEl
                open = state.anchorEl != null
                onClose = { _, _ -> handleClose() }
              }
              if (isAuthenticated) {
                routeLink("/", className = "text-link") {
                  mMenuItemWithIcon("exit_to_app", "Logout") {
                    attrs {
                      onClick = {
                        get("/logout") {
                          onload = {
                            if (status.toInt() == 200) props.onLogout()
                          }
                        }
                        handleClose()
                      }
                    }
                  }
                }
              }
              else {
                routeLink("/login", className = "text-link") {
                  mMenuItemWithIcon("person", "Login", onClick = { handleClose() })
                }
                routeLink("/registration", className = "text-link") {
                  mMenuItemWithIcon("person_add", "Register", onClick = { handleClose() })
                }
              }
            }
          }
        }
      }
    }
  }
}
