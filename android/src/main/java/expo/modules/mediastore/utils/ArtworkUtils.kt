package expo.modules.mediastore.utils

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore

object ArtworkUtils {
  fun getAlbumArtworkUri(albumId: Long): Uri {
    return ContentUris.withAppendedId(
      Uri.parse("content://media/external/audio/albumart"),
      albumId
    )
  }

  fun getAlbumArtworkUri(albumId: String): Uri? {
    return try {
      ContentUris.withAppendedId(
        Uri.parse("content://media/external/audio/albumart"),
        albumId.toLong()
      )
    } catch (e: NumberFormatException) {
      null
    }
  }
}
