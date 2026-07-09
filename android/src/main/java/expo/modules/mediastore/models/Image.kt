package expo.modules.mediastore.models

import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

class ImageRecord : Record {
  @Field var id: String = ""
  @Field var uri: String = ""
  @Field var title: String = ""
  @Field var width: Int = 0
  @Field var height: Int = 0
  @Field var orientation: Int = 0
  @Field var cameraMake: String? = null
  @Field var cameraModel: String? = null
  @Field var dateTaken: Long = 0L
  @Field var gpsLatitude: Double? = null
  @Field var gpsLongitude: Double? = null
  @Field var mimeType: String = ""
  @Field var size: Long = 0L
  @Field var relativePath: String = ""
  @Field var displayName: String = ""
  @Field var dateAdded: Long = 0L
  @Field var dateModified: Long = 0L
}
