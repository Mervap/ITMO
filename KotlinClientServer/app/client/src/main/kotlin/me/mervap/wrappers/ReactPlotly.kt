@file:JsModule("react-plotly.js")
@file:JsNonModule

package me.mervap.wrappers

import react.*

@JsName("default")
external class Plot : Component<RProps, RState> {
  override fun render(): ReactElement?
}