package io.opengood.data.jpa.provider

import app.TestApplication
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.opengood.commons.kotlin.extension.method.sortAscending
import io.opengood.commons.kotlin.extension.method.sortDescending
import io.opengood.commons.kotlin.infix.then
import io.opengood.data.jpa.provider.contract.DataResult
import io.opengood.data.jpa.provider.contract.Filtering
import io.opengood.data.jpa.provider.contract.FilteringCondition
import io.opengood.data.jpa.provider.contract.FilteringParameter
import io.opengood.data.jpa.provider.contract.FilteringType
import io.opengood.data.jpa.provider.contract.PageInfo
import io.opengood.data.jpa.provider.contract.Paging
import io.opengood.data.jpa.provider.contract.Sorting
import io.opengood.data.jpa.provider.contract.SortingDirection
import io.opengood.data.jpa.provider.contract.SortingParameter
import io.opengood.extensions.kotest.matcher.shouldBeEqualIgnoringKeys
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import test.spec.JpaDataProviderTestInput

@SpringBootTest(classes = [TestApplication::class])
@ExtendWith(SpringExtension::class)
abstract class AbstractDataProviderTest {
    abstract val testInput: JpaDataProviderTestInput

    lateinit var dataProvider: JpaDataProvider<*, *>
    lateinit var data: List<MutableMap<String, Any>>
    lateinit var filters: List<FilteringParameter>
    lateinit var sort: List<String>
    lateinit var filterCondition: FilteringCondition

    val requiredRecords = 5
    val requiredFilters = 2
    val requiredSort = 2

    val recordIndexFirst = 0

    val filterIndexFirst = 0
    val filterIndexNext = 1

    val sortIndexFirst = 0
    val sortIndexNext = 1

    val pageIndexFirst = 0
    val pageIndexNext = 1
    val pageIndexLast = 2

    val pageSize = 2
    val pageCount = (requiredRecords + pageSize - 1) / pageSize

    val recordRangeFirst: IntRange = 0..1
    val recordRangeNext: IntRange = 2..3
    val recordRangeLast: IntRange = 4..4

    @BeforeAll
    internal fun setUpBeforeClass() {
        dataProvider = testInput.dataProvider
        data = testInput.data
        filters = testInput.filters
        sort = testInput.sort
        filterCondition = (
            (testInput.filters.any { it.condition == FilteringCondition.AND }) then
                { FilteringCondition.AND }
        ) ?: FilteringCondition.OR

        with(testInput) {
            generateIds()
            setDependencyIds()
            saveDependencies()
        }
    }

    @AfterEach
    internal fun tearDown() {
        with(testInput) {
            deleteAll()
        }
    }

    @AfterAll
    internal fun tearDownAfterClass() {
        with(testInput) {
            deleteDependencies()
        }
    }

    @Test
    fun `Test input for data provider has required number of data entries`() {
        with(data) {
            isNotEmpty().shouldBeTrue()
            size shouldBe requiredRecords
        }
    }

    @Test
    fun `Test input for data provider has required number of filters`() {
        with(filters) {
            isNotEmpty().shouldBeTrue()
            size shouldBe requiredFilters
        }
    }

    @Test
    fun `Test input for data provider filters has all equals and contains types`() {
        filters.isNotEmpty().shouldBeTrue()
        filters[filterIndexFirst].type shouldBe FilteringType.EQUALS
        filters[filterIndexNext].type shouldBe FilteringType.CONTAINS
    }

    @Test
    fun `Test input for data provider has required number of sorts`() {
        with(sort) {
            isNotEmpty().shouldBeTrue()
            size shouldBe requiredSort
        }
    }

    @Test
    fun `Data provider receives identifier and deletes data when it exists in data repository`() {
        val results = dataProvider.save(data)

        val id = results[recordIndexFirst][dataProvider.getRowColumnMapping(dataProvider.id)]!!

        dataProvider.delete(id)

        dataProvider.exists(id).shouldBeFalse()
    }

    @Test
    fun `Data provider receives lists of identifiers and deletes all data when it exists in data repository`() {
        val results = dataProvider.save(data)

        val ids = results.map { it[dataProvider.getRowColumnMapping(dataProvider.id)]!! }

        dataProvider.deleteByIds(ids)

        ids.forEach {
            dataProvider.exists(it).shouldBeFalse()
        }
    }

    @Test
    fun `Data provider receives identifier and returns true when data exists in data repository`() {
        val results = dataProvider.save(data)

        val id = results[recordIndexFirst][dataProvider.getRowColumnMapping(dataProvider.id)]!!

        dataProvider.exists(id).shouldBeTrue()
    }

    @Test
    fun `Data provider receives identifier and returns false when data does not exist in data repository`() {
        val id = data[recordIndexFirst][dataProvider.getRowColumnMapping(dataProvider.id)]!!

        dataProvider.exists(id).shouldBeFalse()
    }

