import Photos
import AVFoundation
import UIKit

class MediaStoreRepository {
  private let imageManager = PHCachingImageManager()

  // MARK: - Audio

  func getAudio(sort: Any?, filter: Any?, pagination: Any?) -> [[String: Any?]] {
    let fetchOptions = PHFetchOptions()
    fetchOptions.predicate = NSPredicate(format: "mediaType == %d", PHAssetMediaType.audio.rawValue)

    applyFilterOptions(fetchOptions, filter: filter)
    applySortOptions(fetchOptions, sort: sort, mediaType: .audio)

    let results = PHAsset.fetchAssets(with: .audio, options: fetchOptions)
    var items: [[String: Any?]] = []

    results.enumerateObjects { asset, _, _ in
      let metadata = self.getAudioMetadata(for: asset)
      items.append(self.mapAudioAsset(asset, metadata: metadata))
    }

    return applyPagination(items, pagination: pagination)
  }

  // MARK: - Videos

  func getVideos(sort: Any?, filter: Any?, pagination: Any?) -> [[String: Any?]] {
    let fetchOptions = PHFetchOptions()
    fetchOptions.predicate = NSPredicate(format: "mediaType == %d", PHAssetMediaType.video.rawValue)

    applyFilterOptions(fetchOptions, filter: filter)
    applySortOptions(fetchOptions, sort: sort, mediaType: .video)

    let results = PHAsset.fetchAssets(with: .video, options: fetchOptions)
    var items: [[String: Any?]] = []

    results.enumerateObjects { asset, _, _ in
      items.append(self.mapVideoAsset(asset))
    }

    return applyPagination(items, pagination: pagination)
  }

  // MARK: - Images

  func getImages(sort: Any?, filter: Any?, pagination: Any?) -> [[String: Any?]] {
    let fetchOptions = PHFetchOptions()
    fetchOptions.predicate = NSPredicate(format: "mediaType == %d", PHAssetMediaType.image.rawValue)

    applyFilterOptions(fetchOptions, filter: filter)
    applySortOptions(fetchOptions, sort: sort, mediaType: .image)

    let results = PHAsset.fetchAssets(with: .image, options: fetchOptions)
    var items: [[String: Any?]] = []

    results.enumerateObjects { asset, _, _ in
      let metadata = self.getImageMetadata(for: asset)
      items.append(self.mapImageAsset(asset, metadata: metadata))
    }

    return applyPagination(items, pagination: pagination)
  }

  // MARK: - Documents (iOS has no direct equivalent - returns empty)

  func getDocuments(sort: Any?, filter: Any?, pagination: Any?) -> [[String: Any?]] {
    return []
  }

  // MARK: - Albums

  func getAlbums(sort: Any?, filter: Any?, pagination: Any?) -> [[String: Any?]] {
    var albumTypes: [PHAssetCollectionType] = [.album, .smartAlbum]
    if let filterDict = filter as? [String: Any?],
       let favoritesOnly = filterDict["favoritesOnly"] as? Bool, favoritesOnly {
      albumTypes = [.smartAlbum]
    }

    var items: [[String: Any?]] = []

    for type in albumTypes {
      let collections = PHAssetCollection.fetchAssetCollections(with: type, options: nil)
      collections.enumerateObjects { collection, _, _ in
        let fetchOptions = PHFetchOptions()
        fetchOptions.predicate = NSPredicate(format: "mediaType IN %@",
          [PHAssetMediaType.audio.rawValue, PHAssetMediaType.video.rawValue])

        let assets = PHAsset.fetchAssets(in: collection, options: fetchOptions)
        if assets.count > 0 {
          items.append([
            "id": collection.localIdentifier,
            "title": collection.localizedTitle ?? "",
            "artist": "",
            "songCount": assets.count,
            "duration": self.calculateTotalDuration(assets: assets),
            "artworkUri": self.getAlbumArtworkUri(collection: collection),
            "dateAdded": (collection.startDate ?? Date()).timeIntervalSince1970 * 1000,
            "year": collection.startDate?.year
          ])
        }
      }
    }

    return applyPagination(applySorting(items, sort: sort), pagination: pagination)
  }

