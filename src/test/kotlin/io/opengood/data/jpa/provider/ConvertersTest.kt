package io.opengood.data.jpa.provider

import helper.Values
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class ConvertersTest : FunSpec({

    test("convertFromDate function converts non-null SQL date to string and returns non-null string") {
        convertFromDate(Values.sqlDate) shouldBe Values.dateString
    }

    test("convertFromDate function does not convert null SQL date to string and returns null string") {
        convertFromDate(null).shouldBeNull()
    }

    test("convertFromUuid function converts non-null UUID to string and returns non-null string") {
        convertFromUuid(Values.uuid) shouldBe Values.uuid.toString()
    }

    test("convertFromUuid function does not convert null UUID to string and returns null string") {
        convertFromUuid(null).shouldBeNull()
    }

    test("convertToDate function converts non-null date string to SQL date and returns non-null SQL date") {
        convertToDate(Values.dateString)?.toString() shouldBe Values.sqlDate.toString()
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
        convertToUuid(Values.uuid.toString()) shouldBe Values.uuid
    }

    test("convertToUuid function does not convert null object to UUID and returns null UUID") {
        convertToUuid(null).shouldBeNull()
    }
})
