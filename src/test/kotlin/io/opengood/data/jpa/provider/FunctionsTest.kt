package io.opengood.data.jpa.provider

import app.DataProvider
import app.DataRepository
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
import io.opengood.commons.kotlin.function.makeEntry
import io.opengood.extensions.kotest.matcher.shouldBeMapEntry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [DataProvider::class, DataRepository::class])
class FunctionsTest : WordSpec() {

    @MockkBean
    lateinit var repository: DataRepository

    @Autowired
    lateinit var dataProvider: DataProvider

    override fun listeners() = listOf(SpringListener)

    init {
        "JPA Data Provider" should {
            "have getObjectMapping extension function return object mapping given row key" {
                with(dataProvider) {
                    getObjectMapping("product_name") shouldBe "name"
                }
            }

            "have getObjectMapping extension function throw exception when row key does not exist" {
                shouldThrow<NoSuchElementException> {
                    with(dataProvider) {
                        getObjectMapping("none")
                    }
                }
            }

            "have getRowMapping extension function return row mapping given object key" {
                with(dataProvider) {
                    getRowMapping("name") shouldBe "product_name"
                }
            }

            "have getRowMapping extension function throw exception when object key does not exist" {
                shouldThrow<NoSuchElementException> {
                    with(dataProvider) {
                        getRowMapping("none")
                    }
                }
            }

            "have nullableObjectValue extension function convert row map entry and return non-null object when row contains entry" {
                with(dataProvider) {
                    nullableObjectValue("name", mapOf("product_name" to "foo"), convertToString) shouldBe "foo"
                }
            }

            "have nullableObjectValue extension function not convert row map entry and return null object when row does not contain entry" {
                with(dataProvider) {
                    nullableObjectValue("name", emptyMap(), convertToString).shouldBeNull()
                }
            }

            "have nullableObjectValue extension function not convert row map entry and throws exception when mapping not found" {
                shouldThrow<IllegalArgumentException> {
                    with(dataProvider) {
                        nullableObjectValue("none", mapOf("product_name" to "foo"), convertToString)
                    }
                }
            }

            "have nullableRowValue extension function convert object value and return row entry when object is not null" {
                with(dataProvider) {
                    nullableRowValue("name", "foo", convertToString)?.shouldBeMapEntry(makeEntry("product_name", "foo"))
                }
            }

            "have nullableRowValue extension function not convert object value and return null when object is null" {
                with(dataProvider) {
                    nullableRowValue("name", null, convertToString).shouldBeNull()
                }
            }

            "have nullableRowValue extension function not convert object value and throws exception when mapping not found" {
                shouldThrow<IllegalArgumentException> {
                    with(dataProvider) {
                        nullableRowValue("none", "foo", convertToString)
                    }
                }
            }

            "have objectValue extension function convert row map entry and return non-null object when row contains entry" {
                with(dataProvider) {
                    objectValue("name", mapOf("product_name" to "foo"), "bar", convertToString) shouldBe "foo"
                }
            }

            "have objectValue extension function not convert row map entry and return default object when row does not contain entry" {
                with(dataProvider) {
                    objectValue("id", mapOf("product_name" to "foo"), "bar", convertToString) shouldBe "bar"
                }
            }

            "have objectValue extension function not convert row map entry and throws exception when mapping not found" {
                shouldThrow<IllegalArgumentException> {
                    with(dataProvider) {
                        objectValue("none", mapOf("product_name" to "foo"), "bar", convertToString)
                    }
                }
            }

            "have rowValue extension function convert object value and return row entry when object is not null" {
                with(dataProvider) {
                    rowValue("name", "foo", "bar", convertToString) shouldBeMapEntry makeEntry("product_name", "foo")
                }
            }

            "have rowValue extension function not convert object value and return row entry with default value when object is null" {
                with(dataProvider) {
                    rowValue("name", null, "bar", convertToString) shouldBeMapEntry makeEntry("product_name", "bar")
                }
            }

            "have rowValue extension function not convert object value and throws exception when mapping not found" {
                shouldThrow<IllegalArgumentException> {
                    with(dataProvider) {
                        rowValue("none", "foo", "bar", convertToString)
                    }
                }
            }
        }
    }
}
