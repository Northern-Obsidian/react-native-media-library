import Photos

class MediaStoreObserver: NSObject, PHPhotoLibraryChangeObserver {
  static let shared = MediaStoreObserver()

  private var onChange: (([String: String]) -> Void)?
  private var isListening = false

  func startListening(onChange: @escaping ([String: String]) -> Void) {
    guard !isListening else { return }
    self.onChange = onChange
    PHPhotoLibrary.shared().register(self)
    isListening = true
  }

  func stopListening() {
    guard isListening else { return }
    PHPhotoLibrary.shared().unregisterChangeObserver(self)
    onChange = nil
    isListening = false
  }

  func photoLibraryDidChange(_ changeInstance: PHChange) {
    guard let onChange = onChange else { return }

    if let changeDetails = changeInstance.changeDetails(for: PHAsset.fetchOptions() as Any) {
      for insertedAsset in changeDetails.insertedAssets {
        let mediaType = mapMediaType(insertedAsset.mediaType)
        onChange([
          "type": "added",
          "mediaType": mediaType,
          "itemId": insertedAsset.localIdentifier,
          "uri": insertedAsset.localIdentifier
        ])
      }

      for removedAsset in changeDetails.removedAssets {
        let mediaType = mapMediaType(removedAsset.mediaType)
        onChange([
          "type": "removed",
          "mediaType": mediaType,
          "itemId": removedAsset.localIdentifier,
          "uri": removedAsset.localIdentifier
        ])
      }

      for changedAsset in changeDetails.changedAssets {
        let mediaType = mapMediaType(changedAsset.mediaType)
        onChange([
          "type": "modified",
          "mediaType": mediaType,
          "itemId": changedAsset.localIdentifier,
          "uri": changedAsset.localIdentifier
        ])
      }
    }
  }

  private func mapMediaType(_ type: PHAssetMediaType) -> String {
    switch type {
    case .audio: return "audio"
    case .video: return "video"
    case .image: return "image"
    default: return "document"
    }
  }
}
