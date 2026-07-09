package expo.modules.mediastore.models

import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

class SortOptionsRecord : Record {
  @Field var field: String = "name"
  @Field var order: String = "asc"
}
