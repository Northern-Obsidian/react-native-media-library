import ExpoModulesCore

public class MediaStoreModule: Module {
  public func definition() -> ModuleDefinition {
    Name("MediaStore")

    Events("onMediaChange")

    AsyncFunction("getAudio") { (sort: [String: Any]?, filter: [String: Any]?, pagination: [String: Any]?) -> [[String: Any]] in
      return []
    }

    AsyncFunction("getVideos") { (sort: [String: Any]?, filter: [String: Any]?, pagination: [String: Any]?) -> [[String: Any]] in
      return []
    }

    AsyncFunction("getImages") { (sort: [String: Any]?, filter: [String: Any]?, pagination: [String: Any]?) -> [[String: Any]] in
      return []
    }

    AsyncFunction("getDocuments") { (sort: [String: Any]?, filter: [String: Any]?, pagination: [String: Any]?) -> [[String: Any]] in
      return []
    }

    AsyncFunction("getAlbums") { (sort: [String: Any]?, filter: [String: Any]?, pagination: [String: Any]?) -> [[String: Any]] in
      return []
    }

    AsyncFunction("getArtists") { (sort: [String: Any]?, pagination: [String: Any]?) -> [[String: Any]] in
      return []
    }

    AsyncFunction("getGenres") { (sort: [String: Any]?, pagination: [String: Any]?) -> [[String: Any]] in
      return []
    }

    AsyncFunction("getPlaylists") { (sort: [String: Any]?, pagination: [String: Any]?) -> [[String: Any]] in
      return []
    }

    AsyncFunction("getFolders") { (sort: [String: Any]?, filter: [String: Any]?, pagination: [String: Any]?) -> [[String: Any]] in
      return []
    }

    AsyncFunction("search") { (options: [String: Any]) -> [String: Any] in
      return [
        "audio": [],
        "videos": [],
        "images": [],
        "documents": [],
        "totalCount": 0,
        "query": options["query"] as? String ?? ""
      ]
    }

    AsyncFunction("getById") { (mediaType: String, id: String) -> [String: Any]? in
      return nil
    }

    AsyncFunction("getByUri") { (uri: String) -> [String: Any]? in
      return nil
    }

    AsyncFunction("getRecent") { (mediaType: String?, limit: Int?) -> [[String: Any]] in
      return []
    }

    AsyncFunction("getFavorites") { (mediaType: String?, sort: [String: Any]?, pagination: [String: Any]?) -> [[String: Any]] in
      return []
    }

    AsyncFunction("getLargestFiles") { (mediaType: String?, limit: Int?) -> [[String: Any]] in
      return []
    }

    AsyncFunction("getDuplicates") { (mediaType: String?) -> [[String: Any]] in
      return []
    }

    AsyncFunction("getStatistics") { () -> [String: Any] in
      return [
        "totalAudio": 0,
        "totalVideo": 0,
        "totalImages": 0,
        "totalDocuments": 0,
        "totalSize": 0,
        "totalDuration": 0
      ]
    }

    AsyncFunction("refresh") { () -> Void in
    }

    Function("checkPermissions") { () -> [String: Any] in
      return [
        "granted": false,
        "audio": false,
        "video": false,
        "images": false
      ]
    }

    AsyncFunction("requestPermissions") { () -> [String: Any] in
      return [
        "granted": false,
        "audio": false,
        "video": false,
        "images": false
      ]
    }
  }
}
