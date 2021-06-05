package me.mervap.manager.client

import com.ccfraser.muirwik.components.mThemeProvider
import com.ccfraser.muirwik.components.table.*
import kotlinx.browser.window
import kotlinx.css.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import me.mervap.SubscriptionInfo
import me.mervap.manager.client.utils.get
import react.*
import react.router.dom.browserRouter
import react.router.dom.redirect
import react.router.dom.route
import react.router.dom.switch
import styled.css
import styled.styledDiv

internal data class MainWindowState(
  var infos: List<SubscriptionInfo>? = null
) : RState

internal class MainWindow(props: RProps) : RComponent<RProps, MainWindowState>(props) {

  private fun updInfos() {
    get("/all_subs") {
      onload = {
        window.setTimeout(::updInfos, 1000)
        val newInfos = Json.decodeFromString<List<SubscriptionInfo>>(responseText)
        setState { infos = newInfos }
      }
      onerror = {
        window.setTimeout(::updInfos, 1000)
      }
    }
  }

  override fun RBuilder.render() {
    mThemeProvider {
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
            browserRouter {
              switch {
                route("/", exact = true) { child(MainMenu::class) {} }
                route("/buy", exact = true) {
                  child(UpdateSub::class) {
                    attrs { isExtend = false }
                  }
                }
                route("/extend", exact = true) {
                  child(UpdateSub::class) {
                    attrs { isExtend = true }
                  }
                }
                redirect(from = "/", to = "/")
              }
            }
            styledDiv {
              css {
                marginLeft = LinearDimension("3rem")
              }
              if (state.infos == null) {
                updInfos()
                +"Loading..."
              } else {
                mTableContainer {
                  mTable {
                    mTableHead {
                      mTableRow {
                        mTableCell { +"Id" }
                        mTableCell { +"Name" }
                        mTableCell { +"Expire date" }
                      }
                    }
                    mTableBody {
                      for (info in state.infos!!) {
                        mTableRow {
                          mTableCell { +info.id }
                          mTableCell { +(info.userName.firstName + " " + info.userName.lastName) }
                          mTableCell { +info.expireDate.toString().take(19).replace("T", " ") }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}