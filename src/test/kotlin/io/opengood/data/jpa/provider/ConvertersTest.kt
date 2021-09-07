package io.opengood.data.jpa.provider

import app.TestApplication
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID
import java.sql.Date as SqlDate
import java.sql.Timestamp as SqlTimestamp

@SpringBootTest(classes = [TestApplication::class])
class ConvertersTest : FunSpec() {

    @Autowired
    lateinit var dateString: String

    @Autowired
    lateinit var sqlDate: SqlDate

    @Autowired
    lateinit var sqlTimestamp: SqlTimestamp

    @Autowired
    lateinit var timestampString: String

    @Autowired
    lateinit var uuid: UUID

    override fun listeners() = listOf(SpringListener)

    init {
        test("convertFromBoolean function converts non-null boolean to string and returns non-null string") {
            convertFromBoolean(true) shouldBe "true"
        }

        test("convertFromBoolean function does not convert null boolean to string and returns null string") {
            convertFromBoolean(null).shouldBeNull()
        }

        test("convertFromDate function converts non-null SQL date to string and returns non-null string") {
            convertFromDate(sqlDate) shouldBe dateString
        }

        test("convertFromDate function does not convert null SQL date to string and returns null string") {
            convertFromDate(null).shouldBeNull()
        }

        test("convertFromUuid function converts non-null UUID to string and returns non-null string") {
            convertFromUuid(uuid) shouldBe uuid.toString()
        }

        test("convertFromInt function converts non-null integer to string and returns non-null string") {
            convertFromInt(1) shouldBe "1"
        }

        test("convertFromInt function does not convert null integer to string and returns null string") {
            convertFromInt(null).shouldBeNull()
        }

        test("convertFromTimestamp function converts non-null SQL timestamp to string and returns non-null string") {
            convertFromTimestamp(sqlTimestamp) shouldBe timestampString
        }

        test("convertFromTimestamp function does not convert null SQL timestamp to string and returns null string") {
            convertFromTimestamp(null).shouldBeNull()
        }

        test("convertFromUuid function does not convert null UUID to string and returns null string") {
            convertFromUuid(null).shouldBeNull()
        }

        test("convertToBoolean function converts non-null object to boolean and returns non-null boolean") {
            convertToBoolean("true")?.shouldBeTrue()
        }

        test("convertToBoolean function does not convert null object to boolean and returns null boolean") {
            convertToBoolean(null).shouldBeNull()
        }

        test("convertToDate function converts non-null date string to SQL date and returns non-null SQL date") {
            convertToDate(dateString)?.toString() shouldBe sqlDate.toString()
        }

        test("convertToDate function does not convert null date string to SQL date and returns null SQL date") {
            convertToDate(null).shouldBeNull()
        }

        test("convertToInt function converts non-null object to integer and returns non-null integer") {
            convertToInt("1") shouldBe 1
        }

        test("convertToInt function does not convert null object to integer and returns null integer") {
            convertToInt(null).shouldBeNull()
        }

        test("convertToString function converts non-null object to string and returns non-null string") {
            convertToString(1) shouldBe "1"
        }

        test("convertToString function does not convert null object to string and returns null string") {
            convertToString(null).shouldBeNull()
        }

        test("convertToTimestamp function converts non-null timestamp string to SQL timestamp and returns non-null SQL timestamp") {
            convertToTimestamp(timestampString)?.toString() shouldBe sqlTimestamp.toString()
        }

        test("convertToTimestamp function does not convert null timestamp string to SQL timestamp and returns null SQL timestamp") {
            convertToTimestamp(null).shouldBeNull()
        }

        test("convertToUuid function converts non-null object to UUID and returns non-null UUID") {
            convertToUuid(uuid.toString()) shouldBe uuid
        }

        test("convertToUuid function does not convert null object to UUID and returns null UUID") {
            convertToUuid(null).shouldBeNull()
        }
    }
}
