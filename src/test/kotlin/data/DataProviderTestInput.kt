package data

import io.opengood.data.jpa.provider.JpaDataProvider
import org.springframework.context.annotation.Configuration
import spec.JpaDataProviderTestInput

@Configuration
class DataProviderTestInput(
    override val dataProvider: JpaDataProvider<*, *>
) : JpaDataProviderTestInput {

    override val data: List<MutableMap<String, Any>> = listOf(
        mutableMapOf(
            "product_name" to "Apple iPhone",
            "product_sku" to "123456",
            "product_category" to "mobile"
        ),
        mutableMapOf(
            "product_name" to "Apple iPad",
            "product_sku" to "098765",
            "product_category" to "tablet"
        ),
        mutableMapOf(
            "product_name" to "Samsung OLED TV",
            "product_sku" to "135790",
            "product_category" to "device"
        ),
        mutableMapOf(
            "product_name" to "Apple Watch",
            "product_sku" to "012345",
            "product_category" to "mobile"
        ),
        mutableMapOf(
            "product_name" to "Apple MBP",
            "product_sku" to "246801",
            "product_category" to "computer"
        )
    )

    override val filters = mapOf(
        "product_sku" to "123456",
        "product_name" to "Samsung"
    )

    override val sort = listOf(
        "product_sku",
        "product_category"
    )
}
