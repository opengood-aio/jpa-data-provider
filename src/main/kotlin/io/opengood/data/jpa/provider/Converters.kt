package io.opengood.data.jpa.provider

import io.opengood.data.jpa.provider.constant.Formats
import java.util.UUID
import java.sql.Date as SqlDate

val convertFromDate: (SqlDate?) -> String? = {
    if (it != null) Formats.ISO_DATE.format(it) else null
}

val convertFromUuid: (UUID?) -> String? = {
    it?.toString()
}

val convertToDate: (Any?) -> SqlDate? = {
    var sqlDate: SqlDate? = null
    if (it != null) {
        val date = Formats.ISO_DATE.parse(it.toString())
        if (date != null) sqlDate = SqlDate(date.time)
    }
    sqlDate
}

val convertToString: (Any?) -> String? = {
    it?.toString()
}

val convertToUuid: (Any?) -> UUID? = {
    if (it != null) UUID.fromString(it.toString()) else null
}
