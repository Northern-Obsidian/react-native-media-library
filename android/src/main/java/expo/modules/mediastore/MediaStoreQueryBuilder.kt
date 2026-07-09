package expo.modules.mediastore

import android.provider.MediaStore
import expo.modules.mediastore.models.FilterOptionsRecord
import expo.modules.mediastore.models.SortOptionsRecord
import expo.modules.mediastore.utils.MimeUtils

class MediaStoreQueryBuilder {
  fun buildAudioProjection(): Array<String> {
    return arrayOf(
      MediaStore.Audio.Media._ID,
      MediaStore.Audio.Media.TITLE,
      MediaStore.Audio.Media.ARTIST,
      MediaStore.Audio.Media.ALBUM,
      MediaStore.Audio.Media.ALBUM_ID,
      MediaStore.Audio.Media.DURATION,
      MediaStore.Audio.Media.SIZE,
      MediaStore.Audio.Media.TRACK,
      MediaStore.Audio.Media.DISC_NUMBER,
      MediaStore.Audio.Media.YEAR,
      MediaStore.Audio.Media.DATE_ADDED,
      MediaStore.Audio.Media.DATE_MODIFIED,
      MediaStore.Audio.Media.BITRATE,
      MediaStore.Audio.Media.MIME_TYPE,
      MediaStore.Audio.Media.DATA,
      MediaStore.Audio.Media.DISPLAY_NAME,
      MediaStore.Audio.Media.RELATIVE_PATH
    )
  }

  fun buildVideoProjection(): Array<String> {
    return arrayOf(
      MediaStore.Video.Media._ID,
      MediaStore.Video.Media.TITLE,
      MediaStore.Video.Media.DURATION,
      MediaStore.Video.Media.WIDTH,
      MediaStore.Video.Media.HEIGHT,
      MediaStore.Video.Media.SIZE,
      MediaStore.Video.Media.MIME_TYPE,
      MediaStore.Video.Media.DATA,
      MediaStore.Video.Media.DISPLAY_NAME,
      MediaStore.Video.Media.DATE_ADDED,
      MediaStore.Video.Media.DATE_MODIFIED,
      MediaStore.Video.Media.RESOLUTION,
      MediaStore.Video.Media.ORIENTATION
    )
  }

  fun buildImageProjection(): Array<String> {
    return arrayOf(
      MediaStore.Images.Media._ID,
      MediaStore.Images.Media.TITLE,
      MediaStore.Images.Media.WIDTH,
      MediaStore.Images.Media.HEIGHT,
      MediaStore.Images.Media.ORIENTATION,
      MediaStore.Images.Media.SIZE,
      MediaStore.Images.Media.MIME_TYPE,
      MediaStore.Images.Media.DATA,
      MediaStore.Images.Media.DISPLAY_NAME,
      MediaStore.Images.Media.DATE_ADDED,
      MediaStore.Images.Media.DATE_MODIFIED,
      MediaStore.Images.Media.DATE_TAKEN
    )
  }

  fun buildDocumentProjection(): Array<String> {
    return arrayOf(
      MediaStore.Files.FileColumns._ID,
      MediaStore.Files.FileColumns.TITLE,
      MediaStore.Files.FileColumns.SIZE,
      MediaStore.Files.FileColumns.MIME_TYPE,
      MediaStore.Files.FileColumns.DATA,
      MediaStore.Files.FileColumns.DISPLAY_NAME,
      MediaStore.Files.FileColumns.DATE_ADDED,
      MediaStore.Files.FileColumns.DATE_MODIFIED,
      MediaStore.Files.FileColumns.RELATIVE_PATH
    )
  }

  fun buildAlbumProjection(): Array<String> {
    return arrayOf(
      MediaStore.Audio.Albums._ID,
      MediaStore.Audio.Albums.ALBUM,
      MediaStore.Audio.Albums.ARTIST,
      MediaStore.Audio.Albums.NUMBER_OF_SONGS,
      MediaStore.Audio.Albums.FIRST_YEAR,
      MediaStore.Audio.Albums.LAST_YEAR
    )
  }

  fun buildArtistProjection(): Array<String> {
    return arrayOf(
      MediaStore.Audio.Artists._ID,
      MediaStore.Audio.Artists.ARTIST,
      MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
      MediaStore.Audio.Artists.NUMBER_OF_TRACKS
    )
  }

