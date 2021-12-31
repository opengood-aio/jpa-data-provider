package spec

import io.opengood.data.jpa.provider.JpaDataProvider
import io.opengood.data.jpa.provider.contract.FilteringParameter
import io.opengood.data.jpa.provider.getRowColumnMapping
import java.util.UUID

interface JpaDataProviderTestInput {
    val name: String
    val dataProvider: JpaDataProvider<*, *>
    val data: List<MutableMap<String, Any>>
    val filters: List<FilteringParameter>
    val sort: List<String>

    fun getDependencies(): List<JpaDataProviderTestInput> = emptyList()

    fun deleteAll() {
        val ids = getIds(this)
        if (ids.isNotEmpty())
            dataProvider.deleteByIds(ids)
    }

    fun deleteDependencies() {
        val dependencies = getDependencies()
        if (dependencies.isNotEmpty()) {
            dependencies.forEach { input ->
                val ids = getIds(input)
                if (ids.isNotEmpty())
                    input.dataProvider.deleteByIds(ids)
            }
        }
    }

    fun generateIds() {
        data.forEach { row ->
            row[dataProvider.getRowColumnMapping(dataProvider.id)] = UUID.randomUUID().toString()
        }
    }

    fun getIds(input: JpaDataProviderTestInput): List<Any> {
        return input.data
            .map { it[input.dataProvider.getRowColumnMapping(input.dataProvider.id)]!! }
            .filter { input.dataProvider.exists(it) }
    }

    fun saveDependencies() {
        val dependencies = getDependencies()
        if (dependencies.isNotEmpty()) {
            dependencies.forEach { input ->
                input.dataProvider.save(input.data)
            }
        }
    }

    fun setDependencyIds() {
        val dependencies = getDependencies()
        if (dependencies.isNotEmpty()) {
            dependencies.forEach { input ->
                input.generateIds()

                val id = input.dataProvider.getRowColumnMapping(input.dataProvider.id)
                data.forEachIndexed { index, row ->
                    row[id] = input.data[index][id]!!
                }
            }
        }
    }
}
