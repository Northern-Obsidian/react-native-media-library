package expo.modules.mediastore.utils

object MimeUtils {
  private val audioMimeTypes = setOf(
    "audio/mpeg", "audio/mp4", "audio/wav", "audio/ogg", "audio/flac",
    "audio/aac", "audio/x-m4a", "audio/x-wav", "audio/x-flac",
    "audio/x-ms-wma", "audio/x-aiff", "audio/amr", "audio/webm",
    "audio/opus"
  )

  private val videoMimeTypes = setOf(
    "video/mp4", "video/x-matroska", "video/webm", "video/avi",
    "video/x-msvideo", "video/quicktime", "video/x-ms-wmv",
    "video/mpeg", "video/3gpp", "video/x-flv",
    "video/x-m4v", "video/mp2t"
  )

  private val imageMimeTypes = setOf(
    "image/jpeg", "image/png", "image/webp", "image/gif",
    "image/bmp", "image/x-ms-bmp", "image/x-icon",
    "image/heif", "image/heic", "image/avif",
    "image/tiff", "image/svg+xml"
  )

  private val documentMimeTypes = mapOf(
    "application/pdf" to "pdf",
    "application/msword" to "doc",
    "application/vnd.openxmlformats-officedocument.wordprocessingml.document" to "docx",
    "application/vnd.ms-excel" to "xls",
    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" to "xlsx",
    "application/vnd.ms-powerpoint" to "ppt",
    "application/vnd.openxmlformats-officedocument.presentationml.presentation" to "pptx",
    "text/plain" to "txt",
    "application/epub+zip" to "epub",
    "application/rtf" to "rtf",
    "text/csv" to "csv",
    "application/json" to "json",
    "application/xml" to "xml",
    "text/xml" to "xml",
    "application/zip" to "zip",
    "application/x-rar-compressed" to "rar",
    "application/x-7z-compressed" to "7z"
  )

  private val extensionToMime = mapOf(
    "mp3" to "audio/mpeg", "m4a" to "audio/x-m4a", "wav" to "audio/wav",
    "flac" to "audio/flac", "ogg" to "audio/ogg", "aac" to "audio/aac",
    "wma" to "audio/x-ms-wma", "aiff" to "audio/x-aiff", "opus" to "audio/opus",
    "mp4" to "video/mp4", "mkv" to "video/x-matroska", "webm" to "video/webm",
    "avi" to "video/x-msvideo", "mov" to "video/quicktime", "wmv" to "video/x-ms-wmv",
    "jpg" to "image/jpeg", "jpeg" to "image/jpeg", "png" to "image/png",
    "webp" to "image/webp", "gif" to "image/gif", "bmp" to "image/bmp",
    "heic" to "image/heic", "heif" to "image/heif", "avif" to "image/avif",
    "pdf" to "application/pdf", "doc" to "application/msword",
    "docx" to "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
    "xls" to "application/vnd.ms-excel",
    "xlsx" to "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
    "ppt" to "application/vnd.ms-powerpoint",
    "pptx" to "application/vnd.openxmlformats-officedocument.presentationml.presentation",
    "txt" to "text/plain", "epub" to "application/epub+zip", "rtf" to "application/rtf",
    "csv" to "text/csv", "json" to "application/json", "xml" to "application/xml",
    "zip" to "application/zip", "rar" to "application/x-rar-compressed",
    "7z" to "application/x-7z-compressed"
  )

  fun isAudio(mimeType: String): Boolean {
    return audioMimeTypes.contains(mimeType)
  }

  fun isVideo(mimeType: String): Boolean {
    return videoMimeTypes.contains(mimeType)
  }

  fun isImage(mimeType: String): Boolean {
    return imageMimeTypes.contains(mimeType)
  }

  fun isDocument(mimeType: String): Boolean {
    return documentMimeTypes.containsKey(mimeType)
  }

  fun getExtensionFromMime(mimeType: String): String {
    return documentMimeTypes[mimeType] ?: mimeType.substringAfterLast("/", "")
  }

  fun getMimeFromExtension(extension: String): String {
    return extensionToMime[extension.lowercase()] ?: "application/octet-stream"
  }

  fun getMediaType(mimeType: String): String {
    return when {
      isAudio(mimeType) -> "audio"
      isVideo(mimeType) -> "video"
      isImage(mimeType) -> "image"
      isDocument(mimeType) -> "document"
      else -> "unknown"
    }
  }

  fun getDocumentMimeTypes(): List<String> {
    return documentMimeTypes.keys.toList()
  }

  fun getDocumentMimeFilter(): String {
    val mimes = listOf(
      "application/pdf", "application/msword",
      "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
      "application/vnd.ms-excel",
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
      "application/vnd.ms-powerpoint",
      "application/vnd.openxmlformats-officedocument.presentationml.presentation",
      "text/plain", "application/epub+zip", "application/rtf",
      "text/csv", "application/json", "application/xml", "text/xml",
      "application/zip", "application/x-rar-compressed", "application/x-7z-compressed"
    )
    return mimes.joinToString(", ") { "'$it'" }
  }
}
