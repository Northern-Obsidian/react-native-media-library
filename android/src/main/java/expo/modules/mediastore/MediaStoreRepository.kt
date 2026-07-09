package expo.modules.mediastore

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import expo.modules.mediastore.models.*
import expo.modules.mediastore.utils.MimeUtils

class MediaStoreRepository(private val context: Context) {
  private val contentResolver: ContentResolver
    get() = context.contentResolver

  fun queryAudio(
    projection: Array<String>?,
    selection: String?,
    selectionArgs: Array<String>?,
    sortOrder: String?
  ): Cursor? {
    return contentResolver.query(
      MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
      projection,
      selection,
      selectionArgs,
      sortOrder
    )
  }

  fun queryVideo(
    projection: Array<String>?,
    selection: String?,
    selectionArgs: Array<String>?,
    sortOrder: String?
  ): Cursor? {
    return contentResolver.query(
      MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
      projection,
      selection,
      selectionArgs,
      sortOrder
    )
  }

  fun queryImages(
    projection: Array<String>?,
    selection: String?,
    selectionArgs: Array<String>?,
    sortOrder: String?
  ): Cursor? {
    return contentResolver.query(
      MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
      projection,
      selection,
      selectionArgs,
      sortOrder
    )
  }

  fun queryDocuments(
    projection: Array<String>?,
    selection: String?,
    selectionArgs: Array<String>?,
    sortOrder: String?
  ): Cursor? {
    return contentResolver.query(
      MediaStore.Files.getContentUri("external"),
      projection,
      selection,
      selectionArgs,
      sortOrder
    )
  }

  fun queryAlbums(
    projection: Array<String>?,
    selection: String?,
    selectionArgs: Array<String>?,
    sortOrder: String?
  ): Cursor? {
    return contentResolver.query(
      MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
      projection,
      selection,
      selectionArgs,
      sortOrder
    )
  }

  fun queryArtists(
    projection: Array<String>?,
    selection: String?,
    selectionArgs: Array<String>?,
    sortOrder: String?
  ): Cursor? {
    return contentResolver.query(
      MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
      projection,
      selection,
      selectionArgs,
      sortOrder
    )
  }

  fun queryGenres(): Cursor? {
    return contentResolver.query(
      MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,
      null,
      null,
      null,
      null
    )
  }

  fun queryPlaylists(): Cursor? {
    return contentResolver.query(
      MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
      null,
      null,
      null,
      null
    )
  }

  fun queryFolders(
    selection: String?,
    sortOrder: String?
  ): Cursor? {
    val projection = arrayOf(
      MediaStore.Files.FileColumns._ID,
      MediaStore.Files.FileColumns.PARENT,
      MediaStore.Files.FileColumns.MIME_TYPE,
      MediaStore.Files.FileColumns.TITLE,
      MediaStore.Files.FileColumns.SIZE,
      MediaStore.Files.FileColumns.DATA,
      MediaStore.Files.FileColumns.RELATIVE_PATH
    )
    return contentResolver.query(
      MediaStore.Files.getContentUri("external"),
      projection,
      selection,
      null,
      sortOrder
    )
  }

  fun queryByUri(uriString: String): Cursor? {
    val uri = Uri.parse(uriString)
    return contentResolver.query(uri, null, null, null, null)
  }

