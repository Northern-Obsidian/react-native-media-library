package expo.modules.mediastore.models

import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

class VideoRecord : Record {
  @Field var id: String = ""
  @Field var uri: String = ""
  @Field var title: String = ""
  @Field var duration: Long = 0L
  @Field var width: Int = 0
  @Field var height: Int = 0
  @Field var frameRate: Double? = null
  @Field var rotation: Int = 0
  @Field var size: Long = 0L
  @Field var mimeType: String = ""
  @Field var relativePath: String = ""
  @Field var displayName: String = ""
  @Field var dateAdded: Long = 0L
  @Field var dateModified: Long = 0L
  @Field var resolution: String = ""
  @Field var orientation: Int = 0
}
