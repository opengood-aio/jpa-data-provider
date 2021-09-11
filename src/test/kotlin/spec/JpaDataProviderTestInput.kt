package spec

import io.opengood.data.jpa.provider.JpaDataProvider

interface JpaDataProviderTestInput {
    val dataProvider: JpaDataProvider<*, *>
    val data: List<Map<String, Any>>
    val filters: Map<String, Any>
    val sort: List<String>
}
