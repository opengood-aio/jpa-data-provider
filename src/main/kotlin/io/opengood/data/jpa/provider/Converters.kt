package io.opengood.data.jpa.provider

import io.opengood.data.jpa.provider.constant.Formats
import java.util.UUID
import java.sql.Date as SqlDate
import java.sql.Timestamp as SqlTimestamp

val convertFromBoolean: (Boolean?) -> String? = {
    it?.toString()
}

val convertFromDate: (SqlDate?) -> String? = {
    if (it != null) Formats.SQL_DATE.format(it) else null
}

val convertFromInt: (Int?) -> String? = {
    it?.toString()
}

val convertFromTimestamp: (SqlTimestamp?) -> String? = {
    if (it != null) Formats.SQL_DATE_TIME.format(it) else null
}

val convertFromUuid: (UUID?) -> String? = {
    it?.toString()
}

val convertToBoolean: (Any?) -> Boolean? = {
    it?.toString()?.toBoolean()
}

val convertToDate: (Any?) -> SqlDate? = {
    var sqlDate: SqlDate? = null
    if (it != null) {
        val date = Formats.SQL_DATE.parse(it.toString())
        if (date != null) sqlDate = SqlDate(date.time)
    }
    sqlDate
}

val convertToInt: (Any?) -> Int? = {
    it?.toString()?.toInt()
}

val convertToString: (Any?) -> String? = {
    it?.toString()
}

val convertToTimestamp: (Any?) -> SqlTimestamp? = {
    var sqlTimestamp: SqlTimestamp? = null
    if (it != null) {
        val timestamp = Formats.SQL_DATE_TIME.parse(it.toString())
        if (timestamp != null) sqlTimestamp = SqlTimestamp(timestamp.time)
    }
    sqlTimestamp
}

val convertToUuid: (Any?) -> UUID? = {
    if (it != null) UUID.fromString(it.toString()) else null
}
