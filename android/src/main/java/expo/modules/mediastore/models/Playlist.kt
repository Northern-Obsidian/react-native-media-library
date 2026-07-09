package expo.modules.mediastore.models

import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

class PlaylistRecord : Record {
  @Field var id: String = ""
  @Field var name: String = ""
  @Field var songCount: Int = 0
  @Field var duration: Long = 0L
  @Field var dateAdded: Long = 0L
  @Field var dateModified: Long = 0L
}
