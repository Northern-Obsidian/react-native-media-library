import Foundation
import Photos

@objc(MediaStore)
class MediaStoreModule: RCTEventEmitter {
  private var hasListeners = false

  override func supportedEvents() -> [String] {
    return ["onMediaChange"]
  }

  override func startObserving() {
    hasListeners = true
    let observer = MediaStoreObserver.shared
    observer.startListening { [weak self] event in
      self?.sendEvent(withName: "onMediaChange", body: event)
    }
  }

  override func stopObserving() {
    hasListeners = false
    MediaStoreObserver.shared.stopListening()
  }

  // MARK: - Audio

  @objc
  func getAudio(_ sort: NSDictionary?, filter: NSDictionary?, pagination: NSDictionary?,
                resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    let repository = MediaStoreRepository()
    resolve(repository.getAudio(sort: sort, filter: filter, pagination: pagination))
  }

  // MARK: - Videos

  @objc
  func getVideos(_ sort: NSDictionary?, filter: NSDictionary?, pagination: NSDictionary?,
                resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    let repository = MediaStoreRepository()
    resolve(repository.getVideos(sort: sort, filter: filter, pagination: pagination))
  }

  // MARK: - Images

  @objc
  func getImages(_ sort: NSDictionary?, filter: NSDictionary?, pagination: NSDictionary?,
                resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    let repository = MediaStoreRepository()
    resolve(repository.getImages(sort: sort, filter: filter, pagination: pagination))
  }

  // MARK: - Documents

  @objc
  func getDocuments(_ sort: NSDictionary?, filter: NSDictionary?, pagination: NSDictionary?,
                   resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    let repository = MediaStoreRepository()
    resolve(repository.getDocuments(sort: sort, filter: filter, pagination: pagination))
  }

  // MARK: - Albums

  @objc
  func getAlbums(_ sort: NSDictionary?, filter: NSDictionary?, pagination: NSDictionary?,
                resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    let repository = MediaStoreRepository()
    resolve(repository.getAlbums(sort: sort, filter: filter, pagination: pagination))
  }

  // MARK: - Artists

  @objc
  func getArtists(_ sort: NSDictionary?, pagination: NSDictionary?,
                 resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    let repository = MediaStoreRepository()
    resolve(repository.getArtists(sort: sort, pagination: pagination))
  }

  // MARK: - Genres

  @objc
  func getGenres(_ sort: NSDictionary?, pagination: NSDictionary?,
                resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    let repository = MediaStoreRepository()
    resolve(repository.getGenres(sort: sort, pagination: pagination))
  }

  // MARK: - Playlists

  @objc
  func getPlaylists(_ sort: NSDictionary?, pagination: NSDictionary?,
                   resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    let repository = MediaStoreRepository()
    resolve(repository.getPlaylists(sort: sort, pagination: pagination))
  }

  // MARK: - Folders

  @objc
  func getFolders(_ sort: NSDictionary?, filter: NSDictionary?, pagination: NSDictionary?,
                 resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    let repository = MediaStoreRepository()
    resolve(repository.getFolders(sort: sort, filter: filter, pagination: pagination))
  }

  // MARK: - Folder Statistics

  @objc
  func getFolderStatistics(_ folderPath: String?,
                          resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    let repository = MediaStoreRepository()
    resolve(repository.getFolderStatistics(folderPath: folderPath))
  }

  // MARK: - Incremental Refresh

  @objc
  func refreshIncremental(_ lastTimestamp: Double?,
                         resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    let repository = MediaStoreRepository()
    resolve(repository.refreshIncremental(lastTimestamp: lastTimestamp))
  }

  // MARK: - Last Refresh Timestamp

  @objc
  func getLastRefreshTimestamp(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    resolve(MediaStoreRepository.lastRefreshTimestamp)
  }

  // MARK: - Search

  @objc
  func search(_ options: NSDictionary,
             resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    let repository = MediaStoreRepository()
    resolve(repository.search(options: options as! [String: Any?]))
  }

  // MARK: - Lookups

  @objc
  func getById(_ mediaType: String, id: String,
              resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    let repository = MediaStoreRepository()
    resolve(repository.getById(mediaType: mediaType, id: id))
  }

  @objc
  func getByUri(_ uri: String,
               resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    let repository = MediaStoreRepository()
    resolve(repository.getByUri(uri: uri))
  }

  @objc
  func getDetailedMetadata(_ mediaType: String, id: String,
                           resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    let repository = MediaStoreRepository()
    resolve(repository.getDetailedMetadata(mediaType: mediaType, id: id))
  }

  @objc
  func getDetailedMetadataByUri(_ uri: String,
                                resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    let repository = MediaStoreRepository()
    resolve(repository.getDetailedMetadataByUri(uri: uri))
  }

  // MARK: - Recent

  @objc
  func getRecent(_ mediaType: String?, limit: Int?,
                resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    let repository = MediaStoreRepository()
    resolve(repository.getRecent(mediaType: mediaType, limit: limit))
  }

  // MARK: - Favorites

  @objc
  func getFavorites(_ mediaType: String?, sort: NSDictionary?, pagination: NSDictionary?,
                   resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    let repository = MediaStoreRepository()
    resolve(repository.getFavorites(mediaType: mediaType, sort: sort, pagination: pagination))
  }

  // MARK: - Largest Files

  @objc
  func getLargestFiles(_ mediaType: String?, limit: Int?,
                      resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    let repository = MediaStoreRepository()
    resolve(repository.getLargestFiles(mediaType: mediaType, limit: limit))
  }

  // MARK: - Duplicates

  @objc
  func getDuplicates(_ mediaType: String?,
                    resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    let repository = MediaStoreRepository()
    resolve(repository.getDuplicates(mediaType: mediaType))
  }

  // MARK: - Statistics

  @objc
  func getStatistics(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    let repository = MediaStoreRepository()
    resolve(repository.getStatistics())
  }

  // MARK: - Refresh

  @objc
  func refresh(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    resolve(nil)
  }

  // MARK: - Permissions

  @objc
  func checkPermissions(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    let permissions = MediaStorePermissions()
    resolve(permissions.checkStatus())
  }

  @objc
  func requestPermissions(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    Task {
      let permissions = MediaStorePermissions()
      let result = await permissions.request()
      resolve(result)
    }
  }

  // MARK: - Artwork

  @objc
  func getAlbumArtwork(_ albumId: String?,
                      resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    let repository = MediaStoreRepository()
    resolve(repository.getAlbumArtwork(albumId: albumId ?? ""))
  }

  // MARK: - Thumbnails

  @objc
  func getVideoThumbnail(_ videoId: String, width: Int?, height: Int?,
                        resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    let repository = MediaStoreRepository()
    resolve(repository.getThumbnail(assetId: videoId, mediaType: .video, width: width, height: height))
  }

  @objc
  func getImageThumbnail(_ imageId: String, width: Int?, height: Int?,
                        resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    let repository = MediaStoreRepository()
    resolve(repository.getThumbnail(assetId: imageId, mediaType: .photo, width: width, height: height))
  }

  // MARK: - Lifecycle

  override static func requiresMainQueueSetup() -> Bool {
    return false
  }

  deinit {
    if hasListeners {
      MediaStoreObserver.shared.stopListening()
    }
  }
}
