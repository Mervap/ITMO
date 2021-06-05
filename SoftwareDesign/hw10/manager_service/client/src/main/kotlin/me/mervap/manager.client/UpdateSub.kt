package me.mervap.manager.client

import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.mSelect
import com.ccfraser.muirwik.components.mTextField
import com.ccfraser.muirwik.components.menu.mMenuItem
import com.ccfraser.muirwik.components.targetInputValue
import com.ccfraser.muirwik.components.targetValue
import kotlinx.css.LinearDimension
import kotlinx.css.width
import kotlinx.datetime.LocalDateTime
import kotlinx.html.InputType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.mervap.Period
import me.mervap.UpdateSubscriptionEvent
import me.mervap.UserName
import me.mervap.manager.client.utils.post
import react.*
import react.dom.br
import react.router.dom.redirect
import styled.css
import styled.styledDiv
import kotlin.js.Date

internal data class UpdateSubProps(var isExtend: Boolean) : RProps

internal data class UpdateSubState(
  var id: String,
  var firstName: String,
  var lastName: String,
  var select: String,
  var date: String,
  var redirect: Boolean = false
) : RState

internal class UpdateSub : RComponent<UpdateSubProps, UpdateSubState>() {

  init {
    state = UpdateSubState("", "", "", Period.MONTH_3.name, Date().toISOString().dropLast(5))
  }

  override fun RBuilder.render() {
    if (state.redirect) {
      redirect(to = "/")
      return
    }
    styledDiv {
      if (props.isExtend) {
        mTextField("Id") {
          css {
            width = LinearDimension("20rem")
          }
          attrs {
            value = state.id
            onChange = { setState { id = it.targetInputValue.trim() } }
          }
        }
      }
      else {
        mTextField("First name") {
          css {
            width = LinearDimension("20rem")
          }
          attrs {
            value = state.firstName
            onChange = { setState { firstName = it.targetInputValue.trim() } }
          }
        }
        br {}
        mTextField("Last name") {
          css {
            width = LinearDimension("20rem")
          }
          attrs {
            value = state.lastName
            onChange = { setState { lastName = it.targetInputValue.trim() } }
          }
        }
      }
      br {}
      mSelect(state.select, onChange = { ev, _ -> setState { select = ev.targetValue as String } }) {
        Period.values().map { it.name }.forEach {
          mMenuItem(it.toLowerCase().capitalize(), value = it)
        }
      }
      br {}
      mTextField("", type = InputType.dateTimeLocal) {
        attrs {
          value = state.date
          onChange = { setState { date = it.targetValue as String } }
        }
      }
      br {}
      val path = if (props.isExtend) "extend" else "buy"
      mButton(path.capitalize(), onClick = {
        console.log(state)
        val info = UpdateSubscriptionEvent(
          state.id,
          UserName(state.firstName, state.lastName),
          props.isExtend,
          Period.valueOf(state.select),
          LocalDateTime.parse(state.date)
        )
        post("/$path", Json.encodeToString(info)) {
          onload = { setState { redirect = true} }
        }
      })
    }
  }
}
