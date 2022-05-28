package io.opengood.data.jpa.provider

import app.TestApplication
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.math.RoundingMode
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

    override fun extensions() = listOf(SpringExtension)

    init {
        test("convertFromBigDecimal function converts non-null big decimal to string and returns non-null string") {
            convertFromBigDecimal(BigDecimal(100.45).setScale(2, RoundingMode.HALF_UP)) shouldBe "100.45"
        }

        test("convertFromBigDecimal function does not convert null big decimal to string and returns null string") {
            convertFromBigDecimal(null).shouldBeNull()
        }

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

        test("convertFromInt function converts non-null integer to string and returns non-null string") {
            convertFromInt(1) shouldBe "1"
        }

        test("convertFromInt function does not convert null integer to string and returns null string") {
            convertFromInt(null).shouldBeNull()
        }

        test("convertFromDouble function converts non-null double to string and returns non-null string") {
            convertFromDouble(1000.00) shouldBe "1000.0"
        }

        test("convertFromDouble function does not convert null double to string and returns null string") {
            convertFromDouble(null).shouldBeNull()
        }

        test("convertFromFloat function converts non-null float to string and returns non-null string") {
            convertFromFloat(10.00f) shouldBe "10.0"
        }

        test("convertFromFloat function does not convert null float to string and returns null string") {
            convertFromFloat(null).shouldBeNull()
        }

        test("convertFromLong function converts non-null long to string and returns non-null string") {
            convertFromLong(1000L) shouldBe "1000"
        }

        test("convertFromLong function does not convert null long to string and returns null string") {
            convertFromLong(null).shouldBeNull()
        }

        test("convertFromShort function converts non-null short to string and returns non-null string") {
            convertFromShort(100) shouldBe "100"
        }

        test("convertFromShort function does not convert null short to string and returns null string") {
            convertFromShort(null).shouldBeNull()
        }

        test("convertFromTimestamp function converts non-null SQL timestamp to string and returns non-null string") {
            convertFromTimestamp(sqlTimestamp) shouldBe timestampString
        }

        test("convertFromTimestamp function does not convert null SQL timestamp to string and returns null string") {
            convertFromTimestamp(null).shouldBeNull()
        }

        test("convertFromUuid function converts non-null UUID to string and returns non-null string") {
            convertFromUuid(uuid) shouldBe uuid.toString()
        }

        test("convertFromUuid function does not convert null UUID to string and returns null string") {
            convertFromUuid(null).shouldBeNull()
        }

        test("convertToBigDecimal function converts non-null object to big decimal and returns non-null big decimal") {
            convertToBigDecimal("100.45") shouldBe BigDecimal(100.45).setScale(2, RoundingMode.HALF_UP)
        }

        test("convertToBigDecimal function does not convert null object to big decimal and returns null big decimal") {
            convertToBigDecimal(null).shouldBeNull()
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

        test("convertToFloat function converts non-null object to float and returns non-null float") {
            convertToFloat("100.00") shouldBe 100.00
        }

        test("convertToFloat function does not convert null object to float and returns null float") {
            convertToFloat(null).shouldBeNull()
        }

        test("convertToLong function converts non-null object to long and returns non-null long") {
            convertToLong("1000") shouldBe 1000L
        }

        test("convertToLong function does not convert null object to long and returns null long") {
            convertToLong(null).shouldBeNull()
        }

        test("convertToInt function converts non-null object to integer and returns non-null integer") {
            convertToInt("1") shouldBe 1
        }

        test("convertToInt function does not convert null object to integer and returns null integer") {
            convertToInt(null).shouldBeNull()
        }

        test("convertToShort function converts non-null object to short and returns non-null short") {
            convertToShort("1") shouldBe 1
        }

        test("convertToShort function does not convert null object to short and returns null short") {
            convertToShort(null).shouldBeNull()
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
