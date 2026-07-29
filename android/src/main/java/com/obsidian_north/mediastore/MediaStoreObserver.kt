package com.obsidian_north.mediastore

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore

data class MediaChangeEvent(
  val type: String, val mediaType: String, val itemId: String, val uri: String
)

class MediaStoreObserver(private val context: Context) {
  private var observer: ContentObserver? = null
  private var listener: ((MediaChangeEvent) -> Unit)? = null
  private var isListening = false

  fun startListening(onChange: (MediaChangeEvent) -> Unit) {
    if (isListening) return
    listener = onChange
    isListening = true

    observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
      override fun onChange(selfChange: Boolean, uri: Uri?) {
        if (uri != null) {
          val event = parseUri(uri)
          if (event != null) listener?.invoke(event)
        }
      }
    }

    val cr = context.contentResolver
    cr.registerContentObserver(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, true, observer!!)
    cr.registerContentObserver(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, true, observer!!)
    cr.registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, observer!!)
    cr.registerContentObserver(MediaStore.Files.getContentUri("external"), true, observer!!)
  }

  fun stopListening() {
    if (!isListening) return
    observer?.let { context.contentResolver.unregisterContentObserver(it) }
    observer = null; listener = null; isListening = false
  }

  private fun parseUri(uri: Uri): MediaChangeEvent? {
    val uriStr = uri.toString()
    val mediaType = when {
      uriStr.contains(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString()) -> "audio"
      uriStr.contains(MediaStore.Video.Media.EXTERNAL_CONTENT_URI.toString()) -> "video"
      uriStr.contains(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString()) -> "image"
      uriStr.contains(MediaStore.Files.getContentUri("external").toString()) -> "document"
      else -> return null
    }
    val type = when {
      uriStr.contains("/update/") -> "modified"
      uriStr.contains("/insert/") -> "added"
      uriStr.contains("/delete/") -> "removed"
      else -> "modified"
    }
    val itemId = uri.lastPathSegment ?: ""
    return MediaChangeEvent(type = type, mediaType = mediaType, itemId = itemId, uri = uriStr)
  }

  fun isActive(): Boolean = isListening
}
