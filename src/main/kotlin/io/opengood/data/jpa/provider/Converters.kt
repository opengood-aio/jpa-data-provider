package io.opengood.data.jpa.provider

import io.opengood.data.jpa.provider.constant.Formats
import java.math.BigDecimal
import java.util.UUID
import java.sql.Date as SqlDate
import java.sql.Timestamp as SqlTimestamp

val convertFromBigDecimal: (BigDecimal?) -> String? = {
    it?.toString()
}

val convertFromBoolean: (Boolean?) -> String? = {
    it?.toString()
}

val convertFromDate: (SqlDate?) -> String? = {
    if (it != null) Formats.SQL_DATE.format(it) else null
}

val convertFromDouble: (Double?) -> String? = {
    it?.toString()
}

val convertFromFloat: (Float?) -> String? = {
    it?.toString()
}

val convertFromInt: (Int?) -> String? = {
    it?.toString()
}

val convertFromLong: (Long?) -> String? = {
    it?.toString()
}

val convertFromShort: (Short?) -> String? = {
    it?.toString()
}

val convertFromTimestamp: (SqlTimestamp?) -> String? = {
    if (it != null) Formats.SQL_DATE_TIME.format(it) else null
}

val convertFromUuid: (UUID?) -> String? = {
    it?.toString()
}

val convertToBigDecimal: (Any?) -> BigDecimal? = {
    it?.toString()?.toBigDecimal()
}

val convertToBoolean: (Any?) -> Boolean? = {
    it?.toString()?.toBoolean()
}

val convertToDate: (Any?) -> SqlDate? = {
    var sqlDate: SqlDate? = null
    if (it != null) {
        val date = Formats.SQL_DATE.parse(it.toString())
        if (date != null) {
            sqlDate = SqlDate(date.time)
        }
    }
    sqlDate
}

val convertToDouble: (Any?) -> Double? = {
    it?.toString()?.toDouble()
}

val convertToFloat: (Any?) -> Float? = {
    it?.toString()?.toFloat()
}

val convertToInt: (Any?) -> Int? = {
    it?.toString()?.toInt()
}

val convertToLong: (Any?) -> Long? = {
    it?.toString()?.toLong()
}

val convertToShort: (Any?) -> Short? = {
    it?.toString()?.toShort()
}

val convertToString: (Any?) -> String? = {
    it?.toString()
}

val convertToTimestamp: (Any?) -> SqlTimestamp? = {
    var sqlTimestamp: SqlTimestamp? = null
    if (it != null) {
        val timestamp = Formats.SQL_DATE_TIME.parse(it.toString())
        if (timestamp != null) {
            sqlTimestamp = SqlTimestamp(timestamp.time)
        }
    }
    sqlTimestamp
}

val convertToUuid: (Any?) -> UUID? = {
    if (it != null) UUID.fromString(it.toString()) else null
}
