export type MediaMetaType = "audio" | "video" | "image" | "document";

/**
 * Rich, deep metadata for a single media item. Returned by `getDetailedMetadata`
 * and `getDetailedMetadataByUri`. Technical/Capture metadata is extracted by
 * opening the file (MediaExtractor / ExifInterface on Android, AVAsset /
 * CGImageSource on iOS), so it is only populated on demand — not in bulk queries.
 */
export interface DetailedMetadata {
  mediaType: MediaMetaType;
  mimeType: string;
  fileSize: number;
  /** Container/wrapper format, e.g. "mp4", "mpeg-4", "matroska", "mpeg-4-visual". */
  containerFormat?: string;
  /** Present for audio and video. */
  durationMs?: number;
  audio?: AudioFormatMetadata;
  video?: VideoFormatMetadata;
  image?: ImageFormatMetadata;
  document?: DocumentFormatMetadata;
  /** Platform-specific extras that were not mapped into the typed sections. */
  raw?: Record<string, unknown>;
}

export interface AudioFormatMetadata {
  /** Normalized short codec name, e.g. "mp3", "aac", "opus", "flac". */
  codec?: string;
  /** Raw codec identifier / MIME, e.g. "audio/mpeg", "mp4a.40.2". */
  codecMime?: string;
  /** Codec profile, e.g. "HE-AAC", "LC", "HD". */
  codecProfile?: string;
  /** Bits per second. */
  bitrate?: number;
  /** Sample rate in Hz. */
  sampleRate?: number;
  /** Number of channels. */
  channels?: number;
  /** Human readable channel layout, e.g. "mono", "stereo", "5.1". */
  channelLayout?: string;
  /** Bits per sample (PCM depth). */
  bitsPerSample?: number;
  durationMs?: number;
  /** RFC-5646 language tag, e.g. "en". */
  language?: string;
}

export interface VideoFormatMetadata {
  /** Normalized short codec name, e.g. "h264", "hevc", "vp9", "av1". */
  codec?: string;
  codecMime?: string;
  /** Codec profile, e.g. "High", "Baseline", "Main". */
  profile?: string;
  /** Codec level, e.g. "4.0". */
  level?: string;
  /** Video bitrate in bits per second. */
  bitrate?: number;
  width?: number;
  height?: number;
  /** Frames per second. */
  frameRate?: number;
  /** Display rotation in degrees. */
  rotation?: number;
  /** Colour space, e.g. "BT.709", "BT.601", "BT.2020". */
  colorSpace?: string;
  colorStandard?: string;
  colorTransfer?: string;
  hasBFrames?: boolean;
  durationMs?: number;
  language?: string;
}

export interface ImageFormatMetadata {
  /** Normalized short format, e.g. "jpeg", "png", "heic", "gif", "webp", "bmp", "tiff". */
  format?: string;
  width?: number;
  height?: number;
  bitsPerSample?: number;
  /** e.g. "RGB", "YCbCr", "Gray". */
  colorSpace?: string;
  exif?: ExifMetadata;
}

export interface ExifMetadata {
  make?: string;
  model?: string;
  software?: string;
  lensMake?: string;
  lensModel?: string;
  imageDescription?: string;
  artist?: string;
  copyright?: string;
  /** Epoch milliseconds. */
  dateTimeOriginal?: number;
  /** Epoch milliseconds. */
  dateTimeDigitized?: number;
  orientation?: number;
  /** F-number (e.g. 2.8). */
  aperture?: number;
  iso?: number;
  /** APEX shutter speed. */
  shutterSpeed?: number;
  /** Seconds (e.g. 0.004). */
  exposureTime?: number;
  exposureProgram?: string;
  /** APEX exposure bias. */
  exposureBias?: number;
  meteringMode?: string;
  flash?: boolean;
  flashMode?: string;
  whiteBalance?: string;
  /** Focal length in mm. */
  focalLength?: number;
  /** 35mm-equivalent focal length in mm. */
  focalLength35mm?: number;
  sceneCaptureType?: string;
  contrast?: string;
  saturation?: string;
  sharpness?: string;
  digitalZoomRatio?: number;
  /** JPEG compression ratio (bits per pixel). */
  compressedBitsPerPixel?: number;
  gpsLatitude?: number;
  gpsLongitude?: number;
  gpsAltitude?: number;
  /** Epoch milliseconds of GPS timestamp. */
  gpsTimestamp?: number;
  gpsProcessingMethod?: string;
  /** e.g. "sRGB", "Adobe RGB", "Uncalibrated". */
  colorSpace?: string;
  pixelXDimension?: number;
  pixelYDimension?: number;
}

export interface DocumentFormatMetadata {
  /** Normalized short format, e.g. "pdf", "docx", "txt". */
  format?: string;
  pageCount?: number;
  wordCount?: number;
  characterCount?: number;
  lineCount?: number;
  title?: string;
  author?: string;
  creator?: string;
  producer?: string;
  subject?: string;
  keywords?: string[];
  language?: string;
  isEncrypted?: boolean;
  /** Epoch milliseconds. */
  creationDate?: number;
  /** Epoch milliseconds. */
  modificationDate?: number;
}
