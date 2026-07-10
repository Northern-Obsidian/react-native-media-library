import NativeModule from "./MediaStoreModule";
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
  MediaStoreError,
  LibraryResult,
  ArtworkResult,
  ThumbnailOptions,
  SizeHistogram,
  FolderStatistics,
  IncrementalChanges,
  MetadataPlugin,
  LibraryQueryOptions,
  LibraryQueryResult,
} from "./MediaStoreModule.types";
import { useEvent } from "expo";

export type {
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
  MediaStoreError,
  LibraryResult,
  ArtworkResult,
  ThumbnailOptions,
  SizeHistogram,
  FolderStatistics,
  IncrementalChanges,
  MetadataPlugin,
  LibraryQueryOptions,
  LibraryQueryResult,
};

export { SortOrder, SortField } from "./MediaStoreModule.types";

const registeredPlugins: Map<string, MetadataPlugin> = new Map();

export async function getAudio(
  sort?: SortOptions,
  filter?: FilterOptions,
  pagination?: PaginationOptions
): Promise<AudioItem[]> {
  const result = await NativeModule.getAudio(
    sort ?? null,
    filter ?? null,
    pagination ?? null
  );
  return applyPlugins(result) as AudioItem[];
}

export async function getVideos(
  sort?: SortOptions,
  filter?: FilterOptions,
  pagination?: PaginationOptions
): Promise<VideoItem[]> {
  const result = await NativeModule.getVideos(
    sort ?? null,
    filter ?? null,
    pagination ?? null
  );
  return applyPlugins(result) as VideoItem[];
}

export async function getImages(
  sort?: SortOptions,
  filter?: FilterOptions,
  pagination?: PaginationOptions
): Promise<ImageItem[]> {
  const result = await NativeModule.getImages(
    sort ?? null,
    filter ?? null,
    pagination ?? null
  );
  return applyPlugins(result) as ImageItem[];
}

export async function getDocuments(
  sort?: SortOptions,
  filter?: FilterOptions,
  pagination?: PaginationOptions
): Promise<DocumentItem[]> {
  const result = await NativeModule.getDocuments(
    sort ?? null,
    filter ?? null,
    pagination ?? null
  );
  return applyPlugins(result) as DocumentItem[];
}

export async function getAlbums(
  sort?: SortOptions,
  filter?: FilterOptions,
  pagination?: PaginationOptions
): Promise<Album[]> {
  return NativeModule.getAlbums(sort ?? null, filter ?? null, pagination ?? null);
}

export async function getArtists(
  sort?: SortOptions,
  pagination?: PaginationOptions
): Promise<Artist[]> {
  return NativeModule.getArtists(sort ?? null, pagination ?? null);
}

export async function getGenres(
  sort?: SortOptions,
  pagination?: PaginationOptions
): Promise<Genre[]> {
  return NativeModule.getGenres(sort ?? null, pagination ?? null);
}

export async function getPlaylists(
  sort?: SortOptions,
  pagination?: PaginationOptions
): Promise<Playlist[]> {
  return NativeModule.getPlaylists(sort ?? null, pagination ?? null);
}

export async function getFolders(
  sort?: SortOptions,
  filter?: FilterOptions,
  pagination?: PaginationOptions
): Promise<Folder[]> {
  return NativeModule.getFolders(sort ?? null, filter ?? null, pagination ?? null);
}

export async function search(
  options: SearchOptions
): Promise<SearchResult> {
  const result = await NativeModule.search(options);
  return {
    ...result,
    audio: applyPlugins(result.audio) as AudioItem[],
    videos: applyPlugins(result.videos) as VideoItem[],
    images: applyPlugins(result.images) as ImageItem[],
    documents: applyPlugins(result.documents) as DocumentItem[],
  };
}

export async function getById(
  mediaType: "audio" | "video" | "image" | "document",
  id: string
): Promise<AudioItem | VideoItem | ImageItem | DocumentItem | null> {
  const result = await NativeModule.getById(mediaType, id);
  return result ? applyPlugins([result])[0] : null;
}

