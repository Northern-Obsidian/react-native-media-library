package expo.modules.mediastore.models

import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

class SearchOptionsRecord : Record {
  @Field var query: String = ""
  @Field var types: List<String>? = null
  @Field var sort: SortOptionsRecord? = null
  @Field var filter: FilterOptionsRecord? = null
  @Field var pagination: PaginationOptionsRecord? = null
}
