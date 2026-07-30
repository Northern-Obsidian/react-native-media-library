package com.obsidian_north.mediastore.models

class AlbumRecord {
  var id: String = ""
  var title: String = ""
  var artist: String = ""
  var songCount: Int = 0
  var duration: Long = 0L
  var artworkUri: String? = null
  var dateAdded: Long = 0L
  var year: Int? = null

  fun toMap(): Map<String, Any?> = mapOf(
    "id" to id, "title" to title, "artist" to artist,
    "songCount" to songCount, "duration" to duration,
    "artworkUri" to artworkUri, "dateAdded" to dateAdded, "year" to year
  )
}

class ArtistRecord {
  var id: String = ""
  var name: String = ""
  var albumCount: Int = 0
  var songCount: Int = 0
  var duration: Long = 0L
  var dateAdded: Long = 0L

  fun toMap(): Map<String, Any?> = mapOf(
    "id" to id, "name" to name, "albumCount" to albumCount,
    "songCount" to songCount, "duration" to duration, "dateAdded" to dateAdded
  )
}

class GenreRecord {
  var id: String = ""
  var name: String = ""
  var songCount: Int = 0
  var duration: Long = 0L

  fun toMap(): Map<String, Any?> = mapOf(
    "id" to id, "name" to name, "songCount" to songCount, "duration" to duration
  )
}

class PlaylistRecord {
  var id: String = ""
  var name: String = ""
  var songCount: Int = 0
  var duration: Long = 0L
  var dateAdded: Long = 0L
  var dateModified: Long = 0L

  fun toMap(): Map<String, Any?> = mapOf(
    "id" to id, "name" to name, "songCount" to songCount,
    "duration" to duration, "dateAdded" to dateAdded, "dateModified" to dateModified
  )
}
