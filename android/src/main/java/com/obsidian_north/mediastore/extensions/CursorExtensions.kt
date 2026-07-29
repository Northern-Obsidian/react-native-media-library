package com.obsidian_north.mediastore.extensions

import android.database.Cursor
import com.obsidian_north.mediastore.utils.CursorUtils

fun Cursor.getStringSafe(columnName: String): String = CursorUtils.getString(this, columnName)
fun Cursor.getStringOrNull(columnName: String): String? = CursorUtils.getStringOrNull(this, columnName)
fun Cursor.getIntSafe(columnName: String): Int = CursorUtils.getInt(this, columnName)
fun Cursor.getLongSafe(columnName: String): Long = CursorUtils.getLong(this, columnName)
fun Cursor.getDoubleOrNull(columnName: String): Double? = CursorUtils.getDoubleOrNull(this, columnName)