export async function getByUri(uri: string): Promise<AudioItem | VideoItem | ImageItem | DocumentItem | null> {
  const result = await NativeModule.getByUri(uri);
  return result ? applyPlugins([result])[0] : null;
}

export async function getRecent(
  mediaType?: "audio" | "video" | "image" | "document",
  limit?: number
): Promise<(AudioItem | VideoItem | ImageItem | DocumentItem)[]> {
  const result = await NativeModule.getRecent(mediaType ?? null, limit ?? null);
  return applyPlugins(result);
}

export async function getFavorites(
  mediaType?: "audio" | "video" | "image" | "document",
  sort?: SortOptions,
  pagination?: PaginationOptions
): Promise<(AudioItem | VideoItem | ImageItem | DocumentItem)[]> {
  const result = await NativeModule.getFavorites(
    mediaType ?? null,
    sort ?? null,
    pagination ?? null
  );
  return applyPlugins(result);
}

export async function getLargestFiles(
  mediaType?: "audio" | "video" | "image" | "document",
  limit?: number
): Promise<(AudioItem | VideoItem | ImageItem | DocumentItem)[]> {
  const result = await NativeModule.getLargestFiles(mediaType ?? null, limit ?? null);
  return applyPlugins(result);
}

export async function getDuplicates(
  mediaType?: "audio" | "video" | "image" | "document"
): Promise<DuplicateItem[]> {
  return NativeModule.getDuplicates(mediaType ?? null);
}

export async function getStatistics(): Promise<MediaStoreStatistics> {
  return NativeModule.getStatistics();
}

export async function refresh(): Promise<void> {
  return NativeModule.refresh();
}

export async function checkPermissions(): Promise<PermissionStatus> {
  return NativeModule.checkPermissions();
}

export async function requestPermissions(): Promise<PermissionStatus> {
  return NativeModule.requestPermissions();
}

export function useMediaChangeEvent(
  onMediaChange?: (event: MediaChangeEvent) => void
): MediaChangeEvent | null {
  return useEvent(NativeModule, "onMediaChange", onMediaChange);
}

export async function getLibrary(
  sort?: SortOptions,
  filter?: FilterOptions,
  pagination?: PaginationOptions
): Promise<LibraryResult> {
  return NativeModule.getLibrary(
    sort ?? null,
    filter ?? null,
    pagination ?? null
  );
}

export async function getLibraryQuery(
  options?: LibraryQueryOptions
): Promise<LibraryQueryResult> {
  const startTime = Date.now();
  const opts = options ?? {};
  const types = opts.types ?? ["audio", "video", "image", "document"];
  const typePag = opts.typePagination ?? {};

  const results = await Promise.all([
    types.includes("audio")
      ? NativeModule.getAudio(
          opts.sort ?? null,
          opts.filter ?? null,
          typePag.audio ?? opts.pagination ?? null
        ).then((r: AudioItem[]) => applyPlugins(r) as AudioItem[])
      : Promise.resolve([] as AudioItem[]),
    types.includes("video")
      ? NativeModule.getVideos(
          opts.sort ?? null,
          opts.filter ?? null,
          typePag.video ?? opts.pagination ?? null
        ).then((r: VideoItem[]) => applyPlugins(r) as VideoItem[])
      : Promise.resolve([] as VideoItem[]),
    types.includes("image")
      ? NativeModule.getImages(
          opts.sort ?? null,
          opts.filter ?? null,
          typePag.image ?? opts.pagination ?? null
        ).then((r: ImageItem[]) => applyPlugins(r) as ImageItem[])
      : Promise.resolve([] as ImageItem[]),
    types.includes("document")
      ? NativeModule.getDocuments(
          opts.sort ?? null,
          opts.filter ?? null,
          typePag.document ?? opts.pagination ?? null
        ).then((r: DocumentItem[]) => applyPlugins(r) as DocumentItem[])
      : Promise.resolve([] as DocumentItem[]),
  ]);

  const [audio, videos, images, documents] = results;
  const totalCount = audio.length + videos.length + images.length + documents.length;
  const totalSize =
    audio.reduce((s: number, i: AudioItem) => s + i.size, 0) +
    videos.reduce((s: number, i: VideoItem) => s + i.size, 0) +
    images.reduce((s: number, i: ImageItem) => s + i.size, 0) +
    documents.reduce((s: number, i: DocumentItem) => s + i.size, 0);

  const result: LibraryQueryResult = {
    audio,
    videos,
    images,
    documents,
    totalCount,
    totalSize,
    queryTime: Date.now() - startTime,
  };

  if (opts.includeStatistics) {
    result.perTypeStatistics = {
      audio: {
        count: audio.length,
        totalSize: audio.reduce((s: number, i: AudioItem) => s + i.size, 0),
        totalDuration: audio.reduce((s: number, i: AudioItem) => s + i.duration, 0),
      },
      video: {
        count: videos.length,
        totalSize: videos.reduce((s: number, i: VideoItem) => s + i.size, 0),
        totalDuration: videos.reduce((s: number, i: VideoItem) => s + i.duration, 0),
      },
      image: {
        count: images.length,
        totalSize: images.reduce((s: number, i: ImageItem) => s + i.size, 0),
      },
      document: {
        count: documents.length,
        totalSize: documents.reduce((s: number, i: DocumentItem) => s + i.size, 0),
      },
    };
  }

  return result;
}

