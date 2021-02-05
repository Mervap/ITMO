import java.io.File
import java.util.*

private val all =
  listOf(
    "StudentId", "StudentName", "GroupId", "GroupName", "CourseId",
    "CourseName", "LecturerId", "LecturerName", "Mark"
  )

data class Rule(val left: List<String>, val right: List<String>) {
  override fun toString(): String {
    return left.joinToString(", ") + " -> " +  right.joinToString(", ")
  }
}

object Irreducible {
  fun step1(rules: List<Rule>): List<Rule> {
    val res = mutableListOf<Rule>()
    for (el in rules) {
      for (r in el.right) {
        res.add(Rule(el.left, listOf(r)))
      }
    }

    return res
  }

  fun step2(rules: List<Rule>): List<Rule> {
    val res = mutableListOf<Rule>()
    for (rule in rules) {
      val queue = ArrayDeque(listOf(rule.left))
      val was = mutableSetOf(rule.left)
      while(queue.isNotEmpty()) {
        val cur = queue.peek()
        queue.pop()
        var flag = false
        for (el in cur) {
          val b = cur - el
          val a = closure(rules, b)
          if (a.containsAll(rule.right)) {
            flag = true
            if (b in was) continue
            queue.add(b)
            was.add(b)
          }
        }
        if (!flag) res.add(Rule(cur, rule.right))
      }
    }
    return res
  }

  fun step3(rules: List<Rule>): List<Rule> {
    val res = mutableListOf<Rule>()
    for (rule in rules) {
      val a = closure(rules - rule, rule.left)
      if (!a.containsAll(rule.right)) {
        res.add(rule)
      }
    }
    return res
  }
}


fun search(action: (List<String>) -> Unit) {

  fun dfs(cur: List<String>, c: Int, fin: Int) {
    if (cur.size == fin) {
      action(cur)
      return
    }

    for (i in c until all.size) {
      val el = all[i]
      if (el in cur) continue
      dfs(cur + el, i + 1, fin)
    }
  }

  for (i in 1..all.size) {
    dfs(emptyList(), 0, i)
  }
}

fun closure(rules: List<Rule>, list: List<String>, withStep: Boolean = false): Set<String> {
  val a = list.toMutableSet()
  while (true) {
    val oldSize = a.size
    rules.forEach { rule ->
      for (el in rule.left) {
        if (el !in a) return@forEach
      }
      val olddSize = a.size
      a.addAll(rule.right)
      if (a.size > olddSize && withStep) {
        println(a)
      }
    }
    if (a.size == oldSize) break
  }
  return a
}

fun main() = with(Scanner(File("input"))) {
  val cnt = nextLine().toInt()
  val rules = mutableListOf<Rule>()
  for (i in 0 until cnt) {
    val rule = nextLine()
    val left: String
    val right: String
    rule.split("->").also {
      left = it[0]
      right = it[1]
    }
    rules.add(Rule(left.split(",").map { it.trim() }, right.split(",").map { it.trim() }))
  }

  println("Super keys:")
  search {
    val a = closure(rules, it)
    if (a.size == all.size) {
      println(it)
    }
  }
  println()
  println("Keys:")
  val queue = ArrayDeque(listOf(all))
  val was = mutableSetOf(all)
  while(queue.isNotEmpty()) {
    val cur = queue.peek()
    queue.pop()
    var flag = false
    for (el in cur) {
      val b = cur - el
      val a = closure(rules, b)
      if (a.size == all.size) {
        flag = true
        if (b in was) continue
        queue.add(b)
        was.add(b)
      }
    }
    if (!flag) {
      println(cur)
    }
  }
  println()

  println("Closures:")
  for (list in listOf(listOf("GroupId", "CourseId"), listOf("StudentId", "CourseId"), listOf("StudentId", "LecturerId"))) {
    print(list.joinToString(", "))
    println(": ")
    println(list)
    closure(rules, list, true)
  }
  println()

  println("Step1:")
  val rules1 = Irreducible.step1(rules).also { it.forEach { println(it) } }
  println()

  println("Step2:")
  val rules2 = Irreducible.step2(rules1).also { it.forEach { println(it) } }
  println()

  println("Step3:")
  Irreducible.step3(rules2).also { it.forEach { println(it) } }
  println()
}