    @Test
    fun `Data provider receives paging request and returns first page of paginated results when data exists in data repository`() {
        val results = dataProvider.save(data)

        val result = dataProvider.get(paging = Paging(index = pageIndexFirst, size = pageSize))

        result.pageInfo.index shouldBe pageIndexFirst
        result.pageInfo.size shouldBe pageSize
        result.pageInfo.count shouldBe pageCount
        result.recordInfo.total shouldBe requiredRecords
        result.data shouldBe results.slice(recordRangeFirst)
    }

    @Test
    fun `Data provider receives paging request and returns next page of paginated results when data exists in data repository`() {
        val results = dataProvider.save(data)

        val result = dataProvider.get(paging = Paging(index = pageIndexNext, size = pageSize))

        result.pageInfo.index shouldBe pageIndexNext
        result.pageInfo.size shouldBe pageSize
        result.pageInfo.count shouldBe pageCount
        result.recordInfo.total shouldBe requiredRecords
        result.data shouldBe results.slice(recordRangeNext)
    }

    @Test
    fun `Data provider receives paging request and returns last page of paginated results when data exists in data repository`() {
        val results = dataProvider.save(data)

        val result = dataProvider.get(paging = Paging(index = pageIndexLast, size = pageSize))

        result.pageInfo.index shouldBe pageIndexLast
        result.pageInfo.size shouldBe pageSize
        result.pageInfo.count shouldBe pageCount
        result.recordInfo.total shouldBe requiredRecords
        result.data shouldBe results.slice(recordRangeLast)
    }

    @Test
    fun `Data provider receives paging request and returns no results when no data exists in data repository`() {
        val result = dataProvider.get(paging = Paging(index = pageIndexFirst, size = pageSize))

        result shouldBe DataResult.EMPTY
    }

    @Test
    fun `Data provider receives empty paging request and returns non-paginated results when data exists in data repository`() {
        val results = dataProvider.save(data)

        val result = dataProvider.get(paging = Paging.EMPTY)

        result.pageInfo shouldBe PageInfo.EMPTY
        result.recordInfo.total shouldBe requiredRecords
        result.data shouldBe results
    }

    @Test
    fun `Data provider receives empty paging request and returns no results when no data exists in data repository`() {
        val result = dataProvider.get(paging = Paging.EMPTY)

        result shouldBe DataResult.EMPTY
    }

    @Test
    fun `Data provider receives sorting request and returns sorted ascending results when data exists in data repository`() {
        val results = dataProvider.save(data)

        val result =
            dataProvider.get(
                paging = Paging(index = pageIndexFirst, size = pageSize),
                sorting =
                    Sorting(
                        params =
                            listOf(
                                SortingParameter(
                                    name = sort[sortIndexFirst],
                                    direction = SortingDirection.ASC,
                                ),
                            ),
                    ),
            )

        result.pageInfo.index shouldBe pageIndexFirst
        result.pageInfo.size shouldBe pageSize
        result.pageInfo.count shouldBe pageCount
        result.recordInfo.total shouldBe requiredRecords
        result.data shouldBe results.sortAscending(sort[sortIndexFirst]).slice(recordRangeFirst)
    }

    @Test
    fun `Data provider receives sorting request and returns sorted descending results when data exists in data repository`() {
        val results = dataProvider.save(data)

        val result =
            dataProvider.get(
                paging = Paging(index = pageIndexFirst, size = pageSize),
                sorting =
                    Sorting(
                        params =
                            listOf(
                                SortingParameter(
                                    name = sort[sortIndexFirst],
                                    direction = SortingDirection.DESC,
                                ),
                            ),
                    ),
            )

        result.pageInfo.index shouldBe pageIndexFirst
        result.pageInfo.size shouldBe pageSize
        result.pageInfo.count shouldBe pageCount
        result.recordInfo.total shouldBe requiredRecords
        result.data shouldBe results.sortDescending(sort[sortIndexFirst]).slice(recordRangeFirst)
    }

    @Test
    fun `Data provider receives sorting request and returns sorted multiple results when data exists in data repository`() {
        val results = dataProvider.save(data)

        val result =
            dataProvider.get(
                paging = Paging(index = pageIndexFirst, size = pageSize),
                sorting =
                    Sorting(
                        params =
                            listOf(
                                SortingParameter(
                                    name = sort[sortIndexFirst],
                                    direction = SortingDirection.ASC,
                                ),
                                SortingParameter(
                                    name = sort[sortIndexNext],
                                    direction = SortingDirection.ASC,
                                ),
                            ),
                    ),
            )

        result.pageInfo.index shouldBe pageIndexFirst
        result.pageInfo.size shouldBe pageSize
        result.pageInfo.count shouldBe pageCount
        result.recordInfo.total shouldBe requiredRecords
        result.data shouldBe
            results
                .sortedWith(
                    compareBy(
                        { it[sort[sortIndexFirst]] as String },
                        { it[sort[sortIndexNext]] as String },
                    ),
                ).slice(recordRangeFirst)
    }

