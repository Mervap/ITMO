data class SqlResponse(val header: List<String>, val rows: List<List<String>>) {
  fun toBoolean(): Boolean = rows.first().first().toBoolean()
  fun toIntList(): List<Int> = rows.map { it.first().toInt() }
}
data class UserInfo(val login: String, val password: String)
data class UserResponse(val userInfo: UserInfo?, val isBook: Boolean, val seatNo: Int)

enum class IsolationLevel {
  READ_UNCOMMITTED,
  READ_COMMITTED,
  REPEATABLE_READ,
  SNAPSHOT,
  SERIALIZABLE;

  override fun toString(): String {
    return name.toLowerCase().replace("_", " ")
  }
}

abstract class SeatManager {
  // Отправляет сообщению пользователю без ожидания ответа
  protected abstract fun sendUserMessage(message: String)

  // Показывает пользователю список свободных мест, спрашивает какое место он хочет купить/забронировать
  // и возвращает ответ. По возможности возвращает пользовательский логин/пароль (да, пороль в явном виде не круто,
  // но тут предлагаю не обращать внимания)
  protected abstract suspend fun askUserAboutSeat(availableSeats: List<Int>): UserResponse

  // Запускает уже обернутый в транзакцию запрос в какой-то базе данных
  // Возвращает ответ в виде полученных строк и заголовка
  protected abstract suspend fun runTransactionSql(sql: String): SqlResponse

  // Оборачивает запрос в нужный уровень изоляции и запускает в бд
  protected suspend fun runSql(sql: String, isolationLevel: IsolationLevel): SqlResponse {
    val transactionSql = """
    start transaction read only isolation level $isolationLevel;
    $sql
    commit;
  """.trimIndent()
    return runTransactionSql(transactionSql)
  }

  // Основная функция. Возвращает удалась ли бронь/покупка
  suspend fun bookOrBuySeat(flightId: Int): Boolean {
    // Получаем список свободных мест
    val seats = runSql("select FreeSeats($flightId);", IsolationLevel.READ_COMMITTED).toIntList()

    // Спрашиваем пользователя что он хочет
    val (userInfo, isBook, seatNo) = askUserAboutSeat(seats)

    val isOk: Boolean
    if (isBook) {
      // Если пользователь хочет забронировать место, но не вошел в аккаунт, отправляем сообщение об ошибке
      if (userInfo == null) {
        sendUserMessage("Only logged users can book seats")
        return false
      }
      // Пытаемся забронировать
      isOk = runSql(
        "select Reserve(${userInfo.login}, ${userInfo.password}, $flightId, $seatNo);",
        IsolationLevel.SERIALIZABLE
      ).toBoolean()
    }
    else {
      // Пытаемся купить
      isOk = runSql(
        "select BuyFree($flightId, $seatNo);",
        IsolationLevel.SERIALIZABLE
      ).toBoolean()
    }

    if (isOk) {
      // Все получилось, сообщаем об успехе
      val suffix = if (isBook) "booked" else "bought"
      sendUserMessage("Seat $seatNo successfully $suffix!")
    }
    else {
      // Забронировать/купить не получилось. Сообщаем об ошибке. Правильно было бы разграничить
      // ошибку неправильного пароля и недоступности места, но думаю в данном задании это не важно
      val suffix = if (isBook) " or login/password is wrong." else ""
      sendUserMessage("Seat $seatNo already occupied$suffix. Please try again")
    }
    return isOk
  }
}