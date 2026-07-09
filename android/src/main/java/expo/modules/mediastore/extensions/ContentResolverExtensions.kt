package expo.modules.mediastore.extensions

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

fun ContentResolver.queryMedia(
  uri: Uri,
  projection: Array<String>?,
  selection: String? = null,
  selectionArgs: Array<String>? = null,
  sortOrder: String? = null
): Cursor? {
  return query(uri, projection, selection, selectionArgs, sortOrder)
}

fun ContentResolver.queryAudio(
  projection: Array<String>?,
  selection: String? = null,
  selectionArgs: Array<String>? = null,
  sortOrder: String? = null
): Cursor? {
  return queryMedia(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, sortOrder)
}

fun ContentResolver.queryVideo(
  projection: Array<String>?,
  selection: String? = null,
  selectionArgs: Array<String>? = null,
  sortOrder: String? = null
): Cursor? {
  return queryMedia(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, sortOrder)
}

fun ContentResolver.queryImages(
  projection: Array<String>?,
  selection: String? = null,
  selectionArgs: Array<String>? = null,
  sortOrder: String? = null
): Cursor? {
  return queryMedia(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, sortOrder)
}

fun ContentResolver.queryDocuments(
  projection: Array<String>?,
  selection: String? = null,
  selectionArgs: Array<String>? = null,
  sortOrder: String? = null
): Cursor? {
  return queryMedia(MediaStore.Files.getContentUri("external"), projection, selection, selectionArgs, sortOrder)
}
