package expo.modules.mediastore.models

import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

class FolderRecord : Record {
  @Field var id: String = ""
  @Field var name: String = ""
  @Field var path: String = ""
  @Field var fileCount: Int = 0
  @Field var totalSize: Long = 0L
}