  // MARK: - Artists

  func getArtists(sort: Any?, pagination: Any?) -> [[String: Any?]] {
    let fetchOptions = PHFetchOptions()
    fetchOptions.predicate = NSPredicate(format: "mediaType == %d", PHAssetMediaType.audio.rawValue)

    let results = PHAsset.fetchAssets(with: .audio, options: fetchOptions)
    var artistMap: [String: [String: Any?]] = [:]

    results.enumerateObjects { asset, _, _ in
      let artist = self.getAudioArtist(for: asset) ?? "Unknown Artist"
      if var existing = artistMap[artist] {
        existing["songCount"] = (existing["songCount"] as? Int ?? 0) + 1
        artistMap[artist] = existing
      } else {
        artistMap[artist] = [
          "id": artist,
          "name": artist,
          "albumCount": 0,
          "songCount": 1,
          "duration": asset.duration,
          "dateAdded": asset.creationDate?.timeIntervalSince1970 ?? 0
        ]
      }
    }

    let items = Array(artistMap.values)
    return applyPagination(applySorting(items, sort: sort), pagination: pagination)
  }

  // MARK: - Genres

  func getGenres(sort: Any?, pagination: Any?) -> [[String: Any?]] {
    let fetchOptions = PHFetchOptions()
    fetchOptions.predicate = NSPredicate(format: "mediaType == %d", PHAssetMediaType.audio.rawValue)

    let results = PHAsset.fetchAssets(with: .audio, options: fetchOptions)
    var genreMap: [String: [String: Any?]] = [:]

    results.enumerateObjects { asset, _, _ in
      let genre = self.getAudioGenre(for: asset) ?? "Unknown"
      if var existing = genreMap[genre] {
        existing["songCount"] = (existing["songCount"] as? Int ?? 0) + 1
        genreMap[genre] = existing
      } else {
        genreMap[genre] = [
          "id": genre,
          "name": genre,
          "songCount": 1,
          "duration": asset.duration
        ]
      }
    }

    let items = Array(genreMap.values)
    return applyPagination(applySorting(items, sort: sort), pagination: pagination)
  }

  // MARK: - Playlists

  func getPlaylists(sort: Any?, pagination: Any?) -> [[String: Any?]] {
    let collections = PHAssetCollection.fetchAssetCollections(with: .smartAlbum, subtype: .smartAlbumPlaylists, options: nil)
    var items: [[String: Any?]] = []

    collections.enumerateObjects { collection, _, _ in
      let assets = PHAsset.fetchAssets(in: collection, options: nil)
      items.append([
        "id": collection.localIdentifier,
        "name": collection.localizedTitle ?? "",
        "songCount": assets.count,
        "duration": self.calculateTotalDuration(assets: assets),
        "dateAdded": (collection.startDate ?? Date()).timeIntervalSince1970 * 1000,
        "dateModified": (collection.endDate ?? Date()).timeIntervalSince1970 * 1000
      ])
    }

    return applyPagination(applySorting(items, sort: sort), pagination: pagination)
  }

  // MARK: - Folders

  func getFolders(sort: Any?, filter: Any?, pagination: Any?) -> [[String: Any?]] {
    var items: [[String: Any?]] = []

    let albums = PHAssetCollection.fetchAssetCollections(with: .smartAlbum, subtype: .any, options: nil)
    albums.enumerateObjects { collection, _, _ in
      let assets = PHAsset.fetchAssets(in: collection, options: nil)
      if assets.count > 0 {
        let path = collection.localizedTitle ?? ""
        items.append([
          "id": collection.localIdentifier,
          "name": path,
          "path": path,
          "fileCount": assets.count,
          "totalSize": self.calculateTotalSize(assets: assets)
        ])
      }
    }

    return applyPagination(applySorting(items, sort: sort), pagination: pagination)
  }

  // MARK: - Search

