import Photos

class MediaStorePermissions {
  func checkStatus() -> [String: Any?] {
    let status = PHPhotoLibrary.authorizationStatus(for: .readWrite)

    return [
      "granted": status == .authorized || status == .limited,
      "audio": status == .authorized || status == .limited,
      "video": status == .authorized || status == .limited,
      "images": status == .authorized || status == .limited
    ]
  }

  func request() async -> [String: Any?] {
    let currentStatus = PHPhotoLibrary.authorizationStatus(for: .readWrite)

    if currentStatus == .authorized || currentStatus == .limited {
      return checkStatus()
    }

    let status = await PHPhotoLibrary.requestAuthorization(for: .readWrite)

    return [
      "granted": status == .authorized || status == .limited,
      "audio": status == .authorized || status == .limited,
      "video": status == .authorized || status == .limited,
      "images": status == .authorized || status == .limited
    ]
  }
}
