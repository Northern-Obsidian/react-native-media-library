import type {
  DetailedMetadata,
  AudioFormatMetadata,
  VideoFormatMetadata,
  ImageFormatMetadata,
  ExifMetadata,
  DocumentFormatMetadata,
  MediaMetaType,
} from "../src/metadata.types";

describe("DetailedMetadata types", () => {
  it("should type-check a full DetailedMetadata object", () => {
    const meta: DetailedMetadata = {
      mediaType: "video",
      mimeType: "video/mp4",
      fileSize: 123456789,
      containerFormat: "mp4",
      durationMs: 60000,
      video: {
        codec: "h264",
        codecMime: "avc1.640028",
        profile: "High",
        level: "4.0",
        bitrate: 5000000,
        width: 1920,
        height: 1080,
        frameRate: 30,
        rotation: 90,
        colorSpace: "BT.709",
        colorStandard: "BT.709",
        colorTransfer: "BT.709",
        durationMs: 60000,
        language: "en",
      },
      audio: {
        codec: "aac",
        codecMime: "mp4a.40.2",
        bitrate: 192000,
        sampleRate: 48000,
        channels: 2,
        channelLayout: "stereo",
        bitsPerSample: 16,
        durationMs: 60000,
        language: "en",
      },
    };
    expect(meta.mediaType).toBe("video");
    expect(meta.video?.width).toBe(1920);
    expect(meta.audio?.channelLayout).toBe("stereo");
  });

  it("should type-check an audio DetailedMetadata object", () => {
    const meta: DetailedMetadata = {
      mediaType: "audio",
      mimeType: "audio/mpeg",
      fileSize: 5000000,
      containerFormat: "mp3",
      audio: {
        codec: "mp3",
        codecMime: "audio/mpeg",
        bitrate: 320000,
        sampleRate: 44100,
        channels: 2,
        channelLayout: "stereo",
      },
    };
    expect(meta.audio?.sampleRate).toBe(44100);
    expect(meta.video).toBeUndefined();
  });

  it("should type-check an image DetailedMetadata object with EXIF", () => {
    const exif: ExifMetadata = {
      make: "Canon",
      model: "EOS R5",
      aperture: 2.8,
      iso: 400,
      focalLength: 50,
      flash: true,
      gpsLatitude: 37.7749,
      gpsLongitude: -122.4194,
      dateTimeOriginal: 1700000000000,
    };
    const meta: DetailedMetadata = {
      mediaType: "image",
      mimeType: "image/jpeg",
      fileSize: 10000000,
      image: {
        format: "jpeg",
        width: 4000,
        height: 3000,
        bitsPerSample: 8,
        colorSpace: "RGB",
        exif,
      },
    };
    expect(meta.image?.exif?.make).toBe("Canon");
    expect(meta.image?.exif?.gpsLatitude).toBeCloseTo(37.7749);
  });

  it("should type-check a document DetailedMetadata object", () => {
    const doc: DocumentFormatMetadata = {
      format: "pdf",
      pageCount: 12,
      wordCount: 5000,
      characterCount: 30000,
      lineCount: 800,
      isEncrypted: false,
      creationDate: 1700000000000,
      modificationDate: 1700000000000,
    };
    const meta: DetailedMetadata = {
      mediaType: "document",
      mimeType: "application/pdf",
      fileSize: 2000000,
      document: doc,
    };
    expect(meta.document?.pageCount).toBe(12);
  });

  it("should accept all MediaMetaType values", () => {
    const types: MediaMetaType[] = ["audio", "video", "image", "document"];
    expect(types).toHaveLength(4);
  });

  it("should allow raw extras", () => {
    const meta: DetailedMetadata = {
      mediaType: "audio",
      mimeType: "audio/mpeg",
      fileSize: 1,
      raw: { customField: "value", nested: { a: 1 } },
    };
    expect(meta.raw?.customField).toBe("value");
  });
});
