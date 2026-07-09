package expo.modules.mediastore.models

import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

class PaginationOptionsRecord : Record {
  @Field var limit: Int? = null
  @Field var offset: Int? = null
  @Field var cursor: String? = null
}
