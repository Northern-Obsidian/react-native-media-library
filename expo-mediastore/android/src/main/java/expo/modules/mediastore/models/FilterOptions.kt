package expo.modules.mediastore.models

import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

class FilterOptionsRecord : Record {
  @Field var mimeTypes: List<String>? = null
  @Field var extensions: List<String>? = null
  @Field var folder: String? = null
  @Field var album: String? = null
  @Field var artist: String? = null
  @Field var minDuration: Long? = null
  @Field var maxDuration: Long? = null
  @Field var minSize: Long? = null
  @Field var maxSize: Long? = null
  @Field var minResolution: Int? = null
  @Field var maxResolution: Int? = null
  @Field var startDate: Long? = null
  @Field var endDate: Long? = null
  @Field var includeHidden: Boolean? = null
  @Field var favoritesOnly: Boolean? = null
  @Field var playlistId: String? = null
}
