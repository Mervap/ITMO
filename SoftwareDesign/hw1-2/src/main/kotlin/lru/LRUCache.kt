package lru

class LRUCache<K, V>(private val capacity: Int) {

  private var listHead: Node<K, V>? = null
  private var listTail: Node<K, V>? = null
  private var size = 0
  private val map = mutableMapOf<K, Node<K, V>>()

  init {
    assert(capacity > 0) { "Capacity must be greater than 0. Actual: $capacity" }
  }

  fun size(): Int {
    return size
  }

  fun get(key: K): V? {
    val listValue = findNodeInList(key)
    val mapValue = map[key]?.value
    checkLRUConsistency()
    assert(listValue == mapValue) {
      "The value found in linked list differs from the value in the map." +
          "List value: $listValue\n" +
          "Map value: $mapValue\n"
    }
    return mapValue
  }

  fun put(key: K, value: V) {
    remove(key)
    if (size == capacity) {
      val oldTail = listTail
      map.remove(listTail?.key)
      listTail = oldTail
      val newHead = Node(null, listHead, key, value)
      listHead?.previous = newHead
      listHead = newHead
      listTail =
        if (size == 1) listHead
        else oldTail?.previous
      listTail?.next = null
      map[key] = newHead
    }
    else {
      val newNode = Node(null, listHead, key, value)
      listHead?.previous = newNode
      listHead = newNode
      if (size == 0) listTail = newNode
      map[key] = newNode
      ++size
    }
    checkLRUConsistency()
    assert(listHead?.key == key && listHead?.value == value) {
      "Key: ${listHead?.key}/$key\nValue: ${listHead?.value}/$value"
    }
  }

  fun remove(key: K): Boolean {
    val keyNode = map[key] ?: return false
    keyNode.previous?.next = keyNode.next
    keyNode.next?.previous = keyNode.previous
    map.remove(key)
    if (keyNode == listHead) listHead = keyNode.next
    if (keyNode == listTail) listTail = keyNode.previous
    --size
    checkLRUConsistency()
    return true
  }

  private fun checkLRUConsistency() {
    assert(listHead?.previous == null)
    assert(listTail?.next == null)
    assert(size <= capacity) {
      "Size should be not greater than capacity:\n" +
          "Size: $size\n" +
          "Capacity: $capacity"
    }
    val listSize = getListSize()
    assert(getListSize() == size) {
      "List size is not equal to the expected size:" +
          "List: $listSize" +
          "Expected: $size"
    }
    val mapSize = map.size
    assert(mapSize == size) {
      "Map size is not equal to the expected size:\n" +
          "List: $mapSize\n" +
          "Expected: $size"
    }
    val listEntities = getLinkedListEntries()
    val mapEntities = map.entries.map { it.key to it.value.value }
    assert(mapEntities.containsAll(listEntities)) { "List contains more elements than map" }
    assert(listEntities.containsAll(mapEntities)) { "Map contains more elements than list" }
  }

  private fun findNodeInList(key: K): V? {
    var value: V? = null
    processAllNodes(object : NodeProcessor<K, V> {
      override fun process(node: Node<K, V>): Boolean {
        if (node.key == key) {
          value = node.value
          return false
        }
        return true
      }
    })
    return value
  }

  private fun getListSize(): Int {
    var actualSize = 0
    processAllNodes(object : NodeProcessor<K, V> {
      override fun process(node: Node<K, V>): Boolean {
        ++actualSize
        return true
      }
    })
    return actualSize
  }

  private fun getLinkedListEntries(): List<Pair<K, V>> {
    val entryList = mutableListOf<Pair<K, V>>()
    processAllNodes(object : NodeProcessor<K, V> {
      override fun process(node: Node<K, V>): Boolean {
        entryList.add(node.key to node.value)
        return true
      }
    })
    return entryList
  }

  private fun processAllNodes(nodeProcessor: NodeProcessor<K, V>) {
    var curNode = listHead
    while (curNode != null) {
      val isContinue = nodeProcessor.process(curNode)
      if (!isContinue) break
      curNode = curNode.next
    }
  }

  private interface NodeProcessor<K, V> {
    fun process(node: Node<K, V>): Boolean
  }

  private class Node<K, T>(var previous: Node<K, T>?, var next: Node<K, T>?, val key: K, val value: T)

}