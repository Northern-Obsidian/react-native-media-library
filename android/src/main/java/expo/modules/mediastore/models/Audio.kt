package expo.modules.mediastore.models

import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

class AudioRecord : Record {
  @Field var id: String = ""
  @Field var uri: String = ""
  @Field var title: String = ""
  @Field var artist: String = ""
  @Field var album: String = ""
  @Field var albumId: String = ""
  @Field var genre: String? = null
  @Field var duration: Long = 0L
  @Field var size: Long = 0L
  @Field var trackNumber: Int = 0
  @Field var discNumber: Int = 0
  @Field var year: Int = 0
  @Field var dateAdded: Long = 0L
  @Field var dateModified: Long = 0L
  @Field var composer: String? = null
  @Field var lyrics: String? = null
  @Field var albumArtist: String? = null
  @Field var isFavorite: Boolean = false
  @Field var playCount: Int = 0
  @Field var lastPlayed: Long = 0L
  @Field var bookmark: Long = 0L
  @Field var bitrate: Int? = null
  @Field var sampleRate: Int? = null
  @Field var channels: Int? = null
  @Field var encoding: String? = null
  @Field var mimeType: String = ""
  @Field var fileExtension: String = ""
  @Field var relativePath: String = ""
  @Field var displayName: String = ""
  @Field var contentUri: String = ""
}
