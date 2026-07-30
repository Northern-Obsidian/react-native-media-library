package com.obsidian_north.mediastore.models

class FolderRecord {
  var id: String = ""
  var name: String = ""
  var path: String = ""
  var fileCount: Int = 0
  var totalSize: Long = 0L

  fun toMap(): Map<String, Any?> = mapOf(
    "id" to id, "name" to name, "path" to path,
    "fileCount" to fileCount, "totalSize" to totalSize
  )
}

class SizeHistogramRecord {
  var lessThan1MB: Int = 0
  var from1to10MB: Int = 0
  var from10to100MB: Int = 0
  var from100MBto1GB: Int = 0
  var greaterThan1GB: Int = 0

  fun toMap(): Map<String, Any?> = mapOf(
    "lessThan1MB" to lessThan1MB, "from1to10MB" to from1to10MB,
    "from10to100MB" to from10to100MB, "from100MBto1GB" to from100MBto1GB,
    "greaterThan1GB" to greaterThan1GB
  )
}

class MediaTypeBreakdownRecord {
  var audio: Int = 0
  var video: Int = 0
  var image: Int = 0
  var document: Int = 0

  fun toMap(): Map<String, Any?> = mapOf(
    "audio" to audio, "video" to video, "image" to image, "document" to document
  )
}

class FolderStatisticsRecord {
  var id: String = ""
  var name: String = ""
  var path: String = ""
  var fileCount: Int = 0
  var totalSize: Long = 0L
  var histogram: SizeHistogramRecord = SizeHistogramRecord()
  var mediaTypeBreakdown: MediaTypeBreakdownRecord = MediaTypeBreakdownRecord()
  var averageFileSize: Double = 0.0

  fun toMap(): Map<String, Any?> = mapOf(
    "id" to id, "name" to name, "path" to path,
    "fileCount" to fileCount, "totalSize" to totalSize,
    "histogram" to histogram.toMap(),
    "mediaTypeBreakdown" to mediaTypeBreakdown.toMap(),
    "averageFileSize" to averageFileSize
  )
}
