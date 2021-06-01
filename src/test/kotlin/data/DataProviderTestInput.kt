package data

import io.opengood.data.jpa.provider.JpaDataProvider
import org.springframework.context.annotation.Configuration
import spec.JpaDataProviderTestInput
import java.util.UUID

@Configuration
class DataProviderTestInput(
    override val dataProvider: JpaDataProvider<*, *>
) : JpaDataProviderTestInput {

    override val data = listOf(
        mapOf(
            "product_id" to UUID.randomUUID().toString(),
            "product_name" to "Apple iPhone",
            "product_sku" to "123456"
        ),
        mapOf(
            "product_id" to UUID.randomUUID().toString(),
            "product_name" to "Apple iPad",
            "product_sku" to "098765"
        ),
        mapOf(
            "product_id" to UUID.randomUUID().toString(),
            "product_name" to "Samsung OLED TV",
            "product_sku" to "135790"
        ),
        mapOf(
            "product_id" to UUID.randomUUID().toString(),
            "product_name" to "Apple iPhone",
            "product_sku" to "123456"
        ),
        mapOf(
            "product_id" to UUID.randomUUID().toString(),
            "product_name" to "Apple MBP",
            "product_sku" to "246801"
        ),
    )

    override val filters = mapOf(
        "product_name" to "Apple iPhone",
        "product_sku" to "123456"
    )

    override val sort = listOf(
        "product_name",
        "product_sku"
    )

    override val ignoreKeys = listOf("product_id")
}
