package itmo.mervap.hw6.token

class MulOperator : Operator() {
  override fun apply(left: Long, right: Long) = left * right
}