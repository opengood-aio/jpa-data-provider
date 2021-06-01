package app

import io.opengood.commons.kotlin.extension.method.putIfNotAbsent
import io.opengood.commons.kotlin.extension.property.Uuid
import io.opengood.commons.kotlin.extension.property.empty
import io.opengood.data.jpa.provider.JpaDataProvider
import io.opengood.data.jpa.provider.convertFromUuid
import io.opengood.data.jpa.provider.convertToString
import io.opengood.data.jpa.provider.convertToUuid
import io.opengood.data.jpa.provider.nullableObjectValue
import io.opengood.data.jpa.provider.objectValue
import io.opengood.data.jpa.provider.rowValue
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class DataProvider(
    override val repository: DataRepository
) : JpaDataProvider<Entity, UUID> {

    override val name: String = "products"
    override val mappings: Map<String, String> =
        mapOf(
            "product_id" to "id",
            "product_name" to "name",
            "product_sku" to "sku",
            "product_category" to "category"
        )

    override fun filterMapper(filters: Map<String, Any>): Entity =
        Entity(
            id = nullableObjectValue("id", filters, convertToUuid),
            name = nullableObjectValue("name", filters, convertToString),
            sku = nullableObjectValue("sku", filters, convertToString),
            category = nullableObjectValue("category", filters, convertToString)
        )

    override fun objectMapper(row: Map<String, Any>): Entity =
        Entity(
            id = objectValue("id", row, Uuid.empty, convertToUuid),
            name = objectValue("name", row, String.empty, convertToString),
            sku = objectValue("sku", row, String.empty, convertToString),
            category = objectValue("category", row, String.empty, convertToString)
        )

    override fun rowMapper(o: Entity): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        with(map) {
            putIfNotAbsent(rowValue("id", o.id, Uuid.empty, convertFromUuid))
            putIfNotAbsent(rowValue("name", o.name, String.empty, convertToString))
            putIfNotAbsent(rowValue("sku", o.sku, String.empty, convertToString))
            putIfNotAbsent(rowValue("category", o.category, String.empty, convertToString))
        }
        return map
    }
}
