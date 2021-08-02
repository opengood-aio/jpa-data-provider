package io.opengood.data.jpa.provider

import app.TestApplication
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
import io.opengood.commons.kotlin.extension.method.keyByIndex
import io.opengood.commons.kotlin.extension.method.sortAscending
import io.opengood.commons.kotlin.extension.method.sortDescending
import io.opengood.commons.kotlin.extension.method.valueByIndex
import io.opengood.data.jpa.provider.contract.DataResult
import io.opengood.data.jpa.provider.contract.Paging
import io.opengood.data.jpa.provider.contract.Sorting
import io.opengood.data.jpa.provider.contract.SortingDirection
import io.opengood.data.jpa.provider.contract.SortingParameter
import io.opengood.extensions.kotest.matcher.shouldBeEqualIgnoringKeys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spec.JpaDataProviderTestInput

@SpringBootTest(classes = [TestApplication::class])
class JpaDataProviderTest : FreeSpec() {

    @Autowired
    lateinit var testInput: List<JpaDataProviderTestInput>

    override fun listeners() = listOf(SpringListener)

    init {
        "JPA Data Provider" - {
            val requiredRecords = 5
            val requiredFilters = 2
            val requiredSort = 2

            val pageIndexFirst = 0
            val pageIndexNext = 1
            val pageIndexLast = 2
            val pageSize = 2
            val pageCount = (requiredRecords + pageSize - 1) / pageSize

            testInput.forEach { input ->
                with(input) {
                    afterTest {
                        dataProvider.repository.deleteAll()
                    }

                    "Test input for ${dataProvider.name} has $requiredRecords data entries" {
                        with(data) {
                            isNotEmpty().shouldBeTrue()
                            size shouldBe requiredRecords
                        }
                    }

                    "Test input for ${dataProvider.name} has $requiredFilters filter items" {
                        with(filters) {
                            isNotEmpty().shouldBeTrue()
                            size shouldBe requiredFilters
                        }
                    }

                    "Test input for ${dataProvider.name} has $requiredSort sort items" {
                        with(sort) {
                            isNotEmpty().shouldBeTrue()
                            size shouldBe requiredSort
                        }
                    }

                    "${dataProvider.name} receives paging request and returns first page of paginated results when data exists in data repository" {
                        val results = dataProvider.save(data)

                        val result = dataProvider.get(paging = Paging(index = pageIndexFirst, size = pageSize))

                        result.pages.index shouldBe pageIndexFirst
                        result.pages.size shouldBe pageSize
                        result.pages.count shouldBe pageCount
                        result.records.total shouldBe requiredRecords
                        result.data shouldBe results.slice(0..1)
                    }

                    "${dataProvider.name} receives paging request and returns next page of paginated results when data exists in data repository" {
                        val results = dataProvider.save(data)

                        val result = dataProvider.get(paging = Paging(index = pageIndexNext, size = pageSize))

                        result.pages.index shouldBe pageIndexNext
                        result.pages.size shouldBe pageSize
                        result.pages.count shouldBe pageCount
                        result.records.total shouldBe requiredRecords
                        result.data shouldBe results.slice(2..3)
                    }

                    "${dataProvider.name} receives paging request and returns last page of paginated results when data exists in data repository" {
                        val results = dataProvider.save(data)

                        val result = dataProvider.get(paging = Paging(index = pageIndexLast, size = pageSize))

                        result.pages.index shouldBe pageIndexLast
                        result.pages.size shouldBe pageSize
                        result.pages.count shouldBe pageCount
                        result.records.total shouldBe requiredRecords
                        result.data shouldBe results.slice(4..4)
                    }

                    "${dataProvider.name} receives paging request and returns no results when no data exists in data repository" {
                        val result = dataProvider.get(paging = Paging(index = pageIndexFirst, size = pageSize))

                        result shouldBe DataResult.EMPTY
                    }

                    "${dataProvider.name} receives empty paging request and returns non-paginated results when data exists in data repository" {
                        val results = dataProvider.save(data)

                        val result = dataProvider.get(paging = Paging.EMPTY)

                        result.pages shouldBe DataResult.Page.EMPTY
                        result.records.total shouldBe requiredRecords
                        result.data shouldBe results
                    }

                    "${dataProvider.name} receives empty paging request and returns no results when no data exists in data repository" {
                        val result = dataProvider.get(paging = Paging.EMPTY)

                        result shouldBe DataResult.EMPTY
                    }

                    "${dataProvider.name} receives sorting request and returns sorted ascending results when data exists in data repository" {
                        val results = dataProvider.save(data)

                        val result = dataProvider.get(
                            paging = Paging(index = pageIndexFirst, size = pageSize),
                            sorting = Sorting(
                                params = listOf(
                                    SortingParameter(
                                        name = sort[0],
                                        direction = SortingDirection.ASC
                                    )
                                )
                            )
                        )

                        result.pages.index shouldBe pageIndexFirst
                        result.pages.size shouldBe pageSize
                        result.pages.count shouldBe pageCount
                        result.records.total shouldBe requiredRecords
                        result.data shouldBe results.sortAscending(sort[0]).slice(0..1)
                    }

                    "${dataProvider.name} receives sorting request and returns sorted descending results when data exists in data repository" {
                        val results = dataProvider.save(data)

                        val result = dataProvider.get(
                            paging = Paging(index = pageIndexFirst, size = pageSize),
                            sorting = Sorting(
                                params = listOf(
                                    SortingParameter(
                                        name = sort[0],
                                        direction = SortingDirection.DESC
                                    )
                                )
                            )
                        )

                        result.pages.index shouldBe pageIndexFirst
                        result.pages.size shouldBe pageSize
                        result.pages.count shouldBe pageCount
                        result.records.total shouldBe requiredRecords
                        result.data shouldBe results.sortDescending(sort[0]).slice(0..1)
                    }

                    "${dataProvider.name} receives sorting request and returns sorted multiple results when data exists in data repository" {
                        val results = dataProvider.save(data)

                        val result = dataProvider.get(
                            paging = Paging(index = pageIndexFirst, size = pageSize),
                            sorting = Sorting(
                                params = listOf(
                                    SortingParameter(
                                        name = sort[0],
                                        direction = SortingDirection.ASC
                                    ),
                                    SortingParameter(
                                        name = sort[1],
                                        direction = SortingDirection.ASC
                                    )
                                )
                            )
                        )

                        result.pages.index shouldBe pageIndexFirst
                        result.pages.size shouldBe pageSize
                        result.pages.count shouldBe pageCount
                        result.records.total shouldBe requiredRecords
                        result.data shouldBe results.sortedWith(
                            compareBy(
                                { it[sort[0]] as String },
                                { it[sort[1]] as String }
                            )
                        ).slice(0..1)
                    }

                    "${dataProvider.name} receives single filter and returns filtered paginated results when data exists in data repository" {
                        val results = dataProvider.save(data)

                        val result = dataProvider.get(
                            filters = mapOf(filters.keyByIndex(0) to filters.valueByIndex(0)),
                            paging = Paging(index = pageIndexFirst, size = pageSize)
                        )

                        result.pages.index shouldBe pageIndexFirst
                        result.pages.size shouldBe pageSize
                        result.pages.count shouldBe 1
                        result.records.total shouldBe 1
                        result.data shouldBe results.filter {
                            it[filters.keyByIndex(0)] == filters.valueByIndex(0)
                        }
                    }

                    "${dataProvider.name} receives multiple filters and returns filtered paginated results when data exists in data repository" {
                        val results = dataProvider.save(data)

                        val result = dataProvider.get(
                            filters = filters,
                            paging = Paging(index = pageIndexFirst, size = pageSize)
                        )

                        result.pages.index shouldBe pageIndexFirst
                        result.pages.size shouldBe pageSize
                        result.pages.count shouldBe 1
                        result.records.total shouldBe 2
                        result.data shouldBe results.filter {
                            it[filters.keyByIndex(0)] == filters.valueByIndex(0) ||
                                (it[filters.keyByIndex(1)] as String).contains(filters.valueByIndex(1) as String)
                        }
                    }

                    "${dataProvider.name} receives single filter and returns filtered non-paginated results when data exists in data repository" {
                        val results = dataProvider.save(data)

                        val result = dataProvider.get(filters = mapOf(filters.keyByIndex(0) to filters.valueByIndex(0)))

                        result.pages shouldBe DataResult.Page.EMPTY
                        result.records.total shouldBe 1
                        result.data shouldBe results.filter {
                            it[filters.keyByIndex(0)] == filters.valueByIndex(0)
                        }
                    }

                    "${dataProvider.name} receives multiple filters and returns non-filtered paginated results when data exists in data repository" {
                        val results = dataProvider.save(data)

                        val result = dataProvider.get(filters = filters)

                        result.pages shouldBe DataResult.Page.EMPTY
                        result.records.total shouldBe 2
                        result.data shouldBe results.filter {
                            it[filters.keyByIndex(0)] == filters.valueByIndex(0) ||
                                (it[filters.keyByIndex(1)] as String).contains(filters.valueByIndex(1) as String)
                        }
                    }

                    "${dataProvider.name} converts data into entities and sends to data repository and returns results when data is saved" {
                        val results = dataProvider.save(data)

                        results.shouldBeEqualIgnoringKeys(data, *ignoreKeys.toTypedArray())
                    }

                    "${dataProvider.name} does not convert data into entities and does not send to data repository and does not return results when data is not saved" {
                        val results = dataProvider.save(emptyList())

                        results.shouldBeEmpty()
                    }
                }
            }
        }
    }
}
