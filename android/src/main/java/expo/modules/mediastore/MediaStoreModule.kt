package expo.modules.mediastore

import android.content.Context
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.mediastore.models.*

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
      val cursor = repository.queryAudio(queryBuilder.buildAudioProjection(), queryBuilder.buildFilter(filter), null, queryBuilder.buildSortOrder(sort))
      cursor?.use { mapper.mapAudio(it) } ?: emptyList()
    }

    AsyncFunction("getVideos") Coroutine { sort: SortOptionsRecord?, filter: FilterOptionsRecord?, pagination: PaginationOptionsRecord? ->
      val cursor = repository.queryVideo(queryBuilder.buildVideoProjection(), queryBuilder.buildFilter(filter), null, queryBuilder.buildSortOrder(sort))
      cursor?.use { mapper.mapVideo(it) } ?: emptyList()
    }

    AsyncFunction("getImages") Coroutine { sort: SortOptionsRecord?, filter: FilterOptionsRecord?, pagination: PaginationOptionsRecord? ->
      val cursor = repository.queryImages(queryBuilder.buildImageProjection(), queryBuilder.buildFilter(filter), null, queryBuilder.buildSortOrder(sort))
      cursor?.use { mapper.mapImages(it) } ?: emptyList()
    }

    AsyncFunction("getDocuments") Coroutine { sort: SortOptionsRecord?, filter: FilterOptionsRecord?, pagination: PaginationOptionsRecord? ->
      val cursor = repository.queryDocuments(queryBuilder.buildDocumentProjection(), queryBuilder.buildDocumentFilter(filter), null, queryBuilder.buildSortOrder(sort))
      cursor?.use { mapper.mapDocuments(it) } ?: emptyList()
    }

    AsyncFunction("getAlbums") Coroutine { sort: SortOptionsRecord?, filter: FilterOptionsRecord?, pagination: PaginationOptionsRecord? ->
      val cursor = repository.queryAlbums(queryBuilder.buildAlbumProjection(), queryBuilder.buildAlbumFilter(filter), null, queryBuilder.buildSortOrder(sort))
      cursor?.use { mapper.mapAlbums(it) } ?: emptyList()
    }

    AsyncFunction("getArtists") Coroutine { sort: SortOptionsRecord?, pagination: PaginationOptionsRecord? ->
      val cursor = repository.queryArtists(queryBuilder.buildArtistProjection(), null, null, queryBuilder.buildSortOrder(sort))
      cursor?.use { mapper.mapArtists(it) } ?: emptyList()
    }

    AsyncFunction("getGenres") Coroutine { sort: SortOptionsRecord?, pagination: PaginationOptionsRecord? ->
      val cursor = repository.queryGenres()
      cursor?.use { mapper.mapGenres(it) } ?: emptyList()
    }

    AsyncFunction("getPlaylists") Coroutine { sort: SortOptionsRecord?, pagination: PaginationOptionsRecord? ->
      val cursor = repository.queryPlaylists()
      cursor?.use { mapper.mapPlaylists(it) } ?: emptyList()
    }

    AsyncFunction("getFolders") Coroutine { sort: SortOptionsRecord?, filter: FilterOptionsRecord?, pagination: PaginationOptionsRecord? ->
      val cursor = repository.queryFolders(queryBuilder.buildDocumentFilter(filter), queryBuilder.buildSortOrder(sort))
      cursor?.use { mapper.mapFolders(it) } ?: emptyList()
    }

    AsyncFunction("search") Coroutine { options: SearchOptionsRecord ->
      val results = repository.search(options)
      results
    }

    AsyncFunction("getById") Coroutine { mediaType: String, id: String ->
      val cursor = when (mediaType) {
        "audio" -> repository.queryAudio(queryBuilder.buildAudioProjection(), "${android.provider.MediaStore.Audio.Media._ID} = ?", arrayOf(id), null)
        "video" -> repository.queryVideo(queryBuilder.buildVideoProjection(), "${android.provider.MediaStore.Video.Media._ID} = ?", arrayOf(id), null)
        "image" -> repository.queryImages(queryBuilder.buildImageProjection(), "${android.provider.MediaStore.Images.Media._ID} = ?", arrayOf(id), null)
        "document" -> repository.queryDocuments(queryBuilder.buildDocumentProjection(), "${android.provider.MediaStore.Files.FileColumns._ID} = ?", arrayOf(id), null)
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
      val results = mutableListOf<Map<String, Any?>>()
      val types = if (mediaType != null) listOf(mediaType) else listOf("audio", "video", "image", "document")
      for (type in types) {
        val cursor = when (type) {
          "audio" -> repository.queryAudio(queryBuilder.buildAudioProjection(), null, null, "${android.provider.MediaStore.MediaColumns.DATE_ADDED} DESC LIMIT $actualLimit")
          "video" -> repository.queryVideo(queryBuilder.buildVideoProjection(), null, null, "${android.provider.MediaStore.MediaColumns.DATE_ADDED} DESC LIMIT $actualLimit")
          "image" -> repository.queryImages(queryBuilder.buildImageProjection(), null, null, "${android.provider.MediaStore.MediaColumns.DATE_ADDED} DESC LIMIT $actualLimit")
          "document" -> repository.queryDocuments(queryBuilder.buildDocumentProjection(), null, null, "${android.provider.MediaStore.MediaColumns.DATE_ADDED} DESC LIMIT $actualLimit")
          else -> null
        }
        cursor?.use { results.addAll(mapper.mapGeneric(it)) }
      }
      results.sortedByDescending { it["dateAdded"] as? Long ?: 0L }.take(actualLimit)
    }

    AsyncFunction("getFavorites") Coroutine { mediaType: String?, sort: SortOptionsRecord?, pagination: PaginationOptionsRecord? ->
      val favoriteSelection = "${android.provider.MediaStore.MediaColumns.IS_FAVORITE} = 1"
      val results = mutableListOf<Map<String, Any?>>()
      val types = if (mediaType != null) listOf(mediaType) else listOf("audio", "video", "image", "document")
      for (type in types) {
        val cursor = when (type) {
          "audio" -> repository.queryAudio(queryBuilder.buildAudioProjection(), favoriteSelection, null, queryBuilder.buildSortOrder(sort))
          "video" -> repository.queryVideo(queryBuilder.buildVideoProjection(), favoriteSelection, null, queryBuilder.buildSortOrder(sort))
          "image" -> repository.queryImages(queryBuilder.buildImageProjection(), favoriteSelection, null, queryBuilder.buildSortOrder(sort))
          "document" -> repository.queryDocuments(queryBuilder.buildDocumentProjection(), favoriteSelection, null, queryBuilder.buildSortOrder(sort))
          else -> null
        }
        cursor?.use { results.addAll(mapper.mapGeneric(it)) }
      }
      results
    }

    AsyncFunction("getLargestFiles") Coroutine { mediaType: String?, limit: Int? ->
      val actualLimit = limit ?: 50
      val results = mutableListOf<Map<String, Any?>>()
      val types = if (mediaType != null) listOf(mediaType) else listOf("audio", "video", "image", "document")
      for (type in types) {
        val cursor = when (type) {
          "audio" -> repository.queryAudio(queryBuilder.buildAudioProjection(), null, null, "${android.provider.MediaStore.MediaColumns.SIZE} DESC LIMIT $actualLimit")
          "video" -> repository.queryVideo(queryBuilder.buildVideoProjection(), null, null, "${android.provider.MediaStore.MediaColumns.SIZE} DESC LIMIT $actualLimit")
          "image" -> repository.queryImages(queryBuilder.buildImageProjection(), null, null, "${android.provider.MediaStore.MediaColumns.SIZE} DESC LIMIT $actualLimit")
          "document" -> repository.queryDocuments(queryBuilder.buildDocumentProjection(), null, null, "${android.provider.MediaStore.MediaColumns.SIZE} DESC LIMIT $actualLimit")
          else -> null
        }
        cursor?.use { results.addAll(mapper.mapGeneric(it)) }
      }
      results.sortedByDescending { it["size"] as? Long ?: 0L }.take(actualLimit)
    }

    AsyncFunction("getDuplicates") Coroutine { mediaType: String? ->
      emptyList<DuplicateRecord>()
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

    OnStartObserving("onMediaChange") {
      observer.startListening { event ->
        sendEvent("onMediaChange", mapOf(
          "type" to event.type,
          "mediaType" to event.mediaType,
          "itemId" to event.itemId,
          "uri" to event.uri
        ))
      }
    }

    OnStopObserving("onMediaChange") {
      observer.stopListening()
    }
  }
}
