package expo.modules.mediastore.models

import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

class StatisticsRecord : Record {
  @Field var totalAudio: Int = 0
  @Field var totalVideo: Int = 0
  @Field var totalImages: Int = 0
  @Field var totalDocuments: Int = 0
  @Field var totalSize: Long = 0L
  @Field var totalDuration: Long = 0L
}
