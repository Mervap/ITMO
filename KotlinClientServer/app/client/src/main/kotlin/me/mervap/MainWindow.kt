package me.mervap

import com.ccfraser.muirwik.components.mThemeProvider
import me.mervap.authentication.AuthenticationMenu
import me.mervap.authentication.Authorization
import react.*
import react.router.dom.browserRouter
import react.router.dom.redirect
import react.router.dom.route
import react.router.dom.switch

sealed class CurrentUser(val username: String)
object UnknownUser : CurrentUser("Guest")
object GuestUser : CurrentUser("Guest")
class AuthenticatedUser(username: String) : CurrentUser(username)

data class MainWindowState(
  var currentUser: CurrentUser,
  var hashtag: String,
  var plotData: PlotData?,
  var plotSaved: Boolean
) : RState

class MainWindow(props: RProps) : RComponent<RProps, MainWindowState>(props) {

  init {
    state = MainWindowState(UnknownUser, "", null, false)
  }

  private fun RBuilder.authorizationPage(isRegistration: Boolean): ReactElement =
    child(Authorization::class) {
      attrs {
        currentUser = state.currentUser
        this.isRegistration = isRegistration
        onAuthorization = { setState { currentUser = AuthenticatedUser(it) } }
      }
    }

  override fun RBuilder.render() {
    mThemeProvider {
      browserRouter {
        child(AuthenticationMenu::class) {
          attrs {
            currentUser = state.currentUser
            onAuthorization = { setState { currentUser = AuthenticatedUser(it) } }
            onLogout = { setState { currentUser = GuestUser } }
          }
        }
        switch {
          route("/", exact = true) {
            child(HashtagAnalyzer::class) {
              attrs {
                currentUser = state.currentUser
                hashtag = state.hashtag
                plotData = state.plotData
                plotSaved = state.plotSaved
                onHashtagChange = { setState { hashtag = it } }
                onPlotDataChange = { setState { plotData = it } }
                onPlotSavedChange = { setState { plotSaved = it } }
              }
            }
          }
          route("/login") { authorizationPage(false) }
          route("/registration") { authorizationPage(true) }
          route("/storage") {
            child(Storage::class) { attrs { currentUser = state.currentUser } }
          }
          redirect(from = "/", to = "/")
        }
      }
    }
  }
}
