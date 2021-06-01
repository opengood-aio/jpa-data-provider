package helper

import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID
import java.sql.Date as SqlDate

object Values {
    private val timestampString = "${LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)}T10:00:00.00Z"
    private val zoneId: ZoneId = ZoneId.of("US/Eastern")
    private val clock: Clock = Clock.fixed(Instant.parse(timestampString), zoneId)

    val dateString: String = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)
    val sqlDate = SqlDate(clock.millis())
    val uuid: UUID = UUID.randomUUID()
}