    @Test
    fun `Data provider receives single equals type filter and returns filtered paginated results when data exists in data repository`() {
        if (filterCondition == FilteringCondition.OR) {
            val results = dataProvider.save(data)

            val result =
                dataProvider.get(
                    filtering = Filtering(params = listOf(filters[filterIndexFirst])),
                    paging = Paging(index = pageIndexFirst, size = pageSize),
                )

            result.pageInfo.index shouldBe pageIndexFirst
            result.pageInfo.size shouldBe pageSize
            result.pageInfo.count shouldBe 1
            result.recordInfo.total shouldBe 1
            result.data shouldBe
                results.filter {
                    it[filters[filterIndexFirst].name] == filters[filterIndexFirst].value
                }
        }
    }

    @Test
    fun `Data provider receives multiple equals and contains type filters and returns filtered paginated results when data exists in data repository`() {
        val results = dataProvider.save(data)

        val result =
            dataProvider.get(
                filtering = Filtering(params = filters),
                paging = Paging(index = pageIndexFirst, size = pageSize),
            )

        result.pageInfo.index shouldBe pageIndexFirst
        result.pageInfo.size shouldBe pageSize
        result.pageInfo.count shouldBe 1
        result.recordInfo.total shouldBe 2

        when (filterCondition) {
            FilteringCondition.AND -> {
                result.data shouldBe
                    results.filter {
                        it[filters[filterIndexFirst].name] == filters[filterIndexFirst].value &&
                            (it[filters[filterIndexNext].name] as String)
                                .contains(filters[filterIndexNext].value as String)
                    }
            }
            FilteringCondition.OR -> {
                result.data shouldBe
                    results.filter {
                        it[filters[filterIndexFirst].name] == filters[filterIndexFirst].value ||
                            (it[filters[filterIndexNext].name] as String)
                                .contains(filters[filterIndexNext].value as String)
                    }
            }
        }
    }

    @Test
    fun `Data provider receives single equals type filter and returns filtered non-paginated results when data exists in data repository`() {
        if (filterCondition == FilteringCondition.OR) {
            val results = dataProvider.save(data)

            val result =
                dataProvider.get(
                    filtering = Filtering(params = listOf(filters[filterIndexFirst])),
                )

            result.pageInfo shouldBe PageInfo.EMPTY
            result.recordInfo.total shouldBe 1
            result.data shouldBe
                results.filter {
                    it[filters[filterIndexFirst].name] == filters[filterIndexFirst].value
                }
        }
    }

    @Test
    fun `Data provider receives multiple equals and contains type filters and returns non-filtered paginated results when data exists in data repository`() {
        if (filterCondition == FilteringCondition.OR) {
            val results = dataProvider.save(data)

            val result = dataProvider.get(filtering = Filtering(params = filters))

            result.pageInfo shouldBe PageInfo.EMPTY
            result.recordInfo.total shouldBe 2

            when (filterCondition) {
                FilteringCondition.AND -> {
                    result.data shouldBe
                        results.filter {
                            it[filters[filterIndexFirst].name] == filters[filterIndexFirst].value &&
                                (it[filters[filterIndexNext].name] as String)
                                    .contains(filters[filterIndexNext].value as String)
                        }
                }
                FilteringCondition.OR -> {
                    result.data shouldBe
                        results.filter {
                            it[filters[filterIndexFirst].name] == filters[filterIndexFirst].value ||
                                (it[filters[filterIndexNext].name] as String)
                                    .contains(filters[filterIndexNext].value as String)
                        }
                }
            }
        }
    }

    @Test
    fun `Data provider receives identifier and returns result when data exists in data repository`() {
        val results = dataProvider.save(data)

        val id = results[recordIndexFirst][dataProvider.getRowColumnMapping(dataProvider.id)]!!

        val result = dataProvider.getById(id)

        result.shouldNotBeNull()
        result.shouldNotBeEmpty()
        result shouldBe
            results.first {
                it[filters[filterIndexFirst].name] == filters[filterIndexFirst].value
            }
    }

    @Test
    fun `Data provider receives identifier and does not return result when no data exists in data repository`() {
        val id = data[recordIndexFirst][dataProvider.getRowColumnMapping(dataProvider.id)]!!

        val result = dataProvider.getById(id)

        result.shouldNotBeNull()
        result.shouldBeEmpty()
    }

    @Test
    fun `Data provider converts data into entities and sends to data repository and returns results when data is saved`() {
        val results = dataProvider.save(data)

        results.shouldBeEqualIgnoringKeys(data, dataProvider.id)
    }

    @Test
    fun `Data provider does not convert data into entities and does not send to data repository and does not return results when data is not saved`() {
        val results = dataProvider.save(emptyList())

        results.shouldBeEmpty()
    }
}
