package com.obsidian_north.mediastore.models

class SearchResultRecord {
  var audio: List<AudioRecord> = emptyList()
  var videos: List<VideoRecord> = emptyList()
  var images: List<ImageRecord> = emptyList()
  var documents: List<DocumentRecord> = emptyList()
  var totalCount: Int = 0
  var query: String = ""

  fun toMap(): Map<String, Any?> = mapOf(
    "audio" to audio.map { it.toMap() }, "videos" to videos.map { it.toMap() },
    "images" to images.map { it.toMap() }, "documents" to documents.map { it.toMap() },
    "totalCount" to totalCount, "query" to query
  )
}

class StatisticsRecord {
  var totalAudio: Int = 0
  var totalVideo: Int = 0
  var totalImages: Int = 0
  var totalDocuments: Int = 0
  var totalSize: Long = 0L
  var totalDuration: Long = 0L

  fun toMap(): Map<String, Any?> = mapOf(
    "totalAudio" to totalAudio, "totalVideo" to totalVideo,
    "totalImages" to totalImages, "totalDocuments" to totalDocuments,
    "totalSize" to totalSize, "totalDuration" to totalDuration
  )
}

class DuplicateRecord {
  var fileHash: String = ""
  var count: Int = 0
  var totalSize: Long = 0L

  fun toMap(): Map<String, Any?> = mapOf(
    "fileHash" to fileHash, "count" to count, "totalSize" to totalSize
  )
}

class IncrementalChangesRecord {
  var added: Int = 0
  var modified: Int = 0
  var removed: Int = 0
  var timestamp: Long = 0L

  fun toMap(): Map<String, Any?> = mapOf(
    "added" to added, "modified" to modified,
    "removed" to removed, "timestamp" to timestamp
  )
}
