package app.model

import io.opengood.commons.kotlin.annotation.NoArg
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "products")
@NoArg
data class Entity(
    @Id
    val id: UUID?,
    val name: String?,
    val sku: String?,
    val category: String?
)
