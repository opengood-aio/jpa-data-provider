package app

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID
import java.sql.Date as SqlDate
import java.sql.Timestamp as SqlTimestamp

@Configuration
class TestAppConfig {

    @Bean
    fun clock(): Clock = Clock.fixed(Instant.now(), ZoneId.systemDefault())

    @Bean
    fun dateString(clock: Clock, sqlDateFormatter: DateTimeFormatter): String =
        LocalDate.now(clock).format(sqlDateFormatter)

    @Bean
    fun sqlDateFormatter(): DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    @Bean
    fun sqlDateTimeFormatter(): DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

    @Bean
    fun sqlDate(clock: Clock): SqlDate = SqlDate(clock.millis())

    @Bean
    fun sqlTimestamp(clock: Clock): SqlTimestamp = SqlTimestamp(clock.millis())

    @Bean
    fun timestampString(clock: Clock, sqlDateTimeFormatter: DateTimeFormatter): String =
        LocalDateTime.now(clock).format(sqlDateTimeFormatter)

    @Bean
    fun uuid(): UUID = UUID.randomUUID()
}
