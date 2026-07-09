package expo.modules.mediastore

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

class MediaStorePermissions(private val context: Context) {
  fun checkStatus(): Map<String, Any?> {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      return mapOf(
        "granted" to isPermissionGranted(Manifest.permission.READ_MEDIA_AUDIO) &&
          isPermissionGranted(Manifest.permission.READ_MEDIA_VIDEO) &&
          isPermissionGranted(Manifest.permission.READ_MEDIA_IMAGES),
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
    return checkStatus()
  }

  private fun isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
  }
}
