package expo.modules.mediastore.utils

import android.database.Cursor

object CursorUtils {
  fun getString(cursor: Cursor, columnName: String): String {
    val index = cursor.getColumnIndex(columnName)
    return if (index >= 0) cursor.getString(index) ?: "" else ""
  }

  fun getStringOrNull(cursor: Cursor, columnName: String): String? {
    val index = cursor.getColumnIndex(columnName)
    return if (index >= 0) cursor.getString(index) else null
  }

  fun getInt(cursor: Cursor, columnName: String): Int {
    val index = cursor.getColumnIndex(columnName)
    return if (index >= 0) cursor.getInt(index) else 0
  }

  fun getLong(cursor: Cursor, columnName: String): Long {
    val index = cursor.getColumnIndex(columnName)
    return if (index >= 0) cursor.getLong(index) else 0L
  }

  fun getDoubleOrNull(cursor: Cursor, columnName: String): Double? {
    val index = cursor.getColumnIndex(columnName)
    return if (index >= 0) {
      if (cursor.isNull(index)) null else cursor.getDouble(index)
    } else null
  }

  fun getIntOrNull(cursor: Cursor, columnName: String): Int? {
    val index = cursor.getColumnIndex(columnName)
    return if (index >= 0) {
      if (cursor.isNull(index)) null else cursor.getInt(index)
    } else null
  }

  fun getLongOrNull(cursor: Cursor, columnName: String): Long? {
    val index = cursor.getColumnIndex(columnName)
    return if (index >= 0) {
      if (cursor.isNull(index)) null else cursor.getLong(index)
    } else null
  }

  fun hasColumn(cursor: Cursor, columnName: String): Boolean {
    return cursor.getColumnIndex(columnName) >= 0
  }
}
