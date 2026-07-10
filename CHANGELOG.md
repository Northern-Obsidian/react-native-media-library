# Changelog

## 2.0.0 (2026-07-10)

### Breaking Changes

- **iOS Support**: Now works on both Android (MediaStore) and iOS (Photos Framework)
- **Typed Returns**: `getRecent`, `getFavorites`, `getLargestFiles` now return properly typed records instead of raw maps
- **Pagination Applied**: `limit` and `offset` in `PaginationOptions` now actually work (were ignored in v1)
- **Cache Wired**: LRU cache is now integrated into all query functions (was built but unused in v1)

### New Features

- **iOS MediaLibrary**: Full iOS support using `PHAsset` and `Photos.framework`
  - Audio, video, image queries with metadata
  - Album, artist, genre, playlist aggregation
  - Search with `CONTAINS[cd]` matching
  - Thumbnail generation via `PHImageManager`
  - Real-time change observation via `PHPhotoLibraryChangeObserver`
  - Permission handling via `PHPhotoLibrary.requestAuthorization`
- **Thumbnail Generation**: `getVideoThumbnail` and `getImageThumbnail` now return actual thumbnail file URIs
  - Android: Uses `ContentResolver.loadThumbnail()` (API 29+) with fallback to `MediaStore.Thumbnails`
  - iOS: Uses `PHImageManager.requestImage()` with configurable size
- **Duplicate Detection**: `getDuplicates` now groups files by size + MD5 hash
- **Document Statistics**: `getStatistics` now includes document counts and sizes
- **ExifInterface**: Image metadata now reads camera make/model from EXIF data

### Bug Fixes

- **SQL Injection**: Fixed unsafe string interpolation in query filters (now uses `escapeSql()`)
- **Broken Extensions Filter**: Fixed `LIKE '%.' || $quoted` syntax to proper `LIKE '%.ext'`
- **Hardcoded Mapper Values**: Audio `isFavorite` now reads `IS_FAVORITE` column (API 29+)
- **Video/Image Metadata**: `relativePath` now read from cursor instead of hardcoded empty string
- **Image GPS**: `gpsLatitude`/`gpsLongitude` now read from `MediaStore.Images.Media` columns
- **Permissions Request**: `requestPermissions()` now actually prompts the user (was a no-op)
- **Genre/Playlist Queries**: Now respect sort and pagination parameters
- **Video Rotation**: Now reads `ORIENTATION` column instead of hardcoded 0

### Improvements

- **Cache Auto-Invalidation**: Cache automatically invalidates on `onMediaChange` events
- **iOS Observers**: `PHPhotoLibraryChangeObserver` properly maps insert/update/remove events
- **Sort Support**: Genre and playlist queries now support sorting

## 1.0.0 (2025-07-10)

### Initial Release

- Audio queries (`getAudio`) with metadata (artist, album, genre, duration, bitrate, etc.)
- Video queries (`getVideos`) with metadata (resolution, frame rate, rotation, orientation, etc.)
- Image queries (`getImages`) with metadata (EXIF, GPS, orientation, camera info, etc.)
- Document queries (`getDocuments`) with support for PDF, DOC, DOCX, XLS, XLSX, PPT, PPTX, TXT, EPUB, RTF, CSV, JSON, XML, ZIP, RAR, 7Z
- Album, Artist, Genre, Playlist aggregation queries
- Folder aggregation (`getFolders`) with file counts and total sizes
- Full-text search engine with prefix, partial, case-insensitive, multi-keyword, unicode support
- Pagination (limit/offset and cursor-based) on every API
- Real-time change observation via `ContentObserver` (add, remove, modify events)
- Granular permissions for Android 13+ (`READ_MEDIA_AUDIO`, `READ_MEDIA_VIDEO`, `READ_MEDIA_IMAGES`)
- Legacy `READ_EXTERNAL_STORAGE` fallback for Android 12 and below
- LRU in-memory cache with configurable TTL and auto-invalidation
- Duplicate detection using file heuristics
- Media statistics (counts, total size, total duration)
- Favorites support
- React hook `useMediaChangeEvent`
- Structured error codes
- 100% TypeScript types
- Thread-safe background execution on `Dispatchers.IO`
- Android API 21+ support
- Expo Modules API compatibility