  fun search(options: SearchOptionsRecord): SearchResultRecord {
    val query = options.query.trim()
    val searchFilter = "${MediaStore.MediaColumns.TITLE} LIKE ? OR ${MediaStore.MediaColumns.DISPLAY_NAME} LIKE ?"
    val searchArgs = arrayOf("%$query%", "%$query%")
    val types = options.types ?: listOf("audio", "video", "image", "document")

    val audioItems = if (types.contains("audio")) {
      queryAudio(
        arrayOf(MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA),
        searchFilter,
        searchArgs,
        null
      )?.use { cursor ->
        val items = mutableListOf<AudioRecord>()
        while (cursor.moveToNext()) {
          items.add(MediaStoreMapper().mapAudioRow(cursor))
        }
        items
      } ?: emptyList()
    } else emptyList()

    val videoItems = if (types.contains("video")) {
      queryVideo(
        arrayOf(MediaStore.Video.Media._ID, MediaStore.Video.Media.TITLE, MediaStore.Video.Media.DURATION, MediaStore.Video.Media.WIDTH, MediaStore.Video.Media.HEIGHT, MediaStore.Video.Media.DATA),
        searchFilter,
        searchArgs,
        null
      )?.use { cursor ->
        val items = mutableListOf<VideoRecord>()
        while (cursor.moveToNext()) {
          items.add(MediaStoreMapper().mapVideoRow(cursor))
        }
        items
      } ?: emptyList()
    } else emptyList()

    val imageItems = if (types.contains("image")) {
      queryImages(
        arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.TITLE, MediaStore.Images.Media.WIDTH, MediaStore.Images.Media.HEIGHT, MediaStore.Images.Media.DATA),
        searchFilter,
        searchArgs,
        null
      )?.use { cursor ->
        val items = mutableListOf<ImageRecord>()
        while (cursor.moveToNext()) {
          items.add(MediaStoreMapper().mapImageRow(cursor))
        }
        items
      } ?: emptyList()
    } else emptyList()

    val documentItems = if (types.contains("document")) {
      val docMimeFilter = MimeUtils.getDocumentMimeFilter()
      queryDocuments(
        arrayOf(MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.TITLE, MediaStore.Files.FileColumns.SIZE, MediaStore.Files.FileColumns.MIME_TYPE, MediaStore.Files.FileColumns.DATA),
        "($searchFilter) AND (${MediaStore.Files.FileColumns.MIME_TYPE} IN (${docMimeFilter}))",
        searchArgs,
        null
      )?.use { cursor ->
        val items = mutableListOf<DocumentRecord>()
        while (cursor.moveToNext()) {
          items.add(MediaStoreMapper().mapDocumentRow(cursor))
        }
        items
      } ?: emptyList()
    } else emptyList()

    val totalCount = audioItems.size + videoItems.size + imageItems.size + documentItems.size

    return SearchResultRecord().apply {
      this.audio = audioItems
      this.videos = videoItems
      this.images = imageItems
      this.documents = documentItems
      this.totalCount = totalCount
      this.query = query
    }
  }

  fun getStatistics(): StatisticsRecord {
    var totalAudio = 0
    var totalVideo = 0
    var totalImages = 0
    var totalDocuments = 0
    var totalSize = 0L
    var totalDuration = 0L

    queryAudio(arrayOf("COUNT(*)", "SUM(${MediaStore.Audio.Media.SIZE})", "SUM(${MediaStore.Audio.Media.DURATION})"), null, null, null)?.use { cursor ->
      if (cursor.moveToFirst()) {
        totalAudio = cursor.getInt(0)
        totalSize += cursor.getLong(1)
        totalDuration += cursor.getLong(2)
      }
    }

    queryVideo(arrayOf("COUNT(*)", "SUM(${MediaStore.Video.Media.SIZE})", "SUM(${MediaStore.Video.Media.DURATION})"), null, null, null)?.use { cursor ->
      if (cursor.moveToFirst()) {
        totalVideo = cursor.getInt(0)
        totalSize += cursor.getLong(1)
        totalDuration += cursor.getLong(2)
      }
    }

    queryImages(arrayOf("COUNT(*)", "SUM(${MediaStore.Images.Media.SIZE})"), null, null, null)?.use { cursor ->
      if (cursor.moveToFirst()) {
        totalImages = cursor.getInt(0)
        totalSize += cursor.getLong(1)
      }
    }

    return StatisticsRecord().apply {
      this.totalAudio = totalAudio
      this.totalVideo = totalVideo
      this.totalImages = totalImages
      this.totalDocuments = totalDocuments
      this.totalSize = totalSize
      this.totalDuration = totalDuration
    }
  }
}
