package expo.modules.mediastore.models

import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

class DuplicateRecord : Record {
  @Field var fileHash: String = ""
  @Field var count: Int = 0
  @Field var items: List<AudioRecord> = emptyList()
  @Field var totalSize: Long = 0L
}
