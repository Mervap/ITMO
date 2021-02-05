package me.mervap.hw7.profiler

import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference

private data class InvocationInfo(val invocationCnt: Int = 0, val summaryExecutionTime: Long = 0)

object ProfilerStatCollector {
  private val invocationInfos = ConcurrentHashMap<Method, InvocationInfo>()
  private val packages = mutableListOf<String>()
  private val isProf = AtomicReference(false)

  fun startProf(vararg packagesToProf: String) {
    if (isProf.compareAndSet(false, true)) {
      packages.clear()
      packages.addAll(packagesToProf)
      invocationInfos.clear()
    }
    else {
      throw RuntimeException("Prof is already started!")
    }
  }

  fun isNeedToProf(method: Method): Boolean {
    return packages.any { method.declaringClass.packageName.startsWith(it) }
  }

  fun stopProf() {
    if (!isProf.compareAndSet(true, false)) {
      throw RuntimeException("Prof is not started!")
    }
  }

  fun addInvocationInfo(method: Method, executionTime: Long) {
    if (!isProf.get()) return

    invocationInfos.putIfAbsent(method, InvocationInfo())
    invocationInfos.compute(method) { _, invocationInfo ->
      if (invocationInfo == null) InvocationInfo(1, executionTime)
      else InvocationInfo(invocationInfo.invocationCnt + 1, invocationInfo.summaryExecutionTime + executionTime)
    }
  }

  private fun StringBuilder.appendIndent(depth: Int): StringBuilder {
    for (i in 1..depth + 1) {
      append("|  ")
    }
    return this
  }


  fun getProfResults(): String = buildString {
    val sortedInfos = invocationInfos.toList().sortedBy { it.first.declaringClass.packageName }
    var depth = 0
    var commonPrefix = packages.first()
    for (packageName in packages) {
      commonPrefix = commonPrefix.commonPrefixWith(packageName)
    }
    commonPrefix = commonPrefix.dropLastWhile { it == '.' }
    val stack = Stack<String>()
    stack.add(commonPrefix)
    val packages = mutableSetOf(commonPrefix)
    append(commonPrefix).append("\n")
    for ((method, info) in sortedInfos) {
      val packageName = method.declaringClass.packageName
      while (stack.isNotEmpty() && (!packageName.startsWith(stack.peek()) || packageName == stack.peek())) {
        stack.pop()
        --depth
      }
      if (!packages.contains(packageName)) {
        packages.add(packageName)
        appendIndent(depth).append("\n")
        val presentablePackageName =
          if (stack.isEmpty()) packageName
          else packageName.substring(stack.peek().length + 1)
        appendIndent(depth - 1).append("|> $presentablePackageName\n")
      }
      stack.push(packageName)
      ++depth
      appendIndent(depth + 1).append("\n")
      appendIndent(depth).append("|> ${method.name}: ")
      val avgTime = info.summaryExecutionTime / info.invocationCnt
      append("inv: ${info.invocationCnt}, avg time: ")
      appendTime(avgTime).append(", total time: ")
      appendTime(info.summaryExecutionTime).append("\n")
    }
  }

  private fun StringBuilder.appendTime(nanos: Long): StringBuilder {
    val millis = nanos / NANOS_IN_MS
    if (millis < 3) {
      append(nanos / NANOS_IN_MUS).append("μs")
    }
    else {
      append(millis).append("ms")
    }
    return this
  }

  private const val NANOS_IN_MS = 1000000
  private const val NANOS_IN_MUS = 1000
}