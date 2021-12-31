package data

import io.opengood.data.jpa.provider.JpaDataProvider
import io.opengood.data.jpa.provider.contract.FilteringCondition
import io.opengood.data.jpa.provider.contract.FilteringParameter
import io.opengood.data.jpa.provider.contract.FilteringType
import org.springframework.context.annotation.Configuration
import spec.JpaDataProviderTestInput

@Configuration
class MatchAllDataProviderTestInput(
    override val dataProvider: JpaDataProvider<*, *>
) : JpaDataProviderTestInput {

    override val name = "Match All"

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

    override val filters = listOf(
        FilteringParameter(name = "product_category", value = "mobile", type = FilteringType.EQUALS, FilteringCondition.AND),
        FilteringParameter(name = "product_name", value = "Apple", type = FilteringType.CONTAINS, FilteringCondition.AND)
    )

    override val sort = listOf(
        "product_category",
        "product_name"
    )
}