export async function getAlbumArtwork(
  albumId: string
): Promise<string | null> {
  return NativeModule.getAlbumArtwork(albumId);
}

export async function getVideoThumbnail(
  videoId: string,
  width?: number,
  height?: number
): Promise<string | null> {
  return NativeModule.getVideoThumbnail(
    videoId,
    width ?? null,
    height ?? null
  );
}

export async function getImageThumbnail(
  imageId: string,
  width?: number,
  height?: number
): Promise<string | null> {
  return NativeModule.getImageThumbnail(
    imageId,
    width ?? null,
    height ?? null
  );
}

export async function getFolderStatistics(
  folderPath?: string
): Promise<FolderStatistics[]> {
  return NativeModule.getFolderStatistics(folderPath ?? null);
}

export async function refreshIncremental(
  lastTimestamp?: number
): Promise<IncrementalChanges> {
  return NativeModule.refreshIncremental(lastTimestamp ?? null);
}

export async function getLastRefreshTimestamp(): Promise<number> {
  return NativeModule.getLastRefreshTimestamp();
}

export function registerPlugin(plugin: MetadataPlugin): void {
  if (!plugin.id || !plugin.name || !plugin.version || typeof plugin.extract !== "function") {
    throw new Error("Invalid plugin: must have id, name, version, and extract function");
  }
  registeredPlugins.set(plugin.id, plugin);
}

export function unregisterPlugin(pluginId: string): boolean {
  return registeredPlugins.delete(pluginId);
}

export function getRegisteredPlugins(): MetadataPlugin[] {
  return Array.from(registeredPlugins.values());
}

function applyPlugins(
  items: (AudioItem | VideoItem | ImageItem | DocumentItem)[]
): (AudioItem | VideoItem | ImageItem | DocumentItem)[] {
  if (registeredPlugins.size === 0) return items;

  return items.map((item) => {
    const merged = { ...(item as unknown as Record<string, unknown>) };
    const custom: Record<string, unknown> = {};

    for (const plugin of registeredPlugins.values()) {
      try {
        const result = plugin.extract(item);
        if (result && typeof result === "object") {
          Object.assign(custom, result);
        }
      } catch (_e) {
        // Plugin errors are silently ignored to not break queries
      }
    }

    if (Object.keys(custom).length > 0) {
      merged.customMetadata = {
        ...((merged.customMetadata as Record<string, unknown>) ?? {}),
        ...custom,
      };
    }

    return merged as unknown as AudioItem | VideoItem | ImageItem | DocumentItem;
  });
}
