import ExpoModulesCore

public class MediaStoreModule: Module {
  public func definition() -> ModuleDefinition {
    Name("MediaStore")

    Events("onMediaChange")

    AsyncFunction("getAudio") { (sort: Any?, filter: Any?, pagination: Any?) -> [[String: Any?]] in
      let repository = MediaStoreRepository()
      return repository.getAudio(sort: sort, filter: filter, pagination: pagination)
    }

    AsyncFunction("getVideos") { (sort: Any?, filter: Any?, pagination: Any?) -> [[String: Any?]] in
      let repository = MediaStoreRepository()
      return repository.getVideos(sort: sort, filter: filter, pagination: pagination)
    }

    AsyncFunction("getImages") { (sort: Any?, filter: Any?, pagination: Any?) -> [[String: Any?]] in
      let repository = MediaStoreRepository()
      return repository.getImages(sort: sort, filter: filter, pagination: pagination)
    }

    AsyncFunction("getDocuments") { (sort: Any?, filter: Any?, pagination: Any?) -> [[String: Any?]] in
      let repository = MediaStoreRepository()
      return repository.getDocuments(sort: sort, filter: filter, pagination: pagination)
    }

    AsyncFunction("getAlbums") { (sort: Any?, filter: Any?, pagination: Any?) -> [[String: Any?]] in
      let repository = MediaStoreRepository()
      return repository.getAlbums(sort: sort, filter: filter, pagination: pagination)
    }

    AsyncFunction("getArtists") { (sort: Any?, pagination: Any?) -> [[String: Any?]] in
      let repository = MediaStoreRepository()
      return repository.getArtists(sort: sort, pagination: pagination)
    }

    AsyncFunction("getGenres") { (sort: Any?, pagination: Any?) -> [[String: Any?]] in
      let repository = MediaStoreRepository()
      return repository.getGenres(sort: sort, pagination: pagination)
    }

    AsyncFunction("getPlaylists") { (sort: Any?, pagination: Any?) -> [[String: Any?]] in
      let repository = MediaStoreRepository()
      return repository.getPlaylists(sort: sort, pagination: pagination)
    }

    AsyncFunction("getFolders") { (sort: Any?, filter: Any?, pagination: Any?) -> [[String: Any?]] in
      let repository = MediaStoreRepository()
      return repository.getFolders(sort: sort, filter: filter, pagination: pagination)
    }

    AsyncFunction("search") { (options: [String: Any?]) -> [String: Any?] in
      let repository = MediaStoreRepository()
      return repository.search(options: options)
    }

    AsyncFunction("getById") { (mediaType: String, id: String) -> [String: Any?]? in
      let repository = MediaStoreRepository()
      return repository.getById(mediaType: mediaType, id: id)
    }

    AsyncFunction("getByUri") { (uri: String) -> [String: Any?]? in
      let repository = MediaStoreRepository()
      return repository.getByUri(uri: uri)
    }

    AsyncFunction("getRecent") { (mediaType: String?, limit: Int?) -> [[String: Any?]] in
      let repository = MediaStoreRepository()
      return repository.getRecent(mediaType: mediaType, limit: limit)
    }

    AsyncFunction("getFavorites") { (mediaType: String?, sort: Any?, pagination: Any?) -> [[String: Any?]] in
      let repository = MediaStoreRepository()
      return repository.getFavorites(mediaType: mediaType, sort: sort, pagination: pagination)
    }

    AsyncFunction("getLargestFiles") { (mediaType: String?, limit: Int?) -> [[String: Any?]] in
      let repository = MediaStoreRepository()
      return repository.getLargestFiles(mediaType: mediaType, limit: limit)
    }

    AsyncFunction("getDuplicates") { (mediaType: String?) -> [[String: Any?]] in
      let repository = MediaStoreRepository()
      return repository.getDuplicates(mediaType: mediaType)
    }

    AsyncFunction("getStatistics") { () -> [String: Any?] in
      let repository = MediaStoreRepository()
      return repository.getStatistics()
    }

    AsyncFunction("refresh") { () in
      // iOS uses PHCachingImageManager which handles its own cache
    }

    AsyncFunction("checkPermissions") { () -> [String: Any?] in
      let permissions = MediaStorePermissions()
      return permissions.checkStatus()
    }

    AsyncFunction("requestPermissions") { () -> [String: Any?] in
      let permissions = MediaStorePermissions()
      return try await permissions.request()
    }

    AsyncFunction("getAlbumArtwork") { (albumId: String) -> String? in
      let repository = MediaStoreRepository()
      return repository.getAlbumArtwork(albumId: albumId)
    }

    AsyncFunction("getVideoThumbnail") { (videoId: String, width: Int?, height: Int?) -> String? in
      let repository = MediaStoreRepository()
      return repository.getThumbnail(assetId: videoId, mediaType: .video, width: width, height: height)
    }

    AsyncFunction("getImageThumbnail") { (imageId: String, width: Int?, height: Int?) -> String? in
      let repository = MediaStoreRepository()
      return repository.getThumbnail(assetId: imageId, mediaType: .photo, width: width, height: height)
    }

    OnStartObserving {
      let observer = MediaStoreObserver.shared
      observer.startListening { event in
        self.sendEvent("onMediaChange", [
          "type": event["type"] ?? "",
          "mediaType": event["mediaType"] ?? "",
          "itemId": event["itemId"] ?? "",
          "uri": event["uri"] ?? ""
        ])
      }
    }

    OnStopObserving {
      MediaStoreObserver.shared.stopListening()
    }
  }
}