  func search(options: [String: Any?]) -> [String: Any?] {
    let query = (options["query"] as? String ?? "").trimmingCharacters(in: .whitespacesAndNewlines)
    guard !query.isEmpty else {
      return ["audio": [], "videos": [], "images": [], "documents": [], "totalCount": 0, "query": query]
    }

    let types = options["types"] as? [String] ?? ["audio", "video", "image", "document"]

    var audioItems: [[String: Any?]] = []
    var videoItems: [[String: Any?]] = []
    var imageItems: [[String: Any?]] = []

    if types.contains("audio") {
      let fetchOptions = PHFetchOptions()
      fetchOptions.predicate = NSPredicate(format: "mediaType == %d AND (title CONTAINS[cd] %@ OR (SUBSTRING(localizedTitle, 1, %d) ==[cd] %@))",
        PHAssetMediaType.audio.rawValue, query, query.count, query)
      let results = PHAsset.fetchAssets(with: .audio, options: fetchOptions)
      results.enumerateObjects { asset, _, _ in
        let metadata = self.getAudioMetadata(for: asset)
        audioItems.append(self.mapAudioAsset(asset, metadata: metadata))
      }
    }

    if types.contains("video") {
      let fetchOptions = PHFetchOptions()
      fetchOptions.predicate = NSPredicate(format: "mediaType == %d AND title CONTAINS[cd] %@",
        PHAssetMediaType.video.rawValue, query)
      let results = PHAsset.fetchAssets(with: .video, options: fetchOptions)
      results.enumerateObjects { asset, _, _ in
        videoItems.append(self.mapVideoAsset(asset))
      }
    }

    if types.contains("image") {
      let fetchOptions = PHFetchOptions()
      fetchOptions.predicate = NSPredicate(format: "mediaType == %d AND title CONTAINS[cd] %@",
        PHAssetMediaType.image.rawValue, query)
      let results = PHAsset.fetchAssets(with: .image, options: fetchOptions)
      results.enumerateObjects { asset, _, _ in
        let metadata = self.getImageMetadata(for: asset)
        imageItems.append(self.mapImageAsset(asset, metadata: metadata))
      }
    }

    let totalCount = audioItems.count + videoItems.count + imageItems.count

    return [
      "audio": audioItems,
      "videos": videoItems,
      "images": imageItems,
      "documents": [],
      "totalCount": totalCount,
      "query": query
    ]
  }

  // MARK: - Lookups

  func getById(mediaType: String, id: String) -> [String: Any?]? {
    let fetchResult = PHAsset.fetchAssets(withLocalIdentifiers: [id], options: nil)
    guard let asset = fetchResult.firstObject else { return nil }

    switch mediaType {
    case "audio":
      let metadata = getAudioMetadata(for: asset)
      return mapAudioAsset(asset, metadata: metadata)
    case "video":
      return mapVideoAsset(asset)
    case "image":
      let metadata = getImageMetadata(for: asset)
      return mapImageAsset(asset, metadata: metadata)
    default:
      return nil
    }
  }

  func getByUri(uri: String) -> [String: Any?]? {
    // Try to fetch by local identifier
    let fetchResult = PHAsset.fetchAssets(withLocalIdentifiers: [uri], options: nil)
    guard let asset = fetchResult.firstObject else { return nil }

    switch asset.mediaType {
    case .audio:
      let metadata = getAudioMetadata(for: asset)
      return mapAudioAsset(asset, metadata: metadata)
    case .video:
      return mapVideoAsset(asset)
    case .image:
      let metadata = getImageMetadata(for: asset)
      return mapImageAsset(asset, metadata: metadata)
    default:
      return nil
    }
  }

  // MARK: - Recent

  func getRecent(mediaType: String?, limit: Int?) -> [[String: Any?]] {
    let actualLimit = limit ?? 50
    let fetchOptions = PHFetchOptions()
    fetchOptions.sortDescriptors = [NSSortDescriptor(key: "creationDate", ascending: false)]

    if let type = mediaType {
      let assetType = mapMediaType(type)
      fetchOptions.predicate = NSPredicate(format: "mediaType == %d", assetType.rawValue)
    }

    let results = PHAsset.fetchAssets(with: fetchOptions)
    var items: [[String: Any?]] = []

    let count = min(results.count, actualLimit)
    for i in 0..<count {
      let asset = results.object(at: i)
      switch asset.mediaType {
      case .audio:
        let metadata = getAudioMetadata(for: asset)
        items.append(mapAudioAsset(asset, metadata: metadata))
      case .video:
        items.append(mapVideoAsset(asset))
      case .image:
        let metadata = getImageMetadata(for: asset)
        items.append(mapImageAsset(asset, metadata: metadata))
      default:
        break
      }
    }

    return items
  }

