package expo.modules.mediastore

import java.util.LinkedHashMap

class MediaStoreCache(private val maxSize: Int = 100) {
  private val cache = LinkedHashMap<String, CacheEntry>(maxSize, 0.75f, true)

  data class CacheEntry(
    val data: Any,
    val timestamp: Long,
    val ttl: Long = 30_000L
  ) {
    fun isExpired(): Boolean = (System.currentTimeMillis() - timestamp) > ttl
  }

  @Synchronized
  fun get(key: String): Any? {
    val entry = cache[key]
    return if (entry != null && !entry.isExpired()) {
      entry.data
    } else {
      cache.remove(key)
      null
    }
  }

  @Synchronized
  fun put(key: String, data: Any, ttl: Long = 30_000L) {
    if (cache.size >= maxSize) {
      val oldest = cache.entries.firstOrNull()
      oldest?.let { cache.remove(it.key) }
    }
    cache[key] = CacheEntry(data, System.currentTimeMillis(), ttl)
  }

  @Synchronized
  fun remove(key: String) {
    cache.remove(key)
  }

  @Synchronized
  fun invalidate() {
    cache.clear()
  }

  @Synchronized
  fun size(): Int = cache.size
}