  fun buildFilter(filter: FilterOptionsRecord?): String? {
    if (filter == null) return null
    val conditions = mutableListOf<String>()

    filter.mimeTypes?.let { types ->
      if (types.isNotEmpty()) {
        val quoted = types.joinToString(", ") { "'$it'" }
        conditions.add("${MediaStore.MediaColumns.MIME_TYPE} IN ($quoted)")
      }
    }

    filter.extensions?.let { exts ->
      if (exts.isNotEmpty()) {
        val quoted = exts.joinToString(", ") { "'$it'" }
        conditions.add("${MediaStore.MediaColumns.DATA} LIKE '%.' || $quoted")
      }
    }

    filter.folder?.let {
      conditions.add("${MediaStore.MediaColumns.RELATIVE_PATH} LIKE '${escapeLike(it)}%'")
    }

    filter.album?.let {
      conditions.add("${MediaStore.Audio.Media.ALBUM} = '${it.replace("'", "''")}'")
    }

    filter.artist?.let {
      conditions.add("${MediaStore.Audio.Media.ARTIST} = '${it.replace("'", "''")}'")
    }

    filter.minDuration?.let { conditions.add("${MediaStore.Audio.Media.DURATION} >= $it") }
    filter.maxDuration?.let { conditions.add("${MediaStore.Audio.Media.DURATION} <= $it") }
    filter.minSize?.let { conditions.add("${MediaStore.MediaColumns.SIZE} >= $it") }
    filter.maxSize?.let { conditions.add("${MediaStore.MediaColumns.SIZE} <= $it") }
    filter.startDate?.let { conditions.add("${MediaStore.MediaColumns.DATE_ADDED} >= $it") }
    filter.endDate?.let { conditions.add("${MediaStore.MediaColumns.DATE_ADDED} <= $it") }

    filter.favoritesOnly?.let { if (it) conditions.add("${MediaStore.MediaColumns.IS_FAVORITE} = 1") }

    return if (conditions.isNotEmpty()) conditions.joinToString(" AND ") else null
  }

  fun buildAlbumFilter(filter: FilterOptionsRecord?): String? {
    if (filter == null) return null
    val conditions = mutableListOf<String>()

    filter.artist?.let {
      conditions.add("${MediaStore.Audio.Albums.ARTIST} = '${it.replace("'", "''")}'")
    }
    filter.startDate?.let { conditions.add("${MediaStore.Audio.Albums._ID} IN (SELECT DISTINCT ${MediaStore.Audio.Media.ALBUM_ID} FROM ${MediaStore.Audio.Media.EXTERNAL_CONTENT_URI} WHERE ${MediaStore.Audio.Media.DATE_ADDED} >= $it)") }
    filter.endDate?.let { conditions.add("${MediaStore.Audio.Albums._ID} IN (SELECT DISTINCT ${MediaStore.Audio.Media.ALBUM_ID} FROM ${MediaStore.Audio.Media.EXTERNAL_CONTENT_URI} WHERE ${MediaStore.Audio.Media.DATE_ADDED} <= $it)") }

    return if (conditions.isNotEmpty()) conditions.joinToString(" AND ") else null
  }

  fun buildDocumentFilter(filter: FilterOptionsRecord?): String? {
    val conditions = mutableListOf<String>()
    val docMimeTypes = listOf(
      "application/pdf", "application/msword",
      "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
      "application/vnd.ms-excel",
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
      "application/vnd.ms-powerpoint",
      "application/vnd.openxmlformats-officedocument.presentationml.presentation",
      "text/plain", "application/epub+zip", "application/rtf",
      "text/csv", "application/json", "application/xml",
      "application/zip", "application/x-rar-compressed", "application/x-7z-compressed"
    )
    val quotedMimes = docMimeTypes.joinToString(", ") { "'$it'" }
    conditions.add("${MediaStore.MediaColumns.MIME_TYPE} IN ($quotedMimes)")

    if (filter != null) {
      filter.minSize?.let { conditions.add("${MediaStore.MediaColumns.SIZE} >= $it") }
      filter.maxSize?.let { conditions.add("${MediaStore.MediaColumns.SIZE} <= $it") }
      filter.startDate?.let { conditions.add("${MediaStore.MediaColumns.DATE_ADDED} >= $it") }
      filter.endDate?.let { conditions.add("${MediaStore.MediaColumns.DATE_ADDED} <= $it") }
      filter.folder?.let { conditions.add("${MediaStore.MediaColumns.RELATIVE_PATH} LIKE '${escapeLike(it)}%'") }

      filter.extensions?.let { exts ->
        if (exts.isNotEmpty()) {
          conditions.add("(${exts.joinToString(" OR ") { "${MediaStore.MediaColumns.DATA} LIKE '%.$it'" }})")
        }
      }
    }

    return conditions.joinToString(" AND ")
  }

  fun buildSortOrder(sort: SortOptionsRecord?): String? {
    if (sort == null) return null
    val direction = if (sort.order == "desc") "DESC" else "ASC"
    val column = when (sort.field) {
      "name" -> MediaStore.MediaColumns.TITLE
      "dateAdded" -> MediaStore.MediaColumns.DATE_ADDED
      "dateModified" -> MediaStore.MediaColumns.DATE_MODIFIED
      "duration" -> MediaStore.Audio.Media.DURATION
      "artist" -> MediaStore.Audio.Media.ARTIST
      "album" -> MediaStore.Audio.Media.ALBUM
      "year" -> MediaStore.Audio.Media.YEAR
      "fileSize" -> MediaStore.MediaColumns.SIZE
      "resolution" -> MediaStore.Video.Media.RESOLUTION
      "width" -> MediaStore.Video.Media.WIDTH
      "height" -> MediaStore.Video.Media.HEIGHT
      else -> MediaStore.MediaColumns.TITLE
    }
    return "$column $direction"
  }

  private fun escapeLike(value: String): String {
    return value.replace("\\", "\\\\")
      .replace("%", "\\%")
      .replace("_", "\\_")
  }
}
