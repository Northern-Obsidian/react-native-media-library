package expo.modules.mediastore.models

import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

class SizeHistogramRecord : Record {
  @Field var lessThan1MB: Int = 0
  @Field var from1to10MB: Int = 0
  @Field var from10to100MB: Int = 0
  @Field var from100MBto1GB: Int = 0
  @Field var greaterThan1GB: Int = 0
}

class MediaTypeBreakdownRecord : Record {
  @Field var audio: Int = 0
  @Field var video: Int = 0
  @Field var image: Int = 0
  @Field var document: Int = 0
}

class FolderStatisticsRecord : Record {
  @Field var id: String = ""
  @Field var name: String = ""
  @Field var path: String = ""
  @Field var fileCount: Int = 0
  @Field var totalSize: Long = 0L
  @Field var histogram: SizeHistogramRecord = SizeHistogramRecord()
  @Field var mediaTypeBreakdown: MediaTypeBreakdownRecord = MediaTypeBreakdownRecord()
  @Field var averageFileSize: Double = 0.0
}

class IncrementalChangesRecord : Record {
  @Field var added: Int = 0
  @Field var modified: Int = 0
  @Field var removed: Int = 0
  @Field var timestamp: Long = 0L
}
