package io.opengood.data.jpa.provider

import io.opengood.commons.kotlin.infix.then
import io.opengood.data.jpa.provider.contract.DataResult
import io.opengood.data.jpa.provider.contract.Filtering
import io.opengood.data.jpa.provider.contract.FilteringCondition
import io.opengood.data.jpa.provider.contract.FilteringParameter
import io.opengood.data.jpa.provider.contract.PageInfo
import io.opengood.data.jpa.provider.contract.Paging
import io.opengood.data.jpa.provider.contract.RecordInfo
import io.opengood.data.jpa.provider.contract.Sorting
import io.opengood.data.jpa.provider.contract.SortingParameter
import io.opengood.data.jpa.provider.contract.getMatcher
import io.opengood.data.jpa.provider.contract.getSort
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository

interface JpaDataProvider<T : Any, Id : Any> {
    val repository: JpaRepository<T, Id>
    val name: String
    val id: String
    val mappings: Map<String, String>

    fun idConverter(id: Any): Id

    fun filterMapper(filters: Map<String, Any>): T
    fun objectFieldMapper(row: Map<String, Any>): T
    fun rowColumnMapper(o: T): Map<String, Any>

    fun delete(id: Any) {
        val entityId = idConverter(id)
        repository.deleteById(entityId)
    }

    fun deleteByIds(ids: List<Any>) {
        val entityIds = ids.map { idConverter(it) }
        repository.deleteAllByIdInBatch(entityIds)
    }

    fun exists(id: Any): Boolean {
        val entityId = idConverter(id)
        return repository.existsById(entityId)
    }

    fun get(
        filtering: Filtering = Filtering.EMPTY,
        paging: Paging = Paging.EMPTY,
        sorting: Sorting = Sorting.EMPTY
    ): DataResult {
        if (paging != Paging.EMPTY) {
            val filters = filters(filtering)
            val matcher = matcher(filtering)
            val pageable = PageRequest.of(paging.index, paging.size, sort(sorting))

            val results = query(filters = filters, matcher = matcher, pageable = pageable)
            return with(results) {
                if (hasContent()) {
                    DataResult(
                        pageInfo = PageInfo(
                            index = results.number,
                            size = results.size,
                            count = results.totalPages
                        ),
                        recordInfo = RecordInfo(
                            total = results.totalElements
                        ),
                        data = content.map { rowColumnMapper(it) }.toList()
                    )
                } else {
                    DataResult.EMPTY
                }
            }
        } else {
            val filters = filters(filtering)
            val matcher = matcher(filtering)

            val results = query(filters = filters, matcher = matcher)
            return with(results) {
                if (hasContent()) {
                    DataResult(
                        pageInfo = PageInfo.EMPTY,
                        recordInfo = RecordInfo(
                            total = results.size.toLong()
                        ),
                        data = content.map { rowColumnMapper(it) }.toList()
                    )
                } else {
                    DataResult.EMPTY
                }
            }
        }
    }

    fun getById(id: Any): Map<String, Any> {
        val entityId = idConverter(id)
        val result = repository.findById(entityId)
        return result.map { rowColumnMapper(it) }.orElse(emptyMap())
    }

    fun save(data: List<Map<String, Any>>): List<Map<String, Any>> {
        with(data) {
            if (isNotEmpty()) {
                val entities = mutableListOf<T>()
                forEach { entities.add(objectFieldMapper(it)) }

                val results = repository.saveAll(entities).toList()
                with(results) {
                    if (isNotEmpty()) {
                        return map { rowColumnMapper(it) }.toList()
                    }
                }
            }
            return emptyList()
        }
    }

    private fun filters(filtering: Filtering): Map<String, Any> {
        return with(filtering.params) {
            if (isNotEmpty()) associate { it.name to it.value } else emptyMap()
        }
    }

    private fun matcher(filtering: Filtering): ExampleMatcher {
        var matcher = matcherCondition(filtering)
        if (filtering != Filtering.EMPTY) {
            with(filtering.params) {
                if (isNotEmpty()) {
                    forEach {
                        val param = FilteringParameter(getObjectFieldMapping(it.name), it.value, it.type, it.condition)
                        with(param) {
                            matcher = getMatcher(matcher)
                        }
                    }
                }
            }
        } else {
            matcher = FilteringParameter.defaultMatcher
        }
        return matcher
    }

    private fun matcherCondition(filtering: Filtering): ExampleMatcher =
        ((filtering.params.any { it.condition == FilteringCondition.AND }) then { ExampleMatcher.matchingAll() })
            ?: ExampleMatcher.matchingAny()

    private fun sort(sorting: Sorting): Sort {
        var sort = Sort.unsorted()
        if (sorting != Sorting.EMPTY) {
            with(sorting.params) {
                if (isNotEmpty()) {
                    forEachIndexed { i, it ->
                        val param = SortingParameter(getObjectFieldMapping(it.name), it.direction)
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
        return sort
    }

    private fun query(
        filters: Map<String, Any>,
        matcher: ExampleMatcher,
        pageable: Pageable = Pageable.unpaged()
    ): Page<T> {
        return with(filters) {
            if (isNotEmpty()) {
                val o = filterMapper(filters)
                repository.findAll(Example.of(o, matcher), pageable)
            } else {
                repository.findAll(pageable)
            }
        }
    }
}
