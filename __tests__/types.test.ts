import type {
  AudioItem,
  VideoItem,
  ImageItem,
  DocumentItem,
  Album,
  Artist,
  Genre,
  Playlist,
  Folder,
  SearchResult,
  SortOptions,
  FilterOptions,
  PaginationOptions,
  SearchOptions,
  MediaStoreStatistics,
  DuplicateItem,
  MediaChangeEvent,
  PermissionStatus,
  LibraryResult,
  ArtworkResult,
  ThumbnailOptions,
  SizeHistogram,
  FolderStatistics,
  IncrementalChanges,
  MetadataPlugin,
  LibraryQueryOptions,
  LibraryQueryResult,
} from "../src/MediaStoreModule.types";

import { SortOrder, SortField } from "../src/MediaStoreModule.types";

describe("MediaStoreModule types", () => {
  it("should export SortOrder enum with correct values", () => {
    expect(SortOrder.Ascending).toBe("asc");
    expect(SortOrder.Descending).toBe("desc");
  });

  it("should export SortField enum with correct values", () => {
    expect(SortField.Name).toBe("name");
    expect(SortField.DateAdded).toBe("dateAdded");
    expect(SortField.DateModified).toBe("dateModified");
    expect(SortField.Duration).toBe("duration");
    expect(SortField.Artist).toBe("artist");
    expect(SortField.Album).toBe("album");
    expect(SortField.Year).toBe("year");
    expect(SortField.FileSize).toBe("fileSize");
    expect(SortField.Resolution).toBe("resolution");
    expect(SortField.Width).toBe("width");
    expect(SortField.Height).toBe("height");
  });

  it("should have correct AudioItem interface shape", () => {
    const audio: AudioItem = {
      id: "1",
      uri: "file:///test.mp3",
      title: "Test Song",
      artist: "Test Artist",
      album: "Test Album",
      albumId: "1",
      genre: "Rock",
      duration: 180000,
      size: 5000000,
      trackNumber: 1,
      discNumber: 1,
      year: 2024,
      dateAdded: 1700000000000,
      dateModified: 1700000000000,
      composer: null,
      lyrics: null,
      albumArtist: null,
      isFavorite: false,
      playCount: 0,
      lastPlayed: 0,
      bookmark: 0,
      bitrate: 320000,
      sampleRate: 44100,
      channels: 2,
      encoding: null,
      mimeType: "audio/mpeg",
      fileExtension: "mp3",
      relativePath: "Music/",
      displayName: "Test Song.mp3",
      contentUri: "content://media/external/audio/media/1",
    };
    expect(audio.id).toBe("1");
    expect(audio.title).toBe("Test Song");
    expect(audio.duration).toBe(180000);
  });

  it("should have correct VideoItem interface shape", () => {
    const video: VideoItem = {
      id: "1",
      uri: "file:///test.mp4",
      title: "Test Video",
      duration: 60000,
      width: 1920,
      height: 1080,
      frameRate: 30,
      rotation: 0,
      size: 50000000,
      mimeType: "video/mp4",
      relativePath: "Movies/",
      displayName: "Test Video.mp4",
      dateAdded: 1700000000000,
      dateModified: 1700000000000,
      resolution: "1920x1080",
      orientation: 1,
    };
    expect(video.id).toBe("1");
    expect(video.width).toBe(1920);
    expect(video.height).toBe(1080);
  });

  it("should have correct ImageItem interface shape", () => {
    const image: ImageItem = {
      id: "1",
      uri: "file:///test.jpg",
      title: "Test Image",
      width: 4000,
      height: 3000,
      orientation: 1,
      cameraMake: "Canon",
      cameraModel: "EOS R5",
      dateTaken: 1700000000000,
      gpsLatitude: 37.7749,
      gpsLongitude: -122.4194,
      mimeType: "image/jpeg",
      size: 10000000,
      relativePath: "DCIM/",
      displayName: "Test Image.jpg",
      dateAdded: 1700000000000,
      dateModified: 1700000000000,
    };
    expect(image.id).toBe("1");
    expect(image.cameraMake).toBe("Canon");
    expect(image.gpsLatitude).toBe(37.7749);
  });

  it("should have correct DocumentItem interface shape", () => {
    const doc: DocumentItem = {
      id: "1",
      uri: "file:///test.pdf",
      name: "Test Document",
      size: 1000000,
      mimeType: "application/pdf",
      extension: "pdf",
      relativePath: "Documents/",
      dateAdded: 1700000000000,
      dateModified: 1700000000000,
    };
    expect(doc.id).toBe("1");
    expect(doc.extension).toBe("pdf");
  });

  it("should have correct SortOptions interface shape", () => {
    const sort: SortOptions = {
      field: SortField.Name,
      order: SortOrder.Ascending,
    };
    expect(sort.field).toBe("name");
    expect(sort.order).toBe("asc");
  });

  it("should have correct FilterOptions interface shape", () => {
    const filter: FilterOptions = {
      mimeTypes: ["audio/mpeg", "audio/flac"],
      extensions: ["mp3", "flac"],
      folder: "Music/",
      album: "Test Album",
      artist: "Test Artist",
      minDuration: 60000,
      maxDuration: 300000,
      minSize: 1000000,
      maxSize: 10000000,
      startDate: 1700000000000,
      endDate: 1700000000000,
      favoritesOnly: true,
    };
    expect(filter.mimeTypes).toHaveLength(2);
    expect(filter.favoritesOnly).toBe(true);
  });

  it("should have correct PaginationOptions interface shape", () => {
    const pagination: PaginationOptions = {
      limit: 20,
      offset: 0,
      cursor: "abc123",
    };
    expect(pagination.limit).toBe(20);
    expect(pagination.offset).toBe(0);
  });

  it("should have correct MediaStoreStatistics interface shape", () => {
    const stats: MediaStoreStatistics = {
      totalAudio: 100,
      totalVideo: 50,
      totalImages: 200,
      totalDocuments: 30,
      totalSize: 1000000000,
      totalDuration: 3600000,
    };
    expect(stats.totalAudio).toBe(100);
    expect(stats.totalSize).toBe(1000000000);
  });

  it("should have correct DuplicateItem interface shape", () => {
    const dup: DuplicateItem = {
      fileHash: "abc123",
      count: 3,
      items: [],
      totalSize: 15000000,
    };
    expect(dup.count).toBe(3);
    expect(dup.items).toHaveLength(0);
  });

  it("should have correct PermissionStatus interface shape", () => {
    const perm: PermissionStatus = {
      granted: true,
      audio: true,
      video: true,
      images: true,
    };
    expect(perm.granted).toBe(true);
  });

  it("should have correct MediaChangeEvent interface shape", () => {
    const event: MediaChangeEvent = {
      type: "added",
      mediaType: "audio",
      itemId: "1",
      uri: "content://media/external/audio/media/1",
    };
    expect(event.type).toBe("added");
    expect(event.mediaType).toBe("audio");
  });

  it("should have correct LibraryResult interface shape", () => {
    const lib: LibraryResult = {
      audio: [],
      videos: [],
      images: [],
      documents: [],
      totalCount: 0,
    };
    expect(lib.totalCount).toBe(0);
  });

  it("should have correct ThumbnailOptions interface shape", () => {
    const thumb: ThumbnailOptions = {
      width: 320,
      height: 240,
      kind: "MINI_KIND",
    };
    expect(thumb.kind).toBe("MINI_KIND");
  });

  it("should have correct SizeHistogram interface shape", () => {
    const hist: SizeHistogram = {
      lessThan1MB: 10,
      from1to10MB: 5,
      from10to100MB: 3,
      from100MBto1GB: 1,
      greaterThan1GB: 0,
    };
    expect(hist.lessThan1MB).toBe(10);
    expect(hist.greaterThan1GB).toBe(0);
  });

  it("should have correct FolderStatistics interface shape", () => {
    const stats: FolderStatistics = {
      id: "abc",
      name: "Music",
      path: "Music/",
      fileCount: 150,
      totalSize: 500000000,
      histogram: {
        lessThan1MB: 10,
        from1to10MB: 50,
        from10to100MB: 80,
        from100MBto1GB: 10,
        greaterThan1GB: 0,
      },
      mediaTypeBreakdown: {
        audio: 140,
        video: 5,
        image: 3,
        document: 2,
      },
      averageFileSize: 3333333,
    };
    expect(stats.fileCount).toBe(150);
    expect(stats.histogram.from10to100MB).toBe(80);
    expect(stats.mediaTypeBreakdown.audio).toBe(140);
  });

  it("should have correct IncrementalChanges interface shape", () => {
    const changes: IncrementalChanges = {
      added: 5,
      modified: 2,
      removed: 1,
      timestamp: 1700000000000,
    };
    expect(changes.added).toBe(5);
    expect(changes.removed).toBe(1);
  });

  it("should have correct MetadataPlugin interface shape", () => {
    const plugin: MetadataPlugin = {
      id: "test-plugin",
      name: "Test Plugin",
      version: "1.0.0",
      extract: (item) => ({ customField: `processed_${item.id}` }),
    };
    expect(plugin.id).toBe("test-plugin");
    expect(plugin.extract({ id: "1" } as AudioItem)).toEqual({
      customField: "processed_1",
    });
  });

  it("should have correct LibraryQueryOptions interface shape", () => {
    const opts: LibraryQueryOptions = {
      sort: { field: SortField.DateAdded, order: SortOrder.Descending },
      types: ["audio", "video"],
      typePagination: {
        audio: { limit: 50, offset: 0 },
        video: { limit: 20, offset: 0 },
      },
      includeStatistics: true,
    };
    expect(opts.types).toHaveLength(2);
    expect(opts.includeStatistics).toBe(true);
  });

  it("should have correct LibraryQueryResult interface shape", () => {
    const result: LibraryQueryResult = {
      audio: [],
      videos: [],
      images: [],
      documents: [],
      totalCount: 100,
      totalSize: 500000000,
      perTypeStatistics: {
        audio: { count: 80, totalSize: 400000000, totalDuration: 3600000 },
        video: { count: 15, totalSize: 80000000, totalDuration: 1200000 },
        image: { count: 5, totalSize: 20000000 },
        document: { count: 0, totalSize: 0 },
      },
      queryTime: 150,
    };
    expect(result.totalCount).toBe(100);
    expect(result.queryTime).toBe(150);
    expect(result.perTypeStatistics?.audio.count).toBe(80);
  });

  it("should support AudioItem with customMetadata", () => {
    const audio: AudioItem = {
      id: "1",
      uri: "file:///test.mp3",
      title: "Test Song",
      artist: "Test Artist",
      album: "Test Album",
      albumId: "1",
      genre: null,
      duration: 180000,
      size: 5000000,
      trackNumber: 1,
      discNumber: 1,
      year: 2024,
      dateAdded: 1700000000000,
      dateModified: 1700000000000,
      composer: null,
      lyrics: null,
      albumArtist: null,
      isFavorite: false,
      playCount: 0,
      lastPlayed: 0,
      bookmark: 0,
      bitrate: 320000,
      sampleRate: 44100,
      channels: 2,
      encoding: null,
      mimeType: "audio/mpeg",
      fileExtension: "mp3",
      relativePath: "Music/",
      displayName: "Test Song.mp3",
      contentUri: "content://media/external/audio/media/1",
      customMetadata: { myPlugin: { rating: 5 } },
    };
    expect(audio.customMetadata?.myPlugin).toEqual({ rating: 5 });
  });
});
