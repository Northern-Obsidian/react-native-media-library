export interface AudioItem {
  id: string;
  uri: string;
  title: string;
  artist: string;
  album: string;
  albumId: string;
  genre: string | null;
  duration: number;
  size: number;
  trackNumber: number;
  discNumber: number;
  year: number;
  dateAdded: number;
  dateModified: number;
  bitrate: number | null;
  mimeType: string;
  fileExtension: string;
  relativePath: string;
  displayName: string;
  contentUri: string;
}

export interface VideoItem {
  id: string;
  uri: string;
  title: string;
  duration: number;
  width: number;
  height: number;
  frameRate: number | null;
  rotation: number;
  size: number;
  mimeType: string;
  relativePath: string;
  displayName: string;
  dateAdded: number;
  dateModified: number;
  resolution: string;
  orientation: number;
}

export interface ImageItem {
  id: string;
  uri: string;
  title: string;
  width: number;
  height: number;
  orientation: number;
  cameraMake: string | null;
  cameraModel: string | null;
  dateTaken: number;
  gpsLatitude: number | null;
  gpsLongitude: number | null;
  mimeType: string;
  size: number;
  relativePath: string;
  displayName: string;
  dateAdded: number;
  dateModified: number;
}

export interface DocumentItem {
  id: string;
  uri: string;
  name: string;
  size: number;
  mimeType: string;
  extension: string;
  relativePath: string;
  dateAdded: number;
  dateModified: number;
}

export interface Album {
  id: string;
  title: string;
  artist: string;
  songCount: number;
  duration: number;
  artworkUri: string | null;
  dateAdded: number;
  year: number | null;
}

export interface Artist {
  id: string;
  name: string;
  albumCount: number;
  songCount: number;
  duration: number;
  dateAdded: number;
}

export interface Genre {
  id: string;
  name: string;
  songCount: number;
  duration: number;
}

export interface Playlist {
  id: string;
  name: string;
  songCount: number;
  duration: number;
  dateAdded: number;
  dateModified: number;
}

export interface Folder {
  id: string;
  name: string;
  path: string;
  fileCount: number;
  totalSize: number;
}

export interface SearchResult {
  audio: AudioItem[];
  videos: VideoItem[];
  images: ImageItem[];
  documents: DocumentItem[];
  totalCount: number;
  query: string;
}

export enum SortOrder {
  Ascending = "asc",
  Descending = "desc",
}

export enum SortField {
  Name = "name",
  DateAdded = "dateAdded",
  DateModified = "dateModified",
  Duration = "duration",
  Artist = "artist",
  Album = "album",
  Year = "year",
  FileSize = "fileSize",
  Resolution = "resolution",
  Width = "width",
  Height = "height",
}

export interface SortOptions {
  field: SortField;
  order: SortOrder;
}

export interface FilterOptions {
  mimeTypes?: string[];
  extensions?: string[];
  folder?: string;
  album?: string;
  artist?: string;
  minDuration?: number;
  maxDuration?: number;
  minSize?: number;
  maxSize?: number;
  minResolution?: number;
  maxResolution?: number;
  startDate?: number;
  endDate?: number;
  includeHidden?: boolean;
  favoritesOnly?: boolean;
  playlistId?: string;
}

export interface PaginationOptions {
  limit?: number;
  offset?: number;
  cursor?: string;
}

export interface SearchOptions {
  query: string;
  types?: ("audio" | "video" | "image" | "document")[];
  sort?: SortOptions;
  filter?: FilterOptions;
  pagination?: PaginationOptions;
}

export interface MediaStoreStatistics {
  totalAudio: number;
  totalVideo: number;
  totalImages: number;
  totalDocuments: number;
  totalSize: number;
  totalDuration: number;
}

export interface DuplicateItem {
  fileHash: string;
  count: number;
  items: AudioItem[];
  totalSize: number;
}

export interface MediaChangeEvent {
  type: "added" | "removed" | "modified";
  mediaType: "audio" | "video" | "image" | "document";
  itemId: string;
  uri: string;
}

export interface PermissionStatus {
  granted: boolean;
  audio: boolean;
  video: boolean;
  images: boolean;
}

export type ErrorCode =
  | "PERMISSION_DENIED"
  | "QUERY_FAILED"
  | "INVALID_ARGUMENTS"
  | "INVALID_SORT_FIELD"
  | "INVALID_MIME_TYPE"
  | "UNSUPPORTED_ANDROID_VERSION"
  | "FILE_UNAVAILABLE"
  | "CURSOR_CLOSED"
  | "CACHE_FAILURE"
  | "UNKNOWN_ERROR";

export interface MediaStoreError {
  code: ErrorCode;
  message: string;
  details?: string;
}

export interface LibraryResult {
  audio: AudioItem[];
  videos: VideoItem[];
  images: ImageItem[];
  documents: DocumentItem[];
  totalCount: number;
}

export interface ArtworkResult {
  uri: string | null;
  mimeType: string | null;
}

export interface ThumbnailOptions {
  width?: number;
  height?: number;
  kind?: "MINI_KIND" | "FULL_SCREEN_KIND" | "MICRO_KIND";
}
