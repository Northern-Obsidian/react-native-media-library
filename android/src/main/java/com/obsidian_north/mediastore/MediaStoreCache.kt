package com.obsidian_north.mediastore

import java.util.LinkedHashMap

class MediaStoreCache(private val maxSize: Int = 100) {
  private val cache = LinkedHashMap<String, CacheEntry>(maxSize, 0.75f, true)
  private var lastRefreshTimestamp: Long = System.currentTimeMillis()
  private val removedSinceRefresh = mutableListOf<String>()

  data class CacheEntry(val data: Any, val timestamp: Long, val ttl: Long = 30_000L) {
    fun isExpired(): Boolean = (System.currentTimeMillis() - timestamp) > ttl
  }

  @Synchronized
  fun get(key: String): Any? {
    val entry = cache[key]
    return if (entry != null && !entry.isExpired()) { entry.data }
    else { cache.remove(key); null }
  }

  @Synchronized
  fun put(key: String, data: Any, ttl: Long = 30_000L) {
    if (cache.size >= maxSize) { cache.entries.firstOrNull()?.let { cache.remove(it.key) } }
    cache[key] = CacheEntry(data, System.currentTimeMillis(), ttl)
  }

  @Synchronized
  fun remove(key: String) { cache.remove(key) }

  @Synchronized
  fun invalidate() { cache.clear(); lastRefreshTimestamp = System.currentTimeMillis() }

  @Synchronized
  fun size(): Int = cache.size

  @Synchronized
  fun getLastRefreshTimestamp(): Long = lastRefreshTimestamp

  @Synchronized
  fun setLastRefreshTimestamp(timestamp: Long) { lastRefreshTimestamp = timestamp }

  @Synchronized
  fun trackRemoved(itemId: String) { removedSinceRefresh.add(itemId) }

  @Synchronized
  fun getAndClearRemovedItems(): List<String> {
    val items = removedSinceRefresh.toList()
    removedSinceRefresh.clear()
    return items
  }
}
