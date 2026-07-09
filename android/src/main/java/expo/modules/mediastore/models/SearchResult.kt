package expo.modules.mediastore.models

import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

class SearchResultRecord : Record {
  @Field var audio: List<AudioRecord> = emptyList()
  @Field var videos: List<VideoRecord> = emptyList()
  @Field var images: List<ImageRecord> = emptyList()
  @Field var documents: List<DocumentRecord> = emptyList()
  @Field var totalCount: Int = 0
  @Field var query: String = ""
}