  // MARK: - Favorites

  func getFavorites(mediaType: String?, sort: Any?, pagination: Any?) -> [[String: Any?]] {
    let fetchOptions = PHFetchOptions()
    fetchOptions.predicate = NSPredicate(format: "favorite == YES")

    if let type = mediaType {
      let assetType = mapMediaType(type)
      fetchOptions.predicate = NSPredicate(format: "favorite == YES AND mediaType == %d", assetType.rawValue)
    }

    applySortOptions(fetchOptions, sort: sort, mediaType: .audio)

    let results = PHAsset.fetchAssets(with: fetchOptions)
    var items: [[String: Any?]] = []

    results.enumerateObjects { asset, _, _ in
      switch asset.mediaType {
      case .audio:
        let metadata = self.getAudioMetadata(for: asset)
        items.append(self.mapAudioAsset(asset, metadata: metadata))
      case .video:
        items.append(self.mapVideoAsset(asset))
      case .image:
        let metadata = self.getImageMetadata(for: asset)
        items.append(self.mapImageAsset(asset, metadata: metadata))
      default:
        break
      }
    }

    return applyPagination(items, pagination: pagination)
  }

  // MARK: - Largest Files

  func getLargestFiles(mediaType: String?, limit: Int?) -> [[String: Any?]] {
    let actualLimit = limit ?? 50
    let fetchOptions = PHFetchOptions()

    if let type = mediaType {
      let assetType = mapMediaType(type)
      fetchOptions.predicate = NSPredicate(format: "mediaType == %d", assetType.rawValue)
    }

    let results = PHAsset.fetchAssets(with: fetchOptions)
    var items: [(Double, [String: Any?])] = []

    results.enumerateObjects { asset, _, _ in
      let size = self.getAssetFileSize(asset)
      var item: [String: Any?]

      switch asset.mediaType {
      case .audio:
        let metadata = self.getAudioMetadata(for: asset)
        item = self.mapAudioAsset(asset, metadata: metadata)
      case .video:
        item = self.mapVideoAsset(asset)
      case .image:
        let metadata = self.getImageMetadata(for: asset)
        item = self.mapImageAsset(asset, metadata: metadata)
      default:
        return
      }

      items.append((size, item))
    }

    return Array(items.sorted { $0.0 > $1.0 }
      .prefix(actualLimit)
      .map { $0.1 })
  }

  // MARK: - Duplicates

  func getDuplicates(mediaType: String?) -> [[String: Any?]] {
    let fetchOptions = PHFetchOptions()
    if let type = mediaType {
      let assetType = mapMediaType(type)
      fetchOptions.predicate = NSPredicate(format: "mediaType == %d", assetType.rawValue)
    }

    let results = PHAsset.fetchAssets(with: fetchOptions)
    var sizeGroups: [Double: [(String, PHAsset)]] = [:]

    results.enumerateObjects { asset, _, _ in
      let size = self.getAssetFileSize(asset)
      if size > 0 {
        if sizeGroups[size] == nil {
          sizeGroups[size] = []
        }
        sizeGroups[size]!.append((asset.localIdentifier, asset))
      }
    }

    var duplicates: [[String: Any?]] = []

    for (_, group) in sizeGroups where group.count >= 1 {
      if group.count >= 2 {
        let hash = group[0].0
        duplicates.append([
          "fileHash": hash,
          "count": group.count,
          "items": [],
          "totalSize": Double(group.count) * Double(group[0].1)
        ])
      }
    }

    return duplicates
  }

  // MARK: - Statistics

