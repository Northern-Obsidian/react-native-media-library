package expo.modules.mediastore.models

import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

class DocumentRecord : Record {
  @Field var id: String = ""
  @Field var uri: String = ""
  @Field var name: String = ""
  @Field var size: Long = 0L
  @Field var mimeType: String = ""
  @Field var extension: String = ""
  @Field var relativePath: String = ""
  @Field var dateAdded: Long = 0L
  @Field var dateModified: Long = 0L
}
