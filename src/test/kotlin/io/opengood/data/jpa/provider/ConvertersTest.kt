package io.opengood.data.jpa.provider

import app.TestApplication
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID
import java.sql.Date as SqlDate

@SpringBootTest(classes = [TestApplication::class])
class ConvertersTest : FunSpec() {

    @Autowired
    lateinit var dateString: String

    @Autowired
    lateinit var sqlDate: SqlDate

    @Autowired
    lateinit var uuid: UUID

    override fun listeners() = listOf(SpringListener)

    init {
        test("convertFromDate function converts non-null SQL date to string and returns non-null string") {
            convertFromDate(sqlDate) shouldBe dateString
        }

        test("convertFromDate function does not convert null SQL date to string and returns null string") {
            convertFromDate(null).shouldBeNull()
        }

        test("convertFromUuid function converts non-null UUID to string and returns non-null string") {
            convertFromUuid(uuid) shouldBe uuid.toString()
        }

        test("convertFromUuid function does not convert null UUID to string and returns null string") {
            convertFromUuid(null).shouldBeNull()
        }

        test("convertToDate function converts non-null date string to SQL date and returns non-null SQL date") {
            convertToDate(dateString)?.toString() shouldBe sqlDate.toString()
        }

        test("convertToDate function does not convert null date string to SQL date and returns null SQL date") {
            convertToDate(null).shouldBeNull()
        }

        test("convertToString function converts non-null object to string and returns non-null string") {
            convertToString(1) shouldBe "1"
        }

        test("convertToString function does not convert null object to string and returns null string") {
            convertToString(null).shouldBeNull()
        }

        test("convertToUuid function converts non-null object to UUID and returns non-null UUID") {
            convertToUuid(uuid.toString()) shouldBe uuid
        }

        test("convertToUuid function does not convert null object to UUID and returns null UUID") {
            convertToUuid(null).shouldBeNull()
        }
    }
}
