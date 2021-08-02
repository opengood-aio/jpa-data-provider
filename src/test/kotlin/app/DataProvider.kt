package app

import io.opengood.commons.kotlin.extension.method.putIfNotAbsent
import io.opengood.commons.kotlin.extension.property.Uuid
import io.opengood.commons.kotlin.extension.property.empty
import io.opengood.data.jpa.provider.JpaDataProvider
import io.opengood.data.jpa.provider.convertFromUuid
import io.opengood.data.jpa.provider.convertToString
import io.opengood.data.jpa.provider.convertToUuid
import io.opengood.data.jpa.provider.nullableObjectFieldValue
import io.opengood.data.jpa.provider.objectFieldValue
import io.opengood.data.jpa.provider.rowColumnValue
import org.springframework.stereotype.Component
import java.util.*

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
            id = nullableObjectFieldValue("id", filters, convertToUuid),
            name = nullableObjectFieldValue("name", filters, convertToString),
            sku = nullableObjectFieldValue("sku", filters, convertToString),
            category = nullableObjectFieldValue("category", filters, convertToString)
        )

    override fun objectFieldMapper(row: Map<String, Any>): Entity =
        Entity(
            id = objectFieldValue("id", row, Uuid.empty, convertToUuid),
            name = objectFieldValue("name", row, String.empty, convertToString),
            sku = objectFieldValue("sku", row, String.empty, convertToString),
            category = objectFieldValue("category", row, String.empty, convertToString)
        )

    override fun rowColumnMapper(o: Entity): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        with(map) {
            putIfNotAbsent(rowColumnValue("id", o.id, Uuid.empty, convertFromUuid))
            putIfNotAbsent(rowColumnValue("name", o.name, String.empty, convertToString))
            putIfNotAbsent(rowColumnValue("sku", o.sku, String.empty, convertToString))
            putIfNotAbsent(rowColumnValue("category", o.category, String.empty, convertToString))
        }
        return map
    }
}
