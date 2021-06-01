package io.opengood.data.jpa.provider

import io.opengood.data.jpa.provider.contract.DataResult
import io.opengood.data.jpa.provider.contract.Paging
import io.opengood.data.jpa.provider.contract.Sorting
import io.opengood.data.jpa.provider.contract.SortingParameter
import io.opengood.data.jpa.provider.contract.getSort
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository

interface JpaDataProvider<T : Any, Id : Any> {
    val repository: JpaRepository<T, Id>
    val name: String
    val mappings: Map<String, String>

    fun filterMapper(filters: Map<String, Any>): T
    fun objectMapper(row: Map<String, Any>): T
    fun rowMapper(o: T): Map<String, Any>

    fun filter(
        filters: Map<String, Any>,
        pageable: Pageable = Pageable.unpaged()
    ): Page<T> {
        with(filters) {
            if (isNotEmpty()) {
                val o = filterMapper(filters)
                return repository.findAll(Example.of(o), pageable)
            }
        }
        return Page.empty()
    }

    fun get(
        filters: Map<String, Any> = emptyMap(),
        paging: Paging = Paging.EMPTY,
        sorting: Sorting = Sorting.EMPTY
    ): DataResult {
        if (paging != Paging.EMPTY) {
            var sort = Sort.unsorted()
            if (sorting != Sorting.EMPTY) {
                with(sorting.params) {
                    if (isNotEmpty()) {
                        forEachIndexed { i, it ->
                            val param = SortingParameter(getObjectMapping(it.name), it.direction)
                            with(param) {
                                if (i == 0) {
                                    sort = getSort()
                                } else {
                                    sort.and(getSort())
                                }
                            }
                        }
                    }
                }
            }

            val pageable = PageRequest.of(paging.index, paging.size, sort)
            val results = if (filters.isNotEmpty()) filter(filters, pageable) else repository.findAll(pageable)

            with(results) {
                if (hasContent()) {
                    val items = mutableListOf<Map<String, Any>>()
                    content.forEach { items.add(rowMapper(it)) }
                    return DataResult(
                        pages = DataResult.Page(
                            index = results.number,
                            size = results.size,
                            count = results.totalPages
                        ),
                        records = DataResult.Record(
                            total = results.totalElements
                        ),
                        data = items
                    )
                }
            }
            return DataResult.EMPTY
        } else {
            val results = (if (filters.isNotEmpty()) filter(filters) else repository.findAll()).toList()

            with(results) {
                if (isNotEmpty()) {
                    val items = mutableListOf<Map<String, Any>>()
                    forEach { items.add(rowMapper(it)) }
                    return DataResult(
                        pages = DataResult.Page.EMPTY,
                        records = DataResult.Record(
                            total = results.size.toLong()
                        ),
                        data = items
                    )
                }
            }
            return DataResult.EMPTY
        }
    }

    fun save(data: List<Map<String, Any>>): List<Map<String, Any>> {
        with(data) {
            if (isNotEmpty()) {
                val entities = mutableListOf<T>()
                forEach { entities.add(objectMapper(it)) }

                val results = repository.saveAll(entities).toList()
                with(results) {
                    if (isNotEmpty()) {
                        val items = mutableListOf<Map<String, Any>>()
                        forEach { items.add(rowMapper(it)) }
                        return items
                    }
                }
            }
            return emptyList()
        }
    }
}
