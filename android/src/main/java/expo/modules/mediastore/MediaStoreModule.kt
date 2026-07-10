package expo.modules.mediastore

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.CancellationSignal
import android.os.Environment
import android.provider.MediaStore
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.mediastore.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest

class MediaStoreModule : Module() {
  private val context: Context
    get() = appContext.reactContext ?: appContext.androidContext
      ?: throw IllegalStateException("React context unavailable")

  private val repository by lazy { MediaStoreRepository(context) }
  private val mapper by lazy { MediaStoreMapper() }
  private val queryBuilder by lazy { MediaStoreQueryBuilder() }
  private val permissions by lazy { MediaStorePermissions(context) }
  private val observer by lazy { MediaStoreObserver(context) }
  private val cache by lazy { MediaStoreCache() }

  override fun definition() = ModuleDefinition {
    Name("MediaStore")

    Events("onMediaChange")

    AsyncFunction("getAudio") Coroutine { sort: SortOptionsRecord?, filter: FilterOptionsRecord?, pagination: PaginationOptionsRecord? ->
      val cacheKey = "audio:${sort}:${filter}:${pagination}"
      val cached = cache.get(cacheKey) as? List<AudioRecord>
      if (cached != null) return@Coroutine cached

      val sortOrder = queryBuilder.applyPagination(queryBuilder.buildSortOrder(sort), pagination)
      val cursor = repository.queryAudio(queryBuilder.buildAudioProjection(), queryBuilder.buildFilter(filter), null, sortOrder)
      val result = cursor?.use { mapper.mapAudio(it) } ?: emptyList()
      cache.put(cacheKey, result)
      result
    }

    AsyncFunction("getVideos") Coroutine { sort: SortOptionsRecord?, filter: FilterOptionsRecord?, pagination: PaginationOptionsRecord? ->
      val cacheKey = "videos:${sort}:${filter}:${pagination}"
      val cached = cache.get(cacheKey) as? List<VideoRecord>
      if (cached != null) return@Coroutine cached

      val sortOrder = queryBuilder.applyPagination(queryBuilder.buildSortOrder(sort), pagination)
      val cursor = repository.queryVideo(queryBuilder.buildVideoProjection(), queryBuilder.buildFilter(filter), null, sortOrder)
      val result = cursor?.use { mapper.mapVideo(it) } ?: emptyList()
      cache.put(cacheKey, result)
      result
    }

    AsyncFunction("getImages") Coroutine { sort: SortOptionsRecord?, filter: FilterOptionsRecord?, pagination: PaginationOptionsRecord? ->
      val cacheKey = "images:${sort}:${filter}:${pagination}"
      val cached = cache.get(cacheKey) as? List<ImageRecord>
      if (cached != null) return@Coroutine cached

      val sortOrder = queryBuilder.applyPagination(queryBuilder.buildSortOrder(sort), pagination)
      val cursor = repository.queryImages(queryBuilder.buildImageProjection(), queryBuilder.buildFilter(filter), null, sortOrder)
      val result = cursor?.use { mapper.mapImages(it) } ?: emptyList()
      cache.put(cacheKey, result)
      result
    }

    AsyncFunction("getDocuments") Coroutine { sort: SortOptionsRecord?, filter: FilterOptionsRecord?, pagination: PaginationOptionsRecord? ->
      val cacheKey = "documents:${sort}:${filter}:${pagination}"
      val cached = cache.get(cacheKey) as? List<DocumentRecord>
      if (cached != null) return@Coroutine cached

      val sortOrder = queryBuilder.applyPagination(queryBuilder.buildSortOrder(sort), pagination)
      val cursor = repository.queryDocuments(queryBuilder.buildDocumentProjection(), queryBuilder.buildDocumentFilter(filter), null, sortOrder)
      val result = cursor?.use { mapper.mapDocuments(it) } ?: emptyList()
      cache.put(cacheKey, result)
      result
    }

    AsyncFunction("getAlbums") Coroutine { sort: SortOptionsRecord?, filter: FilterOptionsRecord?, pagination: PaginationOptionsRecord? ->
      val cacheKey = "albums:${sort}:${filter}:${pagination}"
      val cached = cache.get(cacheKey) as? List<AlbumRecord>
      if (cached != null) return@Coroutine cached

      val sortOrder = queryBuilder.applyPagination(queryBuilder.buildSortOrder(sort), pagination)
      val cursor = repository.queryAlbums(queryBuilder.buildAlbumProjection(), queryBuilder.buildAlbumFilter(filter), null, sortOrder)
      val result = cursor?.use { mapper.mapAlbums(it) } ?: emptyList()
      cache.put(cacheKey, result)
      result
    }

    AsyncFunction("getArtists") Coroutine { sort: SortOptionsRecord?, pagination: PaginationOptionsRecord? ->
      val cacheKey = "artists:${sort}:${pagination}"
      val cached = cache.get(cacheKey) as? List<ArtistRecord>
      if (cached != null) return@Coroutine cached

      val sortOrder = queryBuilder.applyPagination(queryBuilder.buildSortOrder(sort), pagination)
      val cursor = repository.queryArtists(queryBuilder.buildArtistProjection(), null, null, sortOrder)
      val result = cursor?.use { mapper.mapArtists(it) } ?: emptyList()
      cache.put(cacheKey, result)
      result
    }

    AsyncFunction("getGenres") Coroutine { sort: SortOptionsRecord?, pagination: PaginationOptionsRecord? ->
      val cacheKey = "genres:${sort}:${pagination}"
      val cached = cache.get(cacheKey) as? List<GenreRecord>
      if (cached != null) return@Coroutine cached

      val sortOrder = queryBuilder.applyPagination(queryBuilder.buildSortOrder(sort), pagination)
      val cursor = repository.queryGenres(queryBuilder.buildGenreProjection(), null, null, sortOrder)
      val result = cursor?.use { mapper.mapGenres(it) } ?: emptyList()
      cache.put(cacheKey, result)
      result
    }

    AsyncFunction("getPlaylists") Coroutine { sort: SortOptionsRecord?, pagination: PaginationOptionsRecord? ->
      val cacheKey = "playlists:${sort}:${pagination}"
      val cached = cache.get(cacheKey) as? List<PlaylistRecord>
      if (cached != null) return@Coroutine cached

      val sortOrder = queryBuilder.applyPagination(queryBuilder.buildSortOrder(sort), pagination)
      val cursor = repository.queryPlaylists(queryBuilder.buildPlaylistProjection(), null, null, sortOrder)
      val result = cursor?.use { mapper.mapPlaylists(it) } ?: emptyList()
      cache.put(cacheKey, result)
      result
    }

    AsyncFunction("getFolders") Coroutine { sort: SortOptionsRecord?, filter: FilterOptionsRecord?, pagination: PaginationOptionsRecord? ->
      val cacheKey = "folders:${sort}:${filter}:${pagination}"
      val cached = cache.get(cacheKey) as? List<FolderRecord>
      if (cached != null) return@Coroutine cached

      val sortOrder = queryBuilder.applyPagination(queryBuilder.buildSortOrder(sort), pagination)
      val cursor = repository.queryFolders(queryBuilder.buildDocumentFilter(filter), sortOrder)
      val result = cursor?.use { mapper.mapFolders(it) } ?: emptyList()
      cache.put(cacheKey, result)
      result
    }

    AsyncFunction("getFolderStatistics") Coroutine { folderPath: String? ->
      val cacheKey = "folderStats:${folderPath}"
      val cached = cache.get(cacheKey) as? List<FolderStatisticsRecord>
      if (cached != null) return@Coroutine cached

      val cursor = repository.queryFolderStatistics(folderPath)
      val result = cursor?.use { mapper.mapFolderStatistics(it) } ?: emptyList()
      cache.put(cacheKey, result)
      result
    }

    AsyncFunction("refreshIncremental") Coroutine { lastTimestamp: Long? ->
      val since = lastTimestamp ?: cache.getLastRefreshTimestamp()
      val results = mutableMapOf<String, Any>()
      var addedCount = 0
      var modifiedCount = 0

      val types = listOf("audio", "video", "image", "document")
      for (type in types) {
        val projection = when (type) {
          "audio" -> queryBuilder.buildAudioProjection()
          "video" -> queryBuilder.buildVideoProjection()
          "image" -> queryBuilder.buildImageProjection()
          "document" -> queryBuilder.buildDocumentProjection()
          else -> continue
        }
        val cursor = repository.queryIncremental(since, projection, type)
        if (cursor != null) {
          val count = cursor.count
          addedCount += count
          cursor.close()
        }
      }

      val removedItems = cache.getAndClearRemovedItems()
      val removedCount = removedItems.size

      cache.setLastRefreshTimestamp(System.currentTimeMillis())

      IncrementalChangesRecord().apply {
        added = addedCount
        modified = modifiedCount
        removed = removedCount
        timestamp = System.currentTimeMillis()
      }
    }

    AsyncFunction("getLastRefreshTimestamp") Coroutine {
      cache.getLastRefreshTimestamp()
    }

    AsyncFunction("search") Coroutine { options: SearchOptionsRecord ->
      repository.search(options)
    }

    AsyncFunction("getById") Coroutine { mediaType: String, id: String ->
      val cursor = when (mediaType) {
        "audio" -> repository.queryAudio(queryBuilder.buildAudioProjection(), "${MediaStore.Audio.Media._ID} = ?", arrayOf(id), null)
        "video" -> repository.queryVideo(queryBuilder.buildVideoProjection(), "${MediaStore.Video.Media._ID} = ?", arrayOf(id), null)
        "image" -> repository.queryImages(queryBuilder.buildImageProjection(), "${MediaStore.Images.Media._ID} = ?", arrayOf(id), null)
        "document" -> repository.queryDocuments(queryBuilder.buildDocumentProjection(), "${MediaStore.Files.FileColumns._ID} = ?", arrayOf(id), null)
        else -> null
      }
      if (cursor != null) {
        cursor.use { mapper.mapSingle(it, mediaType) }
      } else null
    }

    AsyncFunction("getByUri") Coroutine { uri: String ->
      val mediaType = mapper.detectMediaType(uri)
      val cursor = repository.queryByUri(uri)
      if (cursor != null) {
        cursor.use { mapper.mapSingle(it, mediaType) }
      } else null
    }

    AsyncFunction("getRecent") Coroutine { mediaType: String?, limit: Int? ->
      val actualLimit = limit ?: 50
      val sortOrder = "${MediaStore.MediaColumns.DATE_ADDED} DESC LIMIT $actualLimit"
      val results = mutableListOf<Any>()
      val types = if (mediaType != null) listOf(mediaType) else listOf("audio", "video", "image", "document")
      for (type in types) {
        val cursor = when (type) {
          "audio" -> repository.queryAudio(queryBuilder.buildAudioProjection(), null, null, sortOrder)
          "video" -> repository.queryVideo(queryBuilder.buildVideoProjection(), null, null, sortOrder)
          "image" -> repository.queryImages(queryBuilder.buildImageProjection(), null, null, sortOrder)
          "document" -> repository.queryDocuments(queryBuilder.buildDocumentProjection(), null, null, sortOrder)
          else -> null
        }
        if (cursor != null) {
          when (type) {
            "audio" -> cursor.use { results.addAll(mapper.mapGenericToAudio(it)) }
            "video" -> cursor.use { results.addAll(mapper.mapGenericToVideo(it)) }
            "image" -> cursor.use { results.addAll(mapper.mapGenericToImage(it)) }
            "document" -> cursor.use { results.addAll(mapper.mapGenericToDocument(it)) }
          }
        }
      }
      results
    }

    AsyncFunction("getFavorites") Coroutine { mediaType: String?, sort: SortOptionsRecord?, pagination: PaginationOptionsRecord? ->
      val favoriteSelection = "${MediaStore.MediaColumns.IS_FAVORITE} = 1"
      val sortOrder = queryBuilder.applyPagination(queryBuilder.buildSortOrder(sort), pagination)
      val results = mutableListOf<Any>()
      val types = if (mediaType != null) listOf(mediaType) else listOf("audio", "video", "image", "document")
      for (type in types) {
        val cursor = when (type) {
          "audio" -> repository.queryAudio(queryBuilder.buildAudioProjection(), favoriteSelection, null, sortOrder)
          "video" -> repository.queryVideo(queryBuilder.buildVideoProjection(), favoriteSelection, null, sortOrder)
          "image" -> repository.queryImages(queryBuilder.buildImageProjection(), favoriteSelection, null, sortOrder)
          "document" -> repository.queryDocuments(queryBuilder.buildDocumentProjection(), favoriteSelection, null, sortOrder)
          else -> null
        }
        if (cursor != null) {
          when (type) {
            "audio" -> cursor.use { results.addAll(mapper.mapGenericToAudio(it)) }
            "video" -> cursor.use { results.addAll(mapper.mapGenericToVideo(it)) }
            "image" -> cursor.use { results.addAll(mapper.mapGenericToImage(it)) }
            "document" -> cursor.use { results.addAll(mapper.mapGenericToDocument(it)) }
          }
        }
      }
      results
    }

    AsyncFunction("getLargestFiles") Coroutine { mediaType: String?, limit: Int? ->
      val actualLimit = limit ?: 50
      val sortOrder = "${MediaStore.MediaColumns.SIZE} DESC LIMIT $actualLimit"
      val results = mutableListOf<Any>()
      val types = if (mediaType != null) listOf(mediaType) else listOf("audio", "video", "image", "document")
      for (type in types) {
        val cursor = when (type) {
          "audio" -> repository.queryAudio(queryBuilder.buildAudioProjection(), null, null, sortOrder)
          "video" -> repository.queryVideo(queryBuilder.buildVideoProjection(), null, null, sortOrder)
          "image" -> repository.queryImages(queryBuilder.buildImageProjection(), null, null, sortOrder)
          "document" -> repository.queryDocuments(queryBuilder.buildDocumentProjection(), null, null, sortOrder)
          else -> null
        }
        if (cursor != null) {
          when (type) {
            "audio" -> cursor.use { results.addAll(mapper.mapGenericToAudio(it)) }
            "video" -> cursor.use { results.addAll(mapper.mapGenericToVideo(it)) }
            "image" -> cursor.use { results.addAll(mapper.mapGenericToImage(it)) }
            "document" -> cursor.use { results.addAll(mapper.mapGenericToDocument(it)) }
          }
        }
      }
      results
    }

    AsyncFunction("getDuplicates") Coroutine { mediaType: String? ->
      findDuplicates(mediaType)
    }

    AsyncFunction("getStatistics") Coroutine {
      repository.getStatistics()
    }

    AsyncFunction("refresh") Coroutine {
      cache.invalidate()
    }

    Function("checkPermissions") {
      permissions.checkStatus()
    }

    AsyncFunction("requestPermissions") Coroutine {
      permissions.request()
    }

    AsyncFunction("getAlbumArtwork") Coroutine { albumId: String ->
      val uri = expo.modules.mediastore.utils.ArtworkUtils.getAlbumArtworkUri(albumId)
      uri?.toString()
    }

    AsyncFunction("getVideoThumbnail") Coroutine { videoId: String, width: Int?, height: Int? ->
      generateThumbnail(videoId, "video", width, height)
    }

    AsyncFunction("getImageThumbnail") Coroutine { imageId: String, width: Int?, height: Int? ->
      generateThumbnail(imageId, "image", width, height)
    }

    OnStartObserving("onMediaChange") {
      observer.startListening { event ->
        sendEvent("onMediaChange", mapOf(
          "type" to event.type,
          "mediaType" to event.mediaType,
          "itemId" to event.itemId,
          "uri" to event.uri
        ))
        if (event.type == "removed") {
          cache.trackRemoved(event.itemId)
        }
        cache.invalidate()
      }
    }

    OnStopObserving("onMediaChange") {
      observer.stopListening()
    }
  }

