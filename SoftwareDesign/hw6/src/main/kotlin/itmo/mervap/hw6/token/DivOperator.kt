package itmo.mervap.hw6.token

class DivOperator : Operator() {
  override fun apply(left: Long, right: Long) = left / right
}