  func getStatistics() -> [String: Any?] {
    let audioResult = PHAsset.fetchAssets(with: .audio, options: nil)
    let videoResult = PHAsset.fetchAssets(with: .video, options: nil)
    let imageResult = PHAsset.fetchAssets(with: .image, options: nil)

    var totalDuration: Double = 0

    audioResult.enumerateObjects { asset, _, _ in
      totalDuration += asset.duration
    }

    videoResult.enumerateObjects { asset, _, _ in
      totalDuration += asset.duration
    }

    return [
      "totalAudio": audioResult.count,
      "totalVideo": videoResult.count,
      "totalImages": imageResult.count,
      "totalDocuments": 0,
      "totalSize": calculateTotalSize(assets: audioResult) + calculateTotalSize(assets: videoResult) + calculateTotalSize(assets: imageResult),
      "totalDuration": totalDuration * 1000
    ]
  }

  // MARK: - Artwork

  func getAlbumArtwork(albumId: String) -> String? {
    let fetchResult = PHAssetCollection.fetchAssetCollections(withLocalIdentifiers: [albumId], options: nil)
    guard let collection = fetchResult.firstObject else { return nil }

    let assets = PHAsset.fetchAssets(in: collection, options: nil)
    guard let firstAsset = assets.firstObject else { return nil }

    let options = PHImageRequestOptions()
    options.synchronous = true
    options.deliveryMode = .fastFormat
    options.resizeMode = .fast

    var resultUri: String?

    PHImageManager.default().requestImage(
      for: firstAsset,
      targetSize: CGSize(width: 300, height: 300),
      contentMode: .aspectFill,
      options: options
    ) { image, _ in
      if let image = image, let data = image.jpegData(compressionQuality: 0.8) {
        let tempDir = FileManager.default.temporaryDirectory
        let fileUrl = tempDir.appendingPathComponent("artwork_\(albumId).jpg")
        try? data.write(to: fileUrl)
        resultUri = fileUrl.absoluteString
      }
    }

    return resultUri
  }

  // MARK: - Thumbnails

  func getThumbnail(assetId: String, mediaType: PHAssetMediaType, width: Int?, height: Int?) -> String? {
    let fetchResult = PHAsset.fetchAssets(withLocalIdentifiers: [assetId], options: nil)
    guard let asset = fetchResult.firstObject else { return nil }

    let targetWidth = CGFloat(width ?? 320)
    let targetHeight = CGFloat(height ?? 240)

    let options = PHImageRequestOptions()
    options.synchronous = true
    options.deliveryMode = .fastFormat
    options.resizeMode = .fast

    var resultUri: String?

    PHImageManager.default().requestImage(
      for: asset,
      targetSize: CGSize(width: targetWidth, height: targetHeight),
      contentMode: .aspectFill,
      options: options
    ) { image, _ in
      if let image = image, let data = image.jpegData(compressionQuality: 0.85) {
        let tempDir = FileManager.default.temporaryDirectory
        let typeName = mediaType == .video ? "video" : "image"
        let fileUrl = tempDir.appendingPathComponent("\(typeName)_\(assetId)_\(Int(targetWidth))x\(Int(targetHeight)).jpg")
        try? data.write(to: fileUrl)
        resultUri = fileUrl.absoluteString
      }
    }

    return resultUri
  }

  // MARK: - Private Helpers

  private func mapAudioAsset(_ asset: PHAsset, metadata: [String: Any?]?) -> [String: Any?] {
    var uri: String?
    let resources = PHAssetResource.assetResources(for: asset)
    if let resource = resources.first {
      uri = resource.originalFilename
    }

    return [
      "id": asset.localIdentifier,
      "uri": uri ?? "",
      "title": asset.value(forKey: "filename") as? String ?? "",
      "artist": metadata?["artist"] as? String ?? "",
      "album": metadata?["album"] as? String ?? "",
      "albumId": metadata?["albumId"] as? String ?? "",
      "genre": metadata?["genre"] as? String,
      "duration": Int(asset.duration * 1000),
      "size": getAssetFileSize(asset),
      "trackNumber": metadata?["trackNumber"] as? Int ?? 0,
      "discNumber": metadata?["discNumber"] as? Int ?? 0,
      "year": asset.creationDate?.year ?? 0,
      "dateAdded": (asset.creationDate ?? Date()).timeIntervalSince1970 * 1000,
      "dateModified": (asset.modificationDate ?? Date()).timeIntervalSince1970 * 1000,
      "composer": metadata?["composer"] as? String,
      "lyrics": metadata?["lyrics"] as? String,
      "albumArtist": metadata?["albumArtist"] as? String,
      "isFavorite": asset.isFavorite,
      "playCount": 0,
      "lastPlayed": 0,
      "bookmark": 0,
      "bitrate": metadata?["bitrate"] as? Int,
      "sampleRate": metadata?["sampleRate"] as? Int,
      "channels": metadata?["channels"] as? Int,
      "encoding": nil,
      "mimeType": metadata?["mimeType"] as? String ?? "",
      "fileExtension": uri?.pathExtension ?? "",
      "relativePath": "",
      "displayName": uri ?? "",
      "contentUri": asset.localIdentifier
    ]
  }

