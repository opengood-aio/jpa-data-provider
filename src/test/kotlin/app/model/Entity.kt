package app.model

import io.opengood.commons.kotlin.annotation.NoArg
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

@Entity(name = "products")
@NoArg
data class Entity(
    @Id
    val id: UUID?,
    val name: String?,
    val sku: String?,
    val category: String?,
)
