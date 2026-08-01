package com.obsidian_north.mediastore

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.facebook.react.bridge.ReactApplicationContext

class MediaStorePermissions(private val context: Context, private val reactContext: ReactApplicationContext? = null) {
  fun checkStatus(): Map<String, Any?> {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      return mapOf(
        "granted" to (isPermissionGranted(Manifest.permission.READ_MEDIA_AUDIO) &&
          isPermissionGranted(Manifest.permission.READ_MEDIA_VIDEO) &&
          isPermissionGranted(Manifest.permission.READ_MEDIA_IMAGES)),
        "audio" to isPermissionGranted(Manifest.permission.READ_MEDIA_AUDIO),
        "video" to isPermissionGranted(Manifest.permission.READ_MEDIA_VIDEO),
        "images" to isPermissionGranted(Manifest.permission.READ_MEDIA_IMAGES)
      )
    } else {
      return mapOf(
        "granted" to isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE),
        "audio" to isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE),
        "video" to isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE),
        "images" to isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)
      )
    }
  }

  suspend fun request(): Map<String, Any?> {
    if (isAlreadyGranted()) return checkStatus()

    val activity = getActivity()
    if (activity != null) {
      val permissions = getRequiredPermissions()
      ActivityCompat.requestPermissions(activity, permissions, PERMISSION_REQUEST_CODE)
      kotlinx.coroutines.delay(500)
    }

    return checkStatus()
  }

  private fun isAlreadyGranted(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      return isPermissionGranted(Manifest.permission.READ_MEDIA_AUDIO) &&
        isPermissionGranted(Manifest.permission.READ_MEDIA_VIDEO) &&
        isPermissionGranted(Manifest.permission.READ_MEDIA_IMAGES)
    } else {
      return isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
  }

  private fun getRequiredPermissions(): Array<String> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      arrayOf(Manifest.permission.READ_MEDIA_AUDIO, Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_IMAGES)
    } else {
      arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
  }

  private fun getActivity(): Activity? {
    return reactContext?.currentActivity
  }

  private fun isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
  }

  companion object {
    private const val PERMISSION_REQUEST_CODE = 1984
  }
}