  private func mapVideoAsset(_ asset: PHAsset) -> [String: Any?] {
    var uri: String?
    let resources = PHAssetResource.assetResources(for: asset)
    if let resource = resources.first {
      uri = resource.originalFilename
    }

    return [
      "id": asset.localIdentifier,
      "uri": uri ?? "",
      "title": asset.value(forKey: "filename") as? String ?? "",
      "duration": Int(asset.duration * 1000),
      "width": asset.pixelWidth,
      "height": asset.pixelHeight,
      "frameRate": nil,
      "rotation": 0,
      "size": getAssetFileSize(asset),
      "mimeType": getMimeType(for: asset),
      "relativePath": "",
      "displayName": uri ?? "",
      "dateAdded": (asset.creationDate ?? Date()).timeIntervalSince1970 * 1000,
      "dateModified": (asset.modificationDate ?? Date()).timeIntervalSince1970 * 1000,
      "resolution": "\(asset.pixelWidth)x\(asset.pixelHeight)",
      "orientation": 0
    ]
  }

  private func mapImageAsset(_ asset: PHAsset, metadata: [String: Any?]?) -> [String: Any?] {
    var uri: String?
    let resources = PHAssetResource.assetResources(for: asset)
    if let resource = resources.first {
      uri = resource.originalFilename
    }

    return [
      "id": asset.localIdentifier,
      "uri": uri ?? "",
      "title": asset.value(forKey: "filename") as? String ?? "",
      "width": asset.pixelWidth,
      "height": asset.pixelHeight,
      "orientation": metadata?["orientation"] as? Int ?? 0,
      "cameraMake": metadata?["cameraMake"] as? String,
      "cameraModel": metadata?["cameraModel"] as? String,
      "dateTaken": (asset.creationDate ?? Date()).timeIntervalSince1970 * 1000,
      "gpsLatitude": metadata?["latitude"] as? Double,
      "gpsLongitude": metadata?["longitude"] as? Double,
      "mimeType": getMimeType(for: asset),
      "size": getAssetFileSize(asset),
      "relativePath": "",
      "displayName": uri ?? "",
      "dateAdded": (asset.creationDate ?? Date()).timeIntervalSince1970 * 1000,
      "dateModified": (asset.modificationDate ?? Date()).timeIntervalSince1970 * 1000
    ]
  }

  private func getAudioMetadata(for asset: PHAsset) -> [String: Any?] {
    var metadata: [String: Any?] = [:]

    let resources = PHAssetResource.assetResources(for: asset)
    if let resource = resources.first {
      metadata["mimeType"] = resource.uniformTypeIdentifier
    }

    return metadata
  }

  private func getImageMetadata(for asset: PHAsset) -> [String: Any?] {
    var metadata: [String: Any?] = [:]
    metadata["latitude"] = asset.location?.coordinate.latitude
    metadata["longitude"] = asset.location?.coordinate.longitude
    return metadata
  }

  private func getAudioArtist(for asset: PHAsset) -> String? {
    return asset.value(forKey: "filename") as? String
  }

  private func getAudioGenre(for asset: PHAsset) -> String? {
    return nil
  }

  private func getMimeType(for asset: PHAsset) -> String {
    let resources = PHAssetResource.assetResources(for: asset)
    guard let resource = resources.first else { return "" }
    return resource.uniformTypeIdentifier
  }

