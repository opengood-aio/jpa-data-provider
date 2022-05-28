package spec

import io.opengood.commons.kotlin.extension.method.notContainsKey
import io.opengood.data.jpa.provider.JpaDataProvider
import io.opengood.data.jpa.provider.contract.FilteringParameter
import io.opengood.data.jpa.provider.getRowColumnMapping
import java.util.UUID

interface JpaDataProviderTestInput {
    val dataProvider: JpaDataProvider<*, *>
    val data: List<MutableMap<String, Any>>
    val filters: List<FilteringParameter>
    val sort: List<String>

    fun getDependencies(): List<JpaDataProviderTestInput> = emptyList()

    fun deleteAll() {
        val ids = getIds(this)
        if (ids.isNotEmpty()) {
            dataProvider.deleteByIds(ids)
            saveDependencies()
        }
    }

    fun deleteDependencies() {
        val dependencies = getDependencies()
        if (dependencies.isNotEmpty()) {
            dependencies.forEach { parent ->
                val ids = getIds(parent)
                if (ids.isNotEmpty())
                    parent.dataProvider.deleteByIds(ids)
            }

            dependencies.forEach { parent ->
                if (parent.getDependencies().isNotEmpty()) {
                    parent.deleteDependencies()
                }
            }
        }
    }

    fun generateIds() {
        data.forEach { row ->
            val id = dataProvider.getRowColumnMapping(dataProvider.id)
            if (row.notContainsKey(id)) row[id] = UUID.randomUUID().toString()
        }
    }

    fun getIds(input: JpaDataProviderTestInput): List<Any> =
        input.data
            .map { it[input.dataProvider.getRowColumnMapping(input.dataProvider.id)]!! }
            .filter { input.dataProvider.exists(it) }

    fun saveDependencies() {
        val dependencies = getDependencies()
        if (dependencies.isNotEmpty()) {
            dependencies.forEach { parent ->
                if (parent.getDependencies().isNotEmpty()) {
                    parent.saveDependencies()
                }
                parent.dataProvider.save(parent.data)
            }
        }
    }

    fun setDependencyIds() {
        val dependencies = getDependencies()
        if (dependencies.isNotEmpty()) {
            dependencies.forEach { parent ->
                parent.generateIds()
                if (parent.getDependencies().isNotEmpty()) {
                    parent.setDependencyIds()
                }

                val id = parent.dataProvider.getRowColumnMapping(parent.dataProvider.id)
                data.forEachIndexed { index, row ->
                    row[id] = parent.data[index][id]!!
                }
            }
        }
    }
}
