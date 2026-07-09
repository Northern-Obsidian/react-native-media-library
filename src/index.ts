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
};

export { SortOrder, SortField } from "./MediaStoreModule.types";

export async function getAudio(
  sort?: SortOptions,
  filter?: FilterOptions,
  pagination?: PaginationOptions
): Promise<AudioItem[]> {
  return NativeModule.getAudio(sort ?? null, filter ?? null, pagination ?? null);
}

export async function getVideos(
  sort?: SortOptions,
  filter?: FilterOptions,
  pagination?: PaginationOptions
): Promise<VideoItem[]> {
  return NativeModule.getVideos(sort ?? null, filter ?? null, pagination ?? null);
}

export async function getImages(
  sort?: SortOptions,
  filter?: FilterOptions,
  pagination?: PaginationOptions
): Promise<ImageItem[]> {
  return NativeModule.getImages(sort ?? null, filter ?? null, pagination ?? null);
}

export async function getDocuments(
  sort?: SortOptions,
  filter?: FilterOptions,
  pagination?: PaginationOptions
): Promise<DocumentItem[]> {
  return NativeModule.getDocuments(sort ?? null, filter ?? null, pagination ?? null);
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
  return NativeModule.search(options);
}

export async function getById(
  mediaType: "audio" | "video" | "image" | "document",
  id: string
): Promise<AudioItem | VideoItem | ImageItem | DocumentItem | null> {
  return NativeModule.getById(mediaType, id);
}

export async function getByUri(uri: string): Promise<AudioItem | VideoItem | ImageItem | DocumentItem | null> {
  return NativeModule.getByUri(uri);
}

export async function getRecent(
  mediaType?: "audio" | "video" | "image" | "document",
  limit?: number
): Promise<(AudioItem | VideoItem | ImageItem | DocumentItem)[]> {
  return NativeModule.getRecent(mediaType ?? null, limit ?? null);
}

export async function getFavorites(
  mediaType?: "audio" | "video" | "image" | "document",
  sort?: SortOptions,
  pagination?: PaginationOptions
): Promise<(AudioItem | VideoItem | ImageItem | DocumentItem)[]> {
  return NativeModule.getFavorites(mediaType ?? null, sort ?? null, pagination ?? null);
}

export async function getLargestFiles(
  mediaType?: "audio" | "video" | "image" | "document",
  limit?: number
): Promise<(AudioItem | VideoItem | ImageItem | DocumentItem)[]> {
  return NativeModule.getLargestFiles(mediaType ?? null, limit ?? null);
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
