package expo.modules.mediastore.models

import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

class AlbumRecord : Record {
  @Field var id: String = ""
  @Field var title: String = ""
  @Field var artist: String = ""
  @Field var songCount: Int = 0
  @Field var duration: Long = 0L
  @Field var artworkUri: String? = null
  @Field var dateAdded: Long = 0L
  @Field var year: Int? = null
}
