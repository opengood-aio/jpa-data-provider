package io.opengood.data.jpa.provider

import app.DataProvider
import app.TestApplication
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
import io.opengood.commons.kotlin.function.makeEntry
import io.opengood.extensions.kotest.matcher.shouldBeMapEntry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [TestApplication::class])
class FunctionsTest : WordSpec() {

    @Autowired
    lateinit var dataProvider: DataProvider

    override fun listeners() = listOf(SpringListener)

    init {
        "JPA Data Provider" should {
            "have getObjectFieldMapping extension function return object field mapping given row column key" {
                with(dataProvider) {
                    getObjectFieldMapping("product_name") shouldBe "name"
                }
            }

            "have getObjectFieldMapping extension function throw exception when row column key does not exist" {
                shouldThrow<NoSuchElementException> {
                    with(dataProvider) {
                        getObjectFieldMapping("none")
                    }
                }
            }

            "have getRowColumnMapping extension function return row column mapping given object field key" {
                with(dataProvider) {
                    getRowColumnMapping("name") shouldBe "product_name"
                }
            }

            "have getRowColumnMapping extension function throw exception when object field key does not exist" {
                shouldThrow<NoSuchElementException> {
                    with(dataProvider) {
                        getRowColumnMapping("none")
                    }
                }
            }

            "have nullableObjectFieldValue extension function convert row column map entry and return non-null object field value when row contains column entry" {
                with(dataProvider) {
                    nullableObjectFieldValue("name", mapOf("product_name" to "foo"), convertToString) shouldBe "foo"
                }
            }

            "have nullableObjectFieldValue extension function not convert row column map entry and return null object field value when row does not contain column entry" {
                with(dataProvider) {
                    nullableObjectFieldValue("name", emptyMap(), convertToString).shouldBeNull()
                }
            }

            "have nullableObjectFieldValue extension function not convert row column map entry and throws exception when mapping not found" {
                shouldThrow<IllegalArgumentException> {
                    with(dataProvider) {
                        nullableObjectFieldValue("none", mapOf("product_name" to "foo"), convertToString)
                    }
                }
            }

            "have nullableRowColumnValue extension function convert object field value and return row column entry when object field value is not null" {
                with(dataProvider) {
                    nullableRowColumnValue("name", "foo", convertToString)?.shouldBeMapEntry(makeEntry("product_name", "foo"))
                }
            }

            "have nullableRowColumnValue extension function not convert object field value and return null when object field value is null" {
                with(dataProvider) {
                    nullableRowColumnValue("name", null, convertToString).shouldBeNull()
                }
            }

            "have nullableRowColumnValue extension function not convert object field value and throws exception when mapping not found" {
                shouldThrow<IllegalArgumentException> {
                    with(dataProvider) {
                        nullableRowColumnValue("none", "foo", convertToString)
                    }
                }
            }

            "have objectFieldValue extension function convert row column map entry and return non-null object field value when row contains column entry" {
                with(dataProvider) {
                    objectFieldValue("name", mapOf("product_name" to "foo"), "bar", convertToString) shouldBe "foo"
                }
            }

            "have objectFieldValue extension function not convert row column map entry and return default object field value when row does not contain column entry" {
                with(dataProvider) {
                    objectFieldValue("id", mapOf("product_name" to "foo"), "bar", convertToString) shouldBe "bar"
                }
            }

            "have objectFieldValue extension function not convert row column map entry and throws exception when mapping not found" {
                shouldThrow<IllegalArgumentException> {
                    with(dataProvider) {
                        objectFieldValue("none", mapOf("product_name" to "foo"), "bar", convertToString)
                    }
                }
            }

            "have rowColumnValue extension function convert object field value and return row column entry when object field value is not null" {
                with(dataProvider) {
                    rowColumnValue("name", "foo", "bar", convertToString) shouldBeMapEntry makeEntry("product_name", "foo")
                }
            }

            "have rowColumnValue extension function not convert object field value and return row column entry with default field value when object field value is null" {
                with(dataProvider) {
                    rowColumnValue("name", null, "bar", convertToString) shouldBeMapEntry makeEntry("product_name", "bar")
                }
            }

            "have rowColumnValue extension function not convert object field value and throws exception when mapping not found" {
                shouldThrow<IllegalArgumentException> {
                    with(dataProvider) {
                        rowColumnValue("none", "foo", "bar", convertToString)
                    }
                }
            }
        }
    }
}