  private suspend fun generateThumbnail(mediaId: String, mediaType: String, width: Int?, height: Int?): String? {
    return withContext(Dispatchers.IO) {
      try {
        val contentUri = when (mediaType) {
          "video" -> Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaId)
          "image" -> Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mediaId)
          else -> return@withContext null
        }

        val targetWidth = width ?: 320
        val targetHeight = height ?: 240

        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
          val signal = CancellationSignal()
          context.contentResolver.loadThumbnail(contentUri, android.util.Size(targetWidth, targetHeight), signal)
        } else {
          @Suppress("DEPRECATION")
          when (mediaType) {
            "video" -> MediaStore.Video.Thumbnails.getThumbnail(
              context.contentResolver,
              mediaId.toLong(),
              MediaStore.Video.Thumbnails.MINI_KIND,
              null
            )
            else -> MediaStore.Images.Thumbnails.getThumbnail(
              context.contentResolver,
              mediaId.toLong(),
              MediaStore.Images.Thumbnails.MINI_KIND,
              null
            )
          }
        }

        if (bitmap != null) {
          val thumbDir = File(context.cacheDir, "mediastore_thumbnails")
          thumbDir.mkdirs()
          val thumbFile = File(thumbDir, "${mediaType}_${mediaId}_${targetWidth}x${targetHeight}.jpg")
          FileOutputStream(thumbFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
          }
          bitmap.recycle()
          Uri.fromFile(thumbFile).toString()
        } else null
      } catch (e: Exception) {
        null
      }
    }
  }

  private suspend fun findDuplicates(mediaType: String?): List<DuplicateRecord> {
    return withContext(Dispatchers.IO) {
      val types = if (mediaType != null) listOf(mediaType) else listOf("audio", "video", "image")
      val allFiles = mutableListOf<Triple<String, Long, String>>()

      for (type in types) {
        val projection = arrayOf(
          MediaStore.MediaColumns._ID,
          MediaStore.MediaColumns.DATA,
          MediaStore.MediaColumns.SIZE
        )
        val cursor = when (type) {
          "audio" -> repository.queryAudio(projection, "${MediaStore.MediaColumns.SIZE} > 0", null, null)
          "video" -> repository.queryVideo(projection, "${MediaStore.MediaColumns.SIZE} > 0", null, null)
          "image" -> repository.queryImages(projection, "${MediaStore.MediaColumns.SIZE} > 0", null, null)
          else -> null
        }
        cursor?.use {
          while (it.moveToNext()) {
            val id = it.getLong(0).toString()
            val path = it.getString(1) ?: continue
            val size = it.getLong(2)
            allFiles.add(Triple(id, size, path))
          }
        }
      }

      val sizeGroups = allFiles.groupBy { it.second }
      val duplicates = mutableListOf<DuplicateRecord>()

      for ((size, files) in sizeGroups) {
        if (files.size < 2) continue

        val hashGroups = mutableMapOf<String, MutableList<Triple<String, Long, String>>>()
        for (file in files) {
          try {
            val hash = computeFileHash(file.third)
            hashGroups.getOrPut(hash) { mutableListOf() }.add(file)
          } catch (_: Exception) {}
        }

        for ((hash, group) in hashGroups) {
          if (group.size < 2) continue
          duplicates.add(DuplicateRecord().apply {
            fileHash = hash
            count = group.size
            totalSize = size * group.size
            items = emptyList()
          })
        }
      }

      duplicates.sortedByDescending { it.count }
    }
  }

  private fun computeFileHash(filePath: String): String {
    val file = File(filePath)
    if (!file.exists() || !file.canRead()) return ""

    val digest = MessageDigest.getInstance("MD5")
    val buffer = ByteArray(8192)

    try {
      file.inputStream().use { input ->
        var bytesRead: Int
        while (input.read(buffer).also { bytesRead = it } != -1) {
          digest.update(buffer, 0, bytesRead)
        }
      }
    } catch (_: Exception) {
      return ""
    }

    return digest.digest().joinToString("") { "%02x".format(it) }
  }
}
