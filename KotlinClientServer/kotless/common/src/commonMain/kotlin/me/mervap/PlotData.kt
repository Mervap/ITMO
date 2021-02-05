package me.mervap

import kotlinx.serialization.Serializable

@Serializable
enum class SearchInterval {
  LAST_HOUR {
    override val hours = 1L
    override val presentableString = "last hour"
  },
  DAY {
    override val hours = 24L
    override val presentableString = "last day"
  },
  WEEK {
    override val hours = 24L * 7L
    override val presentableString = "last week"
  },
  MOUTH {
    override val hours = 24L * 31L
    override val presentableString = "last mouth"
  },
  YEAR {
    override val hours = 24L * 356L
    override val presentableString = "last year"
  };

  abstract val hours: Long
  abstract val presentableString: String
}

val SearchInterval.next
  get() = when (this) {
    SearchInterval.LAST_HOUR -> SearchInterval.DAY
    SearchInterval.DAY -> SearchInterval.WEEK
    SearchInterval.WEEK -> SearchInterval.MOUTH
    SearchInterval.MOUTH -> SearchInterval.YEAR
    SearchInterval.YEAR -> SearchInterval.LAST_HOUR
  }

@Serializable
data class PlotData(
  val x: List<String>,
  val y: List<Int>,
  val isOverflow: Boolean,
  val hashtag: String,
  val searchInterval: SearchInterval
)

@Serializable
data class SavedPlot(val plotData: PlotData, val saveTime: String)