package itmo.mervap.hw6.token

class AddOperator : Operator() {
  override fun apply(left: Long, right: Long) = left + right
}