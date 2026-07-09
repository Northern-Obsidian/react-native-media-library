# expo-mediastore

**Universal high-performance media indexing library for Android using MediaStore.**

A reusable Expo Module that provides fast, production-grade access to media and indexed documents on Android devices by leveraging the native MediaStore database instead of recursive filesystem scanning.

## Features

- **Blazing fast** — queries the native MediaStore database directly, no recursive directory scans
- **All media types** — audio, video, images, and documents (PDF, DOC/DOCX, XLS/XLSX, PPT/PPTX, TXT, EPUB, RTF, CSV, JSON, XML, ZIP, RAR, 7Z)
- **Rich metadata** — duration, resolution, bitrate, EXIF, GPS, album art, and more
- **Sorted & filtered queries** — sort by name, date, size, duration, artist, etc. Filter by MIME, extension, folder, date range, size range, and more
- **Full-text search** — prefix, partial, case-insensitive, multi-keyword, unicode-aware
- **Pagination** — limit/offset and cursor-based pagination on every API
- **Real-time change observation** — `ContentObserver` fires `onMediaChange` events when files are added, removed, or modified
- **Permissions-aware** — scoped `READ_MEDIA_*` permissions on Android 13+, automatic fallback
- **LRU caching** — optional in-memory cache with configurable TTL, auto-invalidated on changes
- **Fully typed** — complete TypeScript definitions, no `any`

## Installation

```bash
npm install expo-mediastore
```

Or with a development build:

```bash
npx expo install expo-mediastore
```

## Prerequisites

- Expo SDK 50+ or React Native with New Architecture enabled
- Android API 21+ (Android 5.0)
- For Android 13+ (API 33): granular media permissions are requested automatically
- For Android 12 and below: `READ_EXTERNAL_STORAGE` permission is required

## Quick Start

```typescript
import { getAudio, getImages, useMediaChangeEvent } from "expo-mediastore";
import { useEffect } from "react";

// Fetch all audio tracks
const songs = await getAudio(
  { field: "dateAdded", order: "desc" },
  { minDuration: 30_000 }
);

// Fetch recent images with pagination
const photos = await getImages(
  { field: "dateAdded", order: "desc" },
  null,
  { limit: 20, offset: 0 }
);

// Listen for MediaStore changes in a component
function MediaWatcher() {
  const event = useMediaChangeEvent((e) => {
    console.log(`Media ${e.type}: ${e.mediaType} [${e.itemId}]`);
  });
  return null;
}
```

## API Reference

### Media Queries

| Function | Returns | Description |
|----------|---------|-------------|
| `getAudio(sort?, filter?, pagination?)` | `AudioItem[]` | Fetch audio tracks |
| `getVideos(sort?, filter?, pagination?)` | `VideoItem[]` | Fetch video files |
| `getImages(sort?, filter?, pagination?)` | `ImageItem[]` | Fetch images |
| `getDocuments(sort?, filter?, pagination?)` | `DocumentItem[]` | Fetch documents |
| `getAlbums(sort?, filter?, pagination?)` | `Album[]` | Fetch audio albums |
| `getArtists(sort?, pagination?)` | `Artist[]` | Fetch artists |
| `getGenres(sort?, pagination?)` | `Genre[]` | Fetch genres |
| `getPlaylists(sort?, pagination?)` | `Playlist[]` | Fetch playlists |
| `getFolders(sort?, filter?, pagination?)` | `Folder[]` | Aggregate files by folder |

### Search & Lookup

| Function | Returns | Description |
|----------|---------|-------------|
| `search(options)` | `SearchResult` | Full-text search across media types |
| `getById(mediaType, id)` | `AudioItem \| VideoItem \| ImageItem \| DocumentItem \| null` | Lookup by database ID |
| `getByUri(uri)` | `AudioItem \| VideoItem \| ImageItem \| DocumentItem \| null` | Lookup by content URI |

### Utility Queries

| Function | Returns | Description |
|----------|---------|-------------|
| `getRecent(mediaType?, limit?)` | `(AudioItem \| VideoItem \| ImageItem \| DocumentItem)[]` | Most recently added items |
| `getFavorites(mediaType?, sort?, pagination?)` | `(AudioItem \| VideoItem \| ImageItem \| DocumentItem)[]` | Starred/favorited items |
| `getLargestFiles(mediaType?, limit?)` | `(AudioItem \| VideoItem \| ImageItem \| DocumentItem)[]` | Largest files by size |
| `getDuplicates(mediaType?)` | `DuplicateItem[]` | Detect duplicate files |
| `getStatistics()` | `MediaStoreStatistics` | Aggregate counts and sizes |

### System

| Function | Returns | Description |
|----------|---------|-------------|
| `refresh()` | `void` | Invalidate all caches |
| `checkPermissions()` | `PermissionStatus` | Check current permission state |
| `requestPermissions()` | `PermissionStatus` | Request media permissions |
| `useMediaChangeEvent(callback?)` | `MediaChangeEvent \| null` | React hook for change events |

### Types

#### AudioItem

```typescript
interface AudioItem {
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
```

#### VideoItem

```typescript
interface VideoItem {
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
```

#### ImageItem

```typescript
interface ImageItem {
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
```

#### DocumentItem

```typescript
interface DocumentItem {
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
```

#### SortOptions

```typescript
interface SortOptions {
  field: SortField;
  order: SortOrder;
}

enum SortField {
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

enum SortOrder {
  Ascending = "asc",
  Descending = "desc",
}
```

#### FilterOptions

```typescript
interface FilterOptions {
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
```

#### PaginationOptions

```typescript
interface PaginationOptions {
  limit?: number;
  offset?: number;
  cursor?: string;
}
```

#### SearchOptions

```typescript
interface SearchOptions {
  query: string;
  types?: ("audio" | "video" | "image" | "document")[];
  sort?: SortOptions;
  filter?: FilterOptions;
  pagination?: PaginationOptions;
}
```

#### MediaChangeEvent

```typescript
interface MediaChangeEvent {
  type: "added" | "removed" | "modified";
  mediaType: "audio" | "video" | "image" | "document";
  itemId: string;
  uri: string;
}
```

#### PermissionStatus

```typescript
interface PermissionStatus {
  granted: boolean;
  audio: boolean;
  video: boolean;
  images: boolean;
}
```

## Supported Document Types

| Format | MIME Type |
|--------|-----------|
| PDF | `application/pdf` |
| DOC | `application/msword` |
| DOCX | `application/vnd.openxmlformats-officedocument.wordprocessingml.document` |
| XLS | `application/vnd.ms-excel` |
| XLSX | `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet` |
| PPT | `application/vnd.ms-powerpoint` |
| PPTX | `application/vnd.openxmlformats-officedocument.presentationml.presentation` |
| TXT | `text/plain` |
| EPUB | `application/epub+zip` |
| RTF | `application/rtf` |
| CSV | `text/csv` |
| JSON | `application/json` |
| XML | `application/xml`, `text/xml` |
| ZIP | `application/zip` |
| RAR | `application/x-rar-compressed` |
| 7Z | `application/x-7z-compressed` |

## Examples

### Audio Player Library

```typescript
import { getAudio, getAlbums, getArtists } from "expo-mediastore";

async function loadLibrary() {
  const [songs, albums, artists] = await Promise.all([
    getAudio({ field: "artist", order: "asc" }),
    getAlbums(),
    getArtists(),
  ]);
  return { songs, albums, artists };
}
```

### Recent Photos Gallery

```typescript
import { getImages } from "expo-mediastore";

async function loadGalleryPage(cursor?: string) {
  return getImages(
    { field: "dateAdded", order: "desc" },
    null,
    { limit: 30, cursor }
  );
}
```

### Full-Text Search

```typescript
import { search } from "expo-mediastore";

async function searchMedia(query: string) {
  return search({
    query,
    types: ["audio", "video"],
    sort: { field: "name", order: "asc" },
    pagination: { limit: 50 },
  });
}
```

### Permission Handling

```typescript
import { checkPermissions, requestPermissions } from "expo-mediastore";

async function ensurePermissions() {
  const status = await checkPermissions();
  if (!status.granted) {
    return requestPermissions();
  }
  return status;
}
```

## Architecture

```
React Native / Expo
        │
        ▼
  TypeScript API
        │
        ▼
  Expo Module (expo-module-core)
        │
        ▼
  Kotlin — MediaStoreModule.kt
        │
        ▼
  MediaStoreRepository — ContentResolver queries
        │
        ▼
  MediaStoreQueryBuilder — projections, filters, sort
        │
        ▼
  MediaStoreMapper — Cursor → domain models
        │
        ▼
  JSON (serialized back to JS)
```

The module is **not** a file explorer. It is an indexing engine built on Android's MediaStore database. Filesystem operations (delete, rename, copy, move, write) are intentionally excluded and belong in a separate module.

## Permissions

| Android Version | Required Permissions |
|----------------|---------------------|
| 13+ (API 33+) | `READ_MEDIA_AUDIO`, `READ_MEDIA_VIDEO`, `READ_MEDIA_IMAGES` |
| 12 and below | `READ_EXTERNAL_STORAGE` |

Call `requestPermissions()` before querying media on first launch.

## Performance

- All queries execute on background dispatchers — never blocks the UI thread
- Uses typed projections to minimize cursor column count
- Every `Cursor` is closed in a `use` block to prevent leaks
- Optional LRU cache reduces repeated MediaStore scans
- Supports libraries with hundreds of thousands of items
- Never recursively scans storage directories

## Android Compatibility

- **Minimum SDK**: 21 (Android 5.0)
- **Target SDK**: 34 (Android 14)
- Scoped storage is handled automatically via MediaStore URIs
- No `MANAGE_EXTERNAL_STORAGE` permission requested — uses the standard MediaStore access pattern

## License

MIT
