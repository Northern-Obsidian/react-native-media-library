package expo.modules.mediastore.extensions

import android.database.Cursor
import expo.modules.mediastore.utils.CursorUtils

fun Cursor.getStringSafe(columnName: String): String {
  return CursorUtils.getString(this, columnName)
}

fun Cursor.getStringOrNull(columnName: String): String? {
  return CursorUtils.getStringOrNull(this, columnName)
}

fun Cursor.getIntSafe(columnName: String): Int {
  return CursorUtils.getInt(this, columnName)
}

fun Cursor.getLongSafe(columnName: String): Long {
  return CursorUtils.getLong(this, columnName)
}

fun Cursor.getDoubleOrNull(columnName: String): Double? {
  return CursorUtils.getDoubleOrNull(this, columnName)
}


