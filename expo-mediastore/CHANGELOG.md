# Changelog

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