  private func getAssetFileSize(_ asset: PHAsset) -> Double {
    let resources = PHAssetResource.assetResources(for: asset)
    guard let resource = resources.first else { return 0 }
    return Double(resource.fileSize)
  }

  private func calculateTotalSize(assets: PHFetchResult<PHAsset>) -> Double {
    var totalSize: Double = 0
    assets.enumerateObjects { asset, _, _ in
      totalSize += self.getAssetFileSize(asset)
    }
    return totalSize
  }

  private func calculateTotalDuration(assets: PHFetchResult<PHAsset>) -> Double {
    var totalDuration: Double = 0
    assets.enumerateObjects { asset, _, _ in
      totalDuration += asset.duration
    }
    return totalDuration * 1000
  }

  private func getAlbumArtworkUri(collection: PHAssetCollection) -> String? {
    let assets = PHAsset.fetchAssets(in: collection, options: nil)
    guard let firstAsset = assets.firstObject else { return nil }
    return firstAsset.localIdentifier
  }

  private func mapMediaType(_ type: String) -> PHAssetMediaType {
    switch type {
    case "audio": return .audio
    case "video": return .video
    case "image": return .image
    default: return .unknown
    }
  }

  private func applyFilterOptions(_ options: PHFetchOptions, filter: Any?) {
    guard let filterDict = filter as? [String: Any?] else { return }

    if let startDate = filterDict["startDate"] as? Double {
      let date = Date(timeIntervalSince1970: startDate / 1000)
      options.predicate = NSPredicate(format: "creationDate >= %@", date as NSDate)
    }
    if let endDate = filterDict["endDate"] as? Double {
      let date = Date(timeIntervalSince1970: endDate / 1000)
      if let existing = options.predicate {
        options.predicate = NSCompoundPredicate(andPredicateWithSubpredicates: [
          existing,
          NSPredicate(format: "creationDate <= %@", date as NSDate)
        ])
      } else {
        options.predicate = NSPredicate(format: "creationDate <= %@", date as NSDate)
      }
    }
  }

  private func applySortOptions(_ options: PHFetchOptions, sort: Any?, mediaType: PHAssetMediaType) {
    guard let sortDict = sort as? [String: Any?] else { return }

    let field = sortDict["field"] as? String ?? "name"
    let ascending = (sortDict["order"] as? String ?? "asc") == "asc"

    let sortKey: String
    switch field {
    case "dateAdded": sortKey = "creationDate"
    case "dateModified": sortKey = "modificationDate"
    case "name": sortKey = "filename"
    case "duration": sortKey = "duration"
    default: sortKey = "creationDate"
    }

    options.sortDescriptors = [NSSortDescriptor(key: sortKey, ascending: ascending)]
  }

  private func applySorting(_ items: [[String: Any?]], sort: Any?) -> [[String: Any?]] {
    guard let sortDict = sort as? [String: Any?] else { return items }

    let field = sortDict["field"] as? String ?? "name"
    let ascending = (sortDict["order"] as? String ?? "asc") == "asc"

    return items.sorted { a, b in
      let aVal = a[field] ?? ""
      let bVal = b[field] ?? ""

      if let aNum = aVal as? Double, let bNum = bVal as? Double {
        return ascending ? aNum < bNum : aNum > bNum
      }
      if let aStr = aVal as? String, let bStr = bVal as? String {
        return ascending ? aStr < bStr : aStr > bStr
      }
      return false
    }
  }

  private func applyPagination(_ items: [[String: Any?]], pagination: Any?) -> [[String: Any?]] {
    guard let paginationDict = pagination as? [String: Any?] else { return items }

    var result = items

    if let offset = paginationDict["offset"] as? Int, offset > 0 {
      result = Array(result.dropFirst(offset))
    }

    if let limit = paginationDict["limit"] as? Int, limit > 0 {
      result = Array(result.prefix(limit))
    }

    return result
  }
}

// MARK: - Date Extension

private extension Date {
  var year: Int {
    return Calendar.current.component(.year, from: self)
  }
}
