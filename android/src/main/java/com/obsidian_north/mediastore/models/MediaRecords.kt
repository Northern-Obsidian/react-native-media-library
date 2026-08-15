package com.obsidian_north.mediastore.models

class AudioRecord {
  var id: String = ""
  var uri: String = ""
  var title: String = ""
  var artist: String = ""
  var album: String = ""
  var albumId: String = ""
  var genre: String? = null
  var duration: Long = 0L
  var size: Long = 0L
  var trackNumber: Int = 0
  var discNumber: Int = 0
  var year: Int = 0
  var dateAdded: Long = 0L
  var dateModified: Long = 0L
  var composer: String? = null
  var lyrics: String? = null
  var albumArtist: String? = null
  var isFavorite: Boolean = false
  var playCount: Int = 0
  var lastPlayed: Long = 0L
  var bookmark: Long = 0L
  var bitrate: Int? = null
  var sampleRate: Int? = null
  var channels: Int? = null
  var encoding: String? = null
  var writer: String? = null
  var isMusic: Boolean? = null
  var isPodcast: Boolean? = null
  var isRingtone: Boolean? = null
  var isAlarm: Boolean? = null
  var isNotification: Boolean? = null
  var cdTrackNumber: Int? = null
  var numTracks: Int? = null
  var mimeType: String = ""
  var fileExtension: String = ""
  var relativePath: String = ""
  var displayName: String = ""
  var contentUri: String = ""

  fun toMap(): Map<String, Any?> = mapOf(
    "id" to id, "uri" to uri, "title" to title,
    "artist" to artist, "album" to album, "albumId" to albumId,
    "genre" to genre, "duration" to duration, "size" to size,
    "trackNumber" to trackNumber, "discNumber" to discNumber,
    "year" to year, "dateAdded" to dateAdded, "dateModified" to dateModified,
    "composer" to composer, "lyrics" to lyrics, "albumArtist" to albumArtist,
    "isFavorite" to isFavorite, "playCount" to playCount, "lastPlayed" to lastPlayed,
    "bookmark" to bookmark, "bitrate" to bitrate, "sampleRate" to sampleRate,
    "channels" to channels, "encoding" to encoding, "writer" to writer,
    "isMusic" to isMusic, "isPodcast" to isPodcast, "isRingtone" to isRingtone,
    "isAlarm" to isAlarm, "isNotification" to isNotification,
    "cdTrackNumber" to cdTrackNumber, "numTracks" to numTracks,
    "mimeType" to mimeType,
    "fileExtension" to fileExtension, "relativePath" to relativePath,
    "displayName" to displayName, "contentUri" to contentUri
  )
}

class VideoRecord {
  var id: String = ""
  var uri: String = ""
  var title: String = ""
  var duration: Long = 0L
  var width: Int = 0
  var height: Int = 0
  var frameRate: Int? = null
  var rotation: Int = 0
  var size: Long = 0L
  var mimeType: String = ""
  var relativePath: String = ""
  var displayName: String = ""
  var dateAdded: Long = 0L
  var dateModified: Long = 0L
  var resolution: String = ""
  var orientation: Int = 0
  var colorStandard: String? = null
  var colorTransfer: String? = null
  var bucketId: String? = null
  var bucketDisplayName: String? = null

  fun toMap(): Map<String, Any?> = mapOf(
    "id" to id, "uri" to uri, "title" to title,
    "duration" to duration, "width" to width, "height" to height,
    "frameRate" to frameRate, "rotation" to rotation, "size" to size,
    "mimeType" to mimeType, "relativePath" to relativePath,
    "displayName" to displayName, "dateAdded" to dateAdded,
    "dateModified" to dateModified, "resolution" to resolution,
    "orientation" to orientation, "colorStandard" to colorStandard,
    "colorTransfer" to colorTransfer, "bucketId" to bucketId,
    "bucketDisplayName" to bucketDisplayName
  )
}

class ImageRecord {
  var id: String = ""
  var uri: String = ""
  var title: String = ""
  var width: Int = 0
  var height: Int = 0
  var orientation: Int = 0
  var cameraMake: String? = null
  var cameraModel: String? = null
  var dateTaken: Long = 0L
  var gpsLatitude: Double? = null
  var gpsLongitude: Double? = null
  var mimeType: String = ""
  var size: Long = 0L
  var relativePath: String = ""
  var displayName: String = ""
  var dateAdded: Long = 0L
  var dateModified: Long = 0L
  var bucketId: String? = null
  var bucketDisplayName: String? = null

  fun toMap(): Map<String, Any?> = mapOf(
    "id" to id, "uri" to uri, "title" to title,
    "width" to width, "height" to height, "orientation" to orientation,
    "cameraMake" to cameraMake, "cameraModel" to cameraModel,
    "dateTaken" to dateTaken, "gpsLatitude" to gpsLatitude,
    "gpsLongitude" to gpsLongitude, "mimeType" to mimeType, "size" to size,
    "relativePath" to relativePath, "displayName" to displayName,
    "dateAdded" to dateAdded, "dateModified" to dateModified,
    "bucketId" to bucketId, "bucketDisplayName" to bucketDisplayName
  )
}

class DocumentRecord {
  var id: String = ""
  var uri: String = ""
  var name: String = ""
  var size: Long = 0L
  var mimeType: String = ""
  var extension: String = ""
  var relativePath: String = ""
  var dateAdded: Long = 0L
  var dateModified: Long = 0L
  var title: String? = null

  fun toMap(): Map<String, Any?> = mapOf(
    "id" to id, "uri" to uri, "name" to name, "size" to size,
    "mimeType" to mimeType, "extension" to extension, "title" to title,
    "relativePath" to relativePath, "dateAdded" to dateAdded,
    "dateModified" to dateModified
  )
}
