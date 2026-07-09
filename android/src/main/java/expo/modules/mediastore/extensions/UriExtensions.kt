package expo.modules.mediastore.extensions

import android.net.Uri
import android.provider.MediaStore

fun Uri.isAudioUri(): Boolean {
  return toString().startsWith(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString())
}

fun Uri.isVideoUri(): Boolean {
  return toString().startsWith(MediaStore.Video.Media.EXTERNAL_CONTENT_URI.toString())
}

fun Uri.isImageUri(): Boolean {
  return toString().startsWith(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString())
}

fun Uri.isDocumentUri(): Boolean {
  return toString().startsWith("content://com.android.providers.media.documents")
}

fun Uri.getIdFromUri(): Long {
  return try {
    lastPathSegment?.toLongOrNull() ?: -1L
  } catch (e: NumberFormatException) {
    -1L
  }
}
