package expo.modules.mediastore

import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Files.FileColumns
import expo.modules.mediastore.models.*
import expo.modules.mediastore.utils.ArtworkUtils
import expo.modules.mediastore.utils.CursorUtils
import expo.modules.mediastore.utils.MimeUtils

class MediaStoreMapper {
  fun mapAudio(cursor: Cursor): List<AudioRecord> {
    val items = mutableListOf<AudioRecord>()
    while (cursor.moveToNext()) {
      items.add(mapAudioRow(cursor))
    }
    return items
  }

  fun mapAudioRow(cursor: Cursor): AudioRecord {
    val data = CursorUtils.getString(cursor, MediaStore.Audio.Media.DATA)
    return AudioRecord().apply {
      id = CursorUtils.getLong(cursor, MediaStore.Audio.Media._ID).toString()
      uri = data
      title = CursorUtils.getString(cursor, MediaStore.Audio.Media.TITLE)
      artist = CursorUtils.getString(cursor, MediaStore.Audio.Media.ARTIST)
      album = CursorUtils.getString(cursor, MediaStore.Audio.Media.ALBUM)
      albumId = CursorUtils.getLong(cursor, MediaStore.Audio.Media.ALBUM_ID).toString()
      genre = null
      duration = CursorUtils.getLong(cursor, MediaStore.Audio.Media.DURATION)
      size = CursorUtils.getLong(cursor, MediaStore.Audio.Media.SIZE)
      trackNumber = CursorUtils.getInt(cursor, MediaStore.Audio.Media.TRACK)
      discNumber = CursorUtils.getInt(cursor, MediaStore.Audio.Media.DISC_NUMBER)
      year = CursorUtils.getInt(cursor, MediaStore.Audio.Media.YEAR)
      dateAdded = CursorUtils.getLong(cursor, MediaStore.Audio.Media.DATE_ADDED) * 1000L
      dateModified = CursorUtils.getLong(cursor, MediaStore.Audio.Media.DATE_MODIFIED) * 1000L
      composer = CursorUtils.getStringOrNull(cursor, MediaStore.Audio.Media.COMPOSER)
      lyrics = null
      albumArtist = CursorUtils.getStringOrNull(cursor, MediaStore.Audio.Media.ALBUM_ARTIST)
      isFavorite = false
      playCount = 0
      lastPlayed = 0L
      bookmark = CursorUtils.getLong(cursor, MediaStore.Audio.Media.BOOKMARK)
      bitrate = CursorUtils.getIntOrNull(cursor, MediaStore.Audio.Media.BITRATE)
      sampleRate = CursorUtils.getIntOrNull(cursor, MediaStore.Audio.Media.SAMPLE_RATE)
      channels = CursorUtils.getIntOrNull(cursor, MediaStore.Audio.Media.CHANNEL_COUNT)
      encoding = null
      mimeType = CursorUtils.getString(cursor, MediaStore.Audio.Media.MIME_TYPE)
      fileExtension = data.substringAfterLast('.', "")
      relativePath = CursorUtils.getString(cursor, MediaStore.Audio.Media.RELATIVE_PATH)
      displayName = CursorUtils.getString(cursor, MediaStore.Audio.Media.DISPLAY_NAME)
      contentUri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id).toString()
    }
  }

  fun mapVideo(cursor: Cursor): List<VideoRecord> {
    val items = mutableListOf<VideoRecord>()
    while (cursor.moveToNext()) {
      items.add(mapVideoRow(cursor))
    }
    return items
  }

  fun mapVideoRow(cursor: Cursor): VideoRecord {
    val data = CursorUtils.getString(cursor, MediaStore.Video.Media.DATA)
    return VideoRecord().apply {
      id = CursorUtils.getLong(cursor, MediaStore.Video.Media._ID).toString()
      uri = data
      title = CursorUtils.getString(cursor, MediaStore.Video.Media.TITLE)
      duration = CursorUtils.getLong(cursor, MediaStore.Video.Media.DURATION)
      width = CursorUtils.getInt(cursor, MediaStore.Video.Media.WIDTH)
      height = CursorUtils.getInt(cursor, MediaStore.Video.Media.HEIGHT)
      frameRate = null
      rotation = 0
      size = CursorUtils.getLong(cursor, MediaStore.Video.Media.SIZE)
      mimeType = CursorUtils.getString(cursor, MediaStore.Video.Media.MIME_TYPE)
      relativePath = ""
      displayName = CursorUtils.getString(cursor, MediaStore.Video.Media.DISPLAY_NAME)
      dateAdded = CursorUtils.getLong(cursor, MediaStore.Video.Media.DATE_ADDED) * 1000L
      dateModified = CursorUtils.getLong(cursor, MediaStore.Video.Media.DATE_MODIFIED) * 1000L
      resolution = "${width}x${height}"
      orientation = CursorUtils.getInt(cursor, MediaStore.Video.Media.ORIENTATION)
    }
  }

  fun mapImages(cursor: Cursor): List<ImageRecord> {
    val items = mutableListOf<ImageRecord>()
    while (cursor.moveToNext()) {
      items.add(mapImageRow(cursor))
    }
    return items
  }

  fun mapImageRow(cursor: Cursor): ImageRecord {
    val data = CursorUtils.getString(cursor, MediaStore.Images.Media.DATA)
    return ImageRecord().apply {
      id = CursorUtils.getLong(cursor, MediaStore.Images.Media._ID).toString()
      uri = data
      title = CursorUtils.getString(cursor, MediaStore.Images.Media.TITLE)
      width = CursorUtils.getInt(cursor, MediaStore.Images.Media.WIDTH)
      height = CursorUtils.getInt(cursor, MediaStore.Images.Media.HEIGHT)
      orientation = CursorUtils.getInt(cursor, MediaStore.Images.Media.ORIENTATION)
      cameraMake = null
      cameraModel = null
      dateTaken = CursorUtils.getLong(cursor, MediaStore.Images.Media.DATE_TAKEN)
      gpsLatitude = null
      gpsLongitude = null
      mimeType = CursorUtils.getString(cursor, MediaStore.Images.Media.MIME_TYPE)
      size = CursorUtils.getLong(cursor, MediaStore.Images.Media.SIZE)
      relativePath = ""
      displayName = CursorUtils.getString(cursor, MediaStore.Images.Media.DISPLAY_NAME)
      dateAdded = CursorUtils.getLong(cursor, MediaStore.Images.Media.DATE_ADDED) * 1000L
      dateModified = CursorUtils.getLong(cursor, MediaStore.Images.Media.DATE_MODIFIED) * 1000L
    }
  }

  fun mapDocuments(cursor: Cursor): List<DocumentRecord> {
    val items = mutableListOf<DocumentRecord>()
    while (cursor.moveToNext()) {
      items.add(mapDocumentRow(cursor))
    }
    return items
  }

  fun mapDocumentRow(cursor: Cursor): DocumentRecord {
    val data = CursorUtils.getString(cursor, FileColumns.DATA)
    val mimeType = CursorUtils.getString(cursor, FileColumns.MIME_TYPE)
    return DocumentRecord().apply {
      id = CursorUtils.getLong(cursor, FileColumns._ID).toString()
      uri = data
      name = CursorUtils.getString(cursor, FileColumns.TITLE)
      size = CursorUtils.getLong(cursor, FileColumns.SIZE)
      this.mimeType = mimeType
      extension = MimeUtils.getExtensionFromMime(mimeType).ifEmpty { data.substringAfterLast('.', "") }
      relativePath = CursorUtils.getString(cursor, FileColumns.RELATIVE_PATH)
      dateAdded = CursorUtils.getLong(cursor, FileColumns.DATE_ADDED) * 1000L
      dateModified = CursorUtils.getLong(cursor, FileColumns.DATE_MODIFIED) * 1000L
    }
  }

  fun mapAlbums(cursor: Cursor): List<AlbumRecord> {
    val items = mutableListOf<AlbumRecord>()
    while (cursor.moveToNext()) {
      val albumId = CursorUtils.getLong(cursor, MediaStore.Audio.Albums._ID)
      items.add(AlbumRecord().apply {
        id = albumId.toString()
        title = CursorUtils.getString(cursor, MediaStore.Audio.Albums.ALBUM)
        artist = CursorUtils.getString(cursor, MediaStore.Audio.Albums.ARTIST)
        songCount = CursorUtils.getInt(cursor, MediaStore.Audio.Albums.NUMBER_OF_SONGS)
        duration = 0L
        artworkUri = ArtworkUtils.getAlbumArtworkUri(albumId).toString()
        dateAdded = 0L
        year = CursorUtils.getIntOrNull(cursor, MediaStore.Audio.Albums.FIRST_YEAR)
      })
    }
    return items
  }

  fun mapArtists(cursor: Cursor): List<ArtistRecord> {
    val items = mutableListOf<ArtistRecord>()
    while (cursor.moveToNext()) {
      items.add(ArtistRecord().apply {
        id = CursorUtils.getLong(cursor, MediaStore.Audio.Artists._ID).toString()
        name = CursorUtils.getString(cursor, MediaStore.Audio.Artists.ARTIST)
        albumCount = CursorUtils.getInt(cursor, MediaStore.Audio.Artists.NUMBER_OF_ALBUMS)
        songCount = CursorUtils.getInt(cursor, MediaStore.Audio.Artists.NUMBER_OF_TRACKS)
        duration = 0L
        dateAdded = 0L
      })
    }
    return items
  }

  fun mapGenres(cursor: Cursor): List<GenreRecord> {
    val items = mutableListOf<GenreRecord>()
    while (cursor.moveToNext()) {
      items.add(GenreRecord().apply {
        id = CursorUtils.getLong(cursor, MediaStore.Audio.Genres._ID).toString()
        name = CursorUtils.getString(cursor, MediaStore.Audio.Genres.NAME)
        songCount = 0
        duration = 0L
      })
    }
    return items
  }

  fun mapPlaylists(cursor: Cursor): List<PlaylistRecord> {
    val items = mutableListOf<PlaylistRecord>()
    while (cursor.moveToNext()) {
      items.add(PlaylistRecord().apply {
        id = CursorUtils.getLong(cursor, MediaStore.Audio.Playlists._ID).toString()
        name = CursorUtils.getString(cursor, MediaStore.Audio.Playlists.NAME)
        songCount = 0
        duration = 0L
        dateAdded = CursorUtils.getLong(cursor, MediaStore.Audio.Playlists.DATE_ADDED) * 1000L
        dateModified = CursorUtils.getLong(cursor, MediaStore.Audio.Playlists.DATE_MODIFIED) * 1000L
      })
    }
    return items
  }

  fun mapFolders(cursor: Cursor): List<FolderRecord> {
    val folderMap = mutableMapOf<String, FolderRecord>()
    while (cursor.moveToNext()) {
      val path = CursorUtils.getString(cursor, FileColumns.RELATIVE_PATH)
      if (path.isNotEmpty()) {
        val folder = folderMap.getOrPut(path) {
          FolderRecord().apply {
            id = path.hashCode().toString()
            name = path.trimEnd('/').substringAfterLast('/').ifEmpty { path.trimEnd('/') }
            this.path = path
            fileCount = 0
            totalSize = 0L
          }
        }
        folder.fileCount++
        folder.totalSize += CursorUtils.getLong(cursor, FileColumns.SIZE)
      }
    }
    return folderMap.values.toList()
  }

  fun mapSingle(cursor: Cursor, mediaType: String): Any? {
    if (!cursor.moveToFirst()) return null
    return when (mediaType) {
      "audio" -> mapAudioRow(cursor)
      "video" -> mapVideoRow(cursor)
      "image" -> mapImageRow(cursor)
      "document" -> mapDocumentRow(cursor)
      else -> null
    }
  }

  fun mapGeneric(cursor: Cursor): List<Map<String, Any?>> {
    val items = mutableListOf<Map<String, Any?>>()
    val columns = cursor.columnNames
    while (cursor.moveToNext()) {
      val row = mutableMapOf<String, Any?>()
      for (col in columns) {
        val index = cursor.getColumnIndex(col)
        if (index >= 0) {
          row[col] = when (cursor.getType(index)) {
            Cursor.FIELD_TYPE_INTEGER -> cursor.getLong(index)
            Cursor.FIELD_TYPE_FLOAT -> cursor.getDouble(index)
            Cursor.FIELD_TYPE_STRING -> cursor.getString(index)
            Cursor.FIELD_TYPE_BLOB -> null
            Cursor.FIELD_TYPE_NULL -> null
            else -> null
          }
        }
      }
      items.add(row)
    }
    return items
  }

  fun detectMediaType(uri: String): String {
    return when {
      uri.contains("audio") -> "audio"
      uri.contains("video") -> "video"
      uri.contains("images") -> "image"
      else -> "document"
    }
  }
}
