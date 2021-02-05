package itmo.mervap.hw6.token

class SubOperator : Operator() {
  override fun apply(left: Long, right: Long) = left - right